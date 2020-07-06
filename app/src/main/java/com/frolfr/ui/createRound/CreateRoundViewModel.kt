package com.frolfr.ui.createRound

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.frolfr.api.FrolfrApi
import com.frolfr.api.model.Course
import com.frolfr.api.model.Course2
import com.frolfr.api.model.User
import com.frolfr.api.model.User2
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

    private val _selectedUsers = MutableLiveData<List<User2>>()
    val selectedUsers: LiveData<List<User2>>
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
                val courses = FrolfrApi.retrofitService.courses()
                _courses.value = courses
            } catch (t: Throwable) {
                Log.i("frolfrCourses", "Got error result", t)
            }
        }
    }

    private fun loadUsers() {
        coroutineScope.launch {
            try {
                val users = FrolfrApi.retrofitService.users()
                _users.value = users
            } catch (t: Throwable) {
                Log.i("frolfrFriends", "Got error result", t)
            }
        }
    }

    fun addUser(user: User2) {
        if (selectedUsers.value == null) {
            _selectedUsers.value = mutableListOf(user)
        } else if (!selectedUsers.value?.contains(user)!!) {
            _selectedUsers.value = _selectedUsers.value!!.plus(user)
        }
    }
}