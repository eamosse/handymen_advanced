package com.vama.android.handymen.ui.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vama.android.data.repositories.UserRepository
import com.vama.android.handymen.model.CreateUserModel
import com.vama.android.handymen.model.UserMapper
import com.vama.android.handymen.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddUserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper
) : ViewModel() {

    private val _addSuccess = MutableLiveData<Event<Boolean>>()
    val addSuccess: LiveData<Event<Boolean>> = _addSuccess

    private val _validationError = MutableLiveData<Event<String>>()
    val validationError: LiveData<Event<String>> = _validationError

    fun addUser(name: String, address: String, phone: String, aboutMe: String, website: String) {
        // Créer le modèle dédié à la création d'utilisateur
        val createUserModel = CreateUserModel(
            name = name,
            address = address,
            phoneNumber = phone,
            aboutMe = aboutMe,
            webSite = website
        )

        // Valider les données
        if (!createUserModel.isValid()) {
            createUserModel.getValidationErrorMessage()?.let { errorMessage ->
                _validationError.value = Event(errorMessage)
            }
            return
        }

        viewModelScope.launch {
            try {
                // Convertir le modèle de création en entité User via le mapper
                val user = userMapper.toUser(createUserModel)

                // Persister l'utilisateur
                userRepository.add(user)
                _addSuccess.value = Event(true)
            } catch (e: Exception) {
                _addSuccess.value = Event(false)
            }
        }
    }
}