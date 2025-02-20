package com.vama.android.handymen.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vama.android.handymen.domain.UsersUseCase
import com.vama.android.handymen.model.UserModelView
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val usersUseCase = UsersUseCase()

    // A mediator live data to store the list of users, we keep it private to avoid external modification
    private val _users = MediatorLiveData<List<UserModelView>>()

    // A read-only data to expose the list of users, this is made public to be observed by the view
    val users: LiveData<List<UserModelView>> = _users

    init {
        // Since the usersUseCase is a suspend function, we need to call it inside a coroutine
        viewModelScope.launch {
            // A mediator live data can observe multiple live data sources, and update its value when any of them changes
            _users.addSource(usersUseCase()) {
                _users.value = it
            }
        }
    }
}