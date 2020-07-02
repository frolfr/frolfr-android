package com.frolfr.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.frolfr.api.FrolfrApi
import com.frolfr.api.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    private val _currentUser = MutableLiveData<User>()
    val currentUser: LiveData<User> = _currentUser

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    fun setupCurrentUser() {
        coroutineScope.launch {
            try {
                val userResponse = FrolfrApi.retrofitService.currentUser()

                _currentUser.value = User(
                    userResponse.id.toInt(),
                    userResponse.email,
                    userResponse.firstName,
                    null,
                    userResponse.lastName,
                    userResponse.avatarUrl
                )
            } catch (t: Throwable) {
                Log.i("frolfrCurrentUser", "Got error result", t)
            }
        }
    }
}