package com.frolfr.domain.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.frolfr.api.FrolfrApi
import com.frolfr.api.PaginationLinksAdapter
import com.frolfr.api.model.PaginationLinks
import com.frolfr.db.FrolfrDatabase
import com.frolfr.db.mapper.CourseMapper
import com.frolfr.db.model.ApiSyncEntity
import com.frolfr.db.model.EntityType
import com.frolfr.domain.model.Course
import java.util.*

class CourseRepository {

    private val apiService = FrolfrApi.retrofitService
    private val dbService = FrolfrDatabase.getInstance()

    private val dbCourseMapper = CourseMapper()
    private val apiCourseMapper = com.frolfr.api.mapper.CourseMapper()

    fun getCourse(courseId: Int): LiveData<Course> {
        val dbCourse = dbService.courseDAO.get(courseId)
        return Transformations.map(dbCourse) {
            it?.let {
                dbCourseMapper.fromModel(it)
            }
        }
    }

    fun getCourses(): LiveData<List<Course>> {
        val dbCourses = dbService.courseDAO.getAllLive()
        return Transformations.map(dbCourses) { dbCourses ->
            dbCourses.map { dbCourse ->
                dbCourseMapper.fromModel(dbCourse)
            }
        }
    }

    fun getCoursesRaw(): List<Course> {
        val dbCourses = dbService.courseDAO.getAll()
        return dbCourses.map { dbCourse ->
            dbCourseMapper.fromModel(dbCourse)
        }
    }

    suspend fun fetchAllCourses() {
        var coursesSync = dbService.apiSyncDAO.get(EntityType.COURSE) ?: ApiSyncEntity(EntityType.COURSE)
        var page = 1
        do {
            val coursesDocument = apiService.courses(page++)
            val paginationLinks = coursesDocument.links
                .get<PaginationLinks>(PaginationLinksAdapter()) as PaginationLinks

            val courses = coursesDocument.map { apiCourseMapper.toDomain(it) }

            coursesSync.minId = coursesSync.minId.coerceAtMost(courses.first().id)
            coursesSync.maxId = coursesSync.maxId.coerceAtLeast(courses.last().id)
            coursesSync.lastSyncedAt = Date().time

            persistCourses(courses)
            dbService.apiSyncDAO.insert(coursesSync)
        } while (paginationLinks.hasNextPage())

        coursesSync.hasFullHistory = true
        dbService.apiSyncDAO.insert(coursesSync)
    }

    suspend fun fetchMissingCourses() {
        var coursesSync = dbService.apiSyncDAO.get(EntityType.COURSE)
        // TODO can make the `!coursesSync.hasFullHistory` more efficient
        if (coursesSync == null || !coursesSync.hasFullHistory) {
            fetchAllCourses()
        } else {
            fetchCoursesSince(coursesSync.maxId)
        }
    }

    private suspend fun fetchCoursesSince(latestId: Int) {
        var page = 1
        do {
            val coursesDocument = apiService.courses(page++, sort = "-id")
            val paginationLinks = coursesDocument.links
                .get<PaginationLinks>(PaginationLinksAdapter()) as PaginationLinks

            val courses = coursesDocument.map { apiCourseMapper.toDomain(it) }
            val coursesSince = courses.filter { it.id > latestId }
            val endReached = coursesSince.isEmpty() || coursesSince.size < courses.size

            persistCourses(courses)
        } while (paginationLinks.hasNextPage() && !endReached)
    }

    private fun persistCourses(courses: List<Course>) {
        if (courses.isEmpty()) return

        val dbCourses = courses.map { dbCourseMapper.toModel(it) }

        dbService.courseDAO.insert(dbCourses)
    }

}