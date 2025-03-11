package com.vama.android.handymen.ui.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vama.android.data.model.AddUser
import com.vama.android.handymen.domain.AddUserUseCase
import com.vama.android.handymen.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddUserViewModel @Inject constructor(
    private val addUserUseCase: AddUserUseCase
) : ViewModel() {

    private val _addSuccess = MutableLiveData<Event<Boolean>>()
    val addSuccess: LiveData<Event<Boolean>> = _addSuccess

    fun addUser(name: String, address: String, phone: String, aboutMe: String, website: String) {
        viewModelScope.launch {
            try {
                // Générer une URL d'avatar unique basée sur le nom pour s'assurer que chaque utilisateur a un avatar différent
                val avatarUrl = "https://api.dicebear.com/9.x/miniavs/png?seed=${name.hashCode()}"

                val newUser = AddUser(
                    name = name,
                    avatarUrl = avatarUrl,
                    address = address,
                    phoneNumber = phone,
                    aboutMe = aboutMe,
                    favorite = false, // Par défaut, un nouvel utilisateur n'est pas favori
                    webSite = website
                )

                addUserUseCase(newUser)
                _addSuccess.value = Event(true)
            } catch (e: Exception) {
                _addSuccess.value = Event(false)
            }
        }
    }
}