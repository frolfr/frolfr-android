package com.frolfr.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.frolfr.domain.model.User
import com.frolfr.domain.repository.UserRepository
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
                _currentUser.value = UserRepository().getCurrentUser()
            } catch (t: Throwable) {
                Log.i("frolfrCurrentUser", "Got error result", t)
                throw t
            }
        }
    }
}