package com.frolfr.ui.createRound

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.frolfr.api.FrolfrApi
import com.frolfr.api.FrolfrAuthorization
import com.frolfr.api.PaginationLinksAdapter
import com.frolfr.api.model.*
import com.frolfr.config.PaginationConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CreateRoundViewModel : ViewModel() {

    private val _courses = MutableLiveData<List<Course2>>()
    val courses: LiveData<List<Course2>>
        get() = _courses

    private val _users = MutableLiveData<List<User2>>()
    val users: LiveData<List<User2>>
        get() = _users

    private val _selectedCourse = MutableLiveData<Course2>()
    val selectedCourse: LiveData<Course2>
        get() = _selectedCourse

    private val _selectedUsers = MutableLiveData<List<User2>>()
    val selectedUsers: LiveData<List<User2>>
        get() = _selectedUsers

    private val _round = MutableLiveData<Round>()
    val round: LiveData<Round>
        get() = _round

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        _courses.value = mutableListOf()
        // TODO findNearbyCourses()
        loadCourses(1)
        loadUsers(1)
    }

    private fun loadCourses(pageNum: Int) {
        coroutineScope.launch {
            try {
                val courses = FrolfrApi.retrofitService.courses(pageNum, PaginationConfig.MAX_PAGE_SIZE, "name")
                if (courses.isEmpty()) {
                    return@launch
                }

                _courses.value = _courses.value?.plus(courses)

                val paginationLinks = courses.first().document.links.get<PaginationLinks>(
                    PaginationLinksAdapter()
                ) as PaginationLinks

                val hasNextPage = paginationLinks.hasNextPage()
                if (hasNextPage) {
                    loadCourses(pageNum + 1)
                }
            } catch (t: Throwable) {
                Log.i("frolfrCourses", "Got error result", t)
            }
        }
    }

    private fun loadUsers(pageNum: Int) {
        coroutineScope.launch {
            try {
                val users = FrolfrApi.retrofitService.users(pageNum, PaginationConfig.MAX_PAGE_SIZE)
                if (users.isEmpty()) {
                    return@launch
                }

                _users.value = _users.value?.plus(users) ?: users

                val thisUser = users.find { user ->
                    user.id.toInt() == FrolfrAuthorization.userId
                }
                if (thisUser != null) {
                    addUser(thisUser)
                }

                val paginationLinks = users.first().document.links.get<PaginationLinks>(
                    PaginationLinksAdapter()
                ) as PaginationLinks

                val hasNextPage = paginationLinks.hasNextPage()
                if (hasNextPage) {
                    loadUsers(pageNum + 1)
                }
            } catch (t: Throwable) {
                Log.i("frolfrFriends", "Got error result", t)
            }
        }
    }

    fun selectCourse(course: Course2?) {
        _selectedCourse.value = course
    }

    fun addUser(user: User2) {
        if (selectedUsers.value == null) {
            _selectedUsers.value = mutableListOf(user)
        } else if (!selectedUsers.value?.contains(user)!!) {
            _selectedUsers.value = _selectedUsers.value!!.plus(user)
        }
    }

    fun createRound() {
        if (_selectedCourse.value == null) {
            _error.value = "Must select a course"
            return
        }
        if (_selectedUsers.value == null || _selectedUsers.value!!.isEmpty()) {
            _error.value = "Must select some players"
            return
        }
        coroutineScope.launch {
            try {
                val roundBody = Round()
                roundBody.setCourse(_selectedCourse.value!!)
                roundBody.setUsers(_selectedUsers.value!!)
                val round = FrolfrApi.retrofitService.createRound(roundBody)
                _round.value = round
                Log.i("createRound", "Created Round with id: ${round.id}")
            } catch (t: Throwable) {
                Log.i("createRound", "Got error result", t)
            }
        }
    }

    fun onRoundNavigated() {
        _round.value = null
    }

    fun clearErrors() {
        _error.value = null
    }
}