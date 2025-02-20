package com.vama.android.handymen.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vama.android.data.di.DataModule
import com.vama.android.data.repositories.UserRepository
import com.vama.android.handymen.domain.UsersUseCase
import com.vama.android.handymen.model.UserModelView
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val usersUseCase = UsersUseCase()
    private val _users = MutableLiveData<List<UserModelView>>()
    val users: LiveData<List<UserModelView>> = _users

    init {
        loadUsers()
    }

    fun loadUsers() {
        viewModelScope.launch {
            usersUseCase().value?.let { newUsers ->
                _users.postValue(newUsers)
            }
        }
    }
}