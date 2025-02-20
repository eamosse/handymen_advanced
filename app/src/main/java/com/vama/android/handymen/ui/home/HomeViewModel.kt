package com.vama.android.handymen.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vama.android.handymen.domain.UsersUseCase
import com.vama.android.handymen.model.UserModelView
import com.vama.android.data.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val usersUseCase: UsersUseCase,
    private val userRepository: UserRepository
) : ViewModel() {

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

    fun toggleFavorite(userId: Long) {
        viewModelScope.launch {
            userRepository.toggleFavorite(userId)
            loadUsers() // Reload to reflect changes
        }
    }

    fun deleteUser(userId: Long) {
        viewModelScope.launch {
            userRepository.delete(userId)
            loadUsers() // Reload to reflect changes
        }
    }
}
