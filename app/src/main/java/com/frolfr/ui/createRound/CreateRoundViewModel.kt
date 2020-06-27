package com.frolfr.ui.createRound

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.frolfr.api.FrolfrApi
import com.frolfr.api.model.Course
import com.frolfr.api.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CreateRoundViewModel : ViewModel() {

    private val _courses = MutableLiveData<List<Course>>()
    val courses: LiveData<List<Course>>
        get() = _courses

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>>
        get() = _users

    private val _selectedUsers = MutableLiveData<List<User>>()
    val selectedUsers: LiveData<List<User>>
        get() = _selectedUsers

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        loadCourses()
        // TODO findNearbyCourses()
        loadUsers()
    }

    private fun loadCourses() {
        coroutineScope.launch {
            try {
                val coursesResponse = FrolfrApi.retrofitService.availableCourses()
                _courses.value = coursesResponse.courses
            } catch (t: Throwable) {
                Log.i("frolfrCourses", "Got error result", t)
            }
        }
    }

    private fun loadUsers() {
        coroutineScope.launch {
            try {
                val usersResponse = FrolfrApi.retrofitService.friends()
                _users.value = usersResponse.friends
            } catch (t: Throwable) {
                Log.i("frolfrFriends", "Got error result", t)
            }
        }
    }

    fun addUser(user: User) {
        if (selectedUsers.value == null) {
            _selectedUsers.value = mutableListOf(user)
        } else if (!selectedUsers.value?.contains(user)!!) {
            _selectedUsers.value = _selectedUsers.value!!.plus(user)
        }
    }
}