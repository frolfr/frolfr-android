package com.frolfr.ui.createRound

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.frolfr.domain.model.Course
import com.frolfr.domain.model.Round
import com.frolfr.domain.model.User
import com.frolfr.domain.repository.CourseRepository
import com.frolfr.domain.repository.RoundRepository
import com.frolfr.domain.repository.UserRepository
import kotlinx.coroutines.*

class CreateRoundViewModel : ViewModel() {

    private val _courses: LiveData<List<Course>> = loadCourses()
    val courses: LiveData<List<Course>>
        get() = _courses

    private val _users: LiveData<List<User>> = loadUsers()
    val users: LiveData<List<User>>
        get() = _users

    private val _selectedCourse = MutableLiveData<Course>()
    val selectedCourse: LiveData<Course>
        get() = _selectedCourse

    private val _selectedUsers = MutableLiveData<List<User>>()
    val selectedUsers: LiveData<List<User>>
        get() = _selectedUsers

    private val _round = MutableLiveData<Round>()
    val round: LiveData<Round>
        get() = _round

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val _coursesFetched = MutableLiveData<Boolean>()
    private val _additionalCoursesFetched = MutableLiveData<Boolean>()

    private val _usersFetched = MutableLiveData<Boolean>()
    private val _additionalUsersFetched = MutableLiveData<Boolean>()

    private var viewModelJob = Job()
    private val ioScope = CoroutineScope(viewModelJob + Dispatchers.IO)
    private val mainScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        // TODO findNearbyCourses()
    }

    private fun loadCourses(): LiveData<List<Course>> {
        return CourseRepository().getCourses()
    }

    fun fetchCourses() {
        _coursesFetched.value = true
        ioScope.launch {
            CourseRepository().fetchAllCourses()
        }
    }

    fun fetchAdditionalCourses() {
        _additionalCoursesFetched.value = true
        ioScope.launch {
            CourseRepository().fetchMissingCourses()
        }
    }

    fun fetchedCourses(): Boolean {
        return _coursesFetched.value ?: false
    }

    fun fetchedAdditionalCourses(): Boolean {
        return _additionalCoursesFetched.value ?: false
    }

    private fun loadUsers(): LiveData<List<User>> {
        return UserRepository().getUsers()
    }

    fun fetchUsers() {
        _usersFetched.value = true
        ioScope.launch {
            UserRepository().fetchAllUsers()
        }
    }

    fun fetchAdditionalUsers() {
        _additionalUsersFetched.value = true
        ioScope.launch {
            UserRepository().fetchMissingUsers()
        }
    }

    fun fetchedUsers(): Boolean {
        return _usersFetched.value ?: false
    }

    fun fetchedAdditionalUsers(): Boolean {
        return _additionalUsersFetched.value ?: false
    }

    fun selectCourse(course: Course?) {
        _selectedCourse.value = course
    }

    fun addUser(user: User) {
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
        mainScope.launch {
            var round: Round? = null
            ioScope.launch {
                try {
                    round = RoundRepository().createRound(
                        _selectedCourse.value!!,
                        _selectedUsers.value!!
                    )
                } catch (t: Throwable) {
                    Log.i("createRound", "Got error result", t)
                }
            }.join()

            _round.value = round
            Log.i("createRound", "Created Round with id: ${round?.id}")
        }
    }

    fun onRoundNavigated() {
        _round.value = null
    }

    fun clearErrors() {
        _error.value = null
    }
}