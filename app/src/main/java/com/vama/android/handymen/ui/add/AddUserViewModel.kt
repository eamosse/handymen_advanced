package com.vama.android.handymen.ui.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vama.android.data.model.User
import com.vama.android.data.preferences.DataSource
import com.vama.android.data.repositories.UserRepository
import com.vama.android.handymen.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddUserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _addSuccess = MutableLiveData<Event<Boolean>>()
    val addSuccess: LiveData<Event<Boolean>> = _addSuccess

    fun addUser(name: String, address: String, phone: String, aboutMe: String, website: String) {
        viewModelScope.launch {
            try {
                // Générer une URL d'avatar unique basée sur le nom pour s'assurer que chaque utilisateur a un avatar différent
                val avatarUrl = "https://api.dicebear.com/9.x/miniavs/png?seed=${name.hashCode()}"

                val newUser = User(
                    id = 0, // L'ID sera généré par l'API
                    name = name,
                    avatarUrl = avatarUrl,
                    address = address,
                    phoneNumber = phone,
                    aboutMe = aboutMe,
                    favorite = false, // Par défaut, un nouvel utilisateur n'est pas favori
                    webSite = website
                )

                // S'assurer que le mode en ligne est activé
                if (userRepository.getCurrentDataSource() != DataSource.ONLINE) {
                    userRepository.setDataSource(DataSource.ONLINE)
                }

                // Activer la synchronisation si elle n'est pas encore activée
                if (!userRepository.isSyncEnabled()) {
                    userRepository.setSyncEnabled(true)
                }

                userRepository.add(newUser)
                _addSuccess.value = Event(true)
            } catch (e: Exception) {
                _addSuccess.value = Event(false)
            }
        }
    }

    // Méthode pour changer la source de données
    fun setDataSource(source: DataSource) {
        userRepository.setDataSource(source)
    }
}