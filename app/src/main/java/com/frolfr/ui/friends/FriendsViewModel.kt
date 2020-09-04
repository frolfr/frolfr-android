package com.frolfr.ui.friends

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.frolfr.domain.model.User
import com.frolfr.domain.repository.UserRepository
import kotlinx.coroutines.*

class FriendsViewModel : ViewModel() {

    val friends: LiveData<List<User>> = loadFriends()
    private val _friendsFetched = MutableLiveData<Boolean>()
    private val _additionalFriendsFetched = MutableLiveData<Boolean>()

    private val _navigateToFriendDetail = MutableLiveData<User>()
    val navigateToFriendDetail: LiveData<User>
        get() = _navigateToFriendDetail

    private val _refreshComplete = MutableLiveData<Boolean>()
    val refreshComplete: LiveData<Boolean>
        get() = _refreshComplete

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.IO)


    fun onFriendClicked(friend: User) {
        _navigateToFriendDetail.value = friend
    }

    fun onFriendNavigated() {
        _navigateToFriendDetail.value = null
    }

    private fun loadFriends(): LiveData<List<User>> {
        return UserRepository().getFriends()
    }

    fun fetchFriends() {
        _friendsFetched.value = true
        coroutineScope.launch {
            UserRepository().fetchAllFriends()
        }
    }

    fun fetchAdditionalFriends() {
        _additionalFriendsFetched.value = true
        coroutineScope.launch {
            UserRepository().fetchMissingFriends()
        }
    }

    fun fetchedFriends(): Boolean {
        return _friendsFetched.value ?: false
    }

    fun fetchedAdditionalFriends(): Boolean {
        return _additionalFriendsFetched.value ?: false
    }

    fun refreshFriends() {
        GlobalScope.launch(Dispatchers.Main) {
            coroutineScope.launch {
                UserRepository().fetchMissingFriends()
            }.join()
            _refreshComplete.value = true
        }
    }

    fun onRefreshCompleteAcknowledged() {
        _refreshComplete.value = false
    }
}