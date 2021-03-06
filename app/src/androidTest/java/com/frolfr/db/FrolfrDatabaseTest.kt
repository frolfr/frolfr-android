package com.frolfr.db

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.frolfr.db.dao.CourseDAO
import com.frolfr.db.dao.RoundDAO
import com.frolfr.db.mapper.CourseMapper
import com.frolfr.db.mapper.RoundMapper
import com.frolfr.db.model.CourseEntity
import com.frolfr.db.model.RoundEntity
import com.frolfr.domain.model.Course
import com.frolfr.domain.model.Round
import org.junit.Assert.assertEquals
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.*

/**
 * This is not meant to be a full set of tests. For simplicity, most of your samples do not
 * include tests. However, when building the Room, it is helpful to make sure it works before
 * adding the UI.
 */

@RunWith(AndroidJUnit4::class)
class FrolfrDatabaseTest {

    private lateinit var roundDAO: RoundDAO
    private lateinit var courseDAO: CourseDAO

    private lateinit var db: FrolfrDatabase

    private val courseMapper = CourseMapper()
    private val roundMapper = RoundMapper()

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, FrolfrDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()

        roundDAO = db.roundDAO
        courseDAO = db.courseDAO
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetRound() {
        val course = CourseEntity()
        courseDAO.insert(course)
        val round = RoundEntity()
        round.courseId = 0
        roundDAO.insert(round)
        val round2 = roundDAO.get(0)
        assertEquals(false, round2?.round?.isComplete)
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetCurrentRound() {
        val round = RoundEntity()
        roundDAO.insert(round)
        val round2 = roundDAO.getCurrentRound()
        assertEquals(false, round2?.isComplete)
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetCurrentRoundWithRelations() {
        val course = Course(
            1,
            "Perkerson Park",
            18,
            "Atlanta",
            "GA",
            "US"
        )
        val round = Round(
            1,
            Date(),
            course,
            emptyList(),
            false
        )
        courseDAO.insert(courseMapper.toModel(course))
        roundDAO.insert(roundMapper.toModel(round).round)

        val round2 = roundDAO.get(1)
        assertEquals(false, round2?.round?.isComplete)
        assertEquals("Perkerson Park", round2?.course?.name)

//        val roundsFull = roundDAO.getAllRoundsFull()
//        val rounds = roundDAO.getAllRounds()
//        assertTrue(roundsFull.value?.isNotEmpty() ?: false)
    }
}
