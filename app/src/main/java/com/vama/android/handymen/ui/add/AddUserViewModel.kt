package com.vama.android.handymen.ui.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vama.android.data.di.DataModule
import com.vama.android.data.model.User
import com.vama.android.data.repositories.UserRepository
import com.vama.android.handymen.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddUserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _addSuccess = MutableLiveData<Event<Boolean>>()
    val addSuccess: LiveData<Event<Boolean>> = _addSuccess

    fun addUser(name: String, address: String, phone: String, aboutMe: String, website: String) {
        val newUser = User(
            id = 0,
            name = name,
            avatarUrl = "https://api.dicebear.com/9.x/miniavs/png",
            address = address,
            phoneNumber = phone,
            aboutMe = aboutMe,
            favorite = true,
            webSite = website
        )

        try {
            userRepository.add(newUser)
            _addSuccess.value = Event(true)
        } catch (e: Exception) {
            _addSuccess.value = Event(false)
        }
    }
}