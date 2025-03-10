package com.vama.android.handymen.domain

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.vama.android.data.model.User
import com.vama.android.data.repositories.UserRepository
import com.vama.android.handymen.model.UserModelView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UsersUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(forceRefresh: Boolean = true): LiveData<List<UserModelView>> {
        Log.d("UsersUseCase", "Récupération des utilisateurs (forceRefresh=$forceRefresh)")

        // Si forceRefresh est vrai, synchroniser avec le serveur avant de récupérer les données
        if (forceRefresh) {
            try {
                withContext(Dispatchers.IO) {
                    // Tenter de synchroniser les données avec le serveur
                    if (repository.isSyncEnabled()) {
                        Log.d("UsersUseCase", "Synchronisation avec le serveur en cours...")
                        repository.syncData()
                        Log.d("UsersUseCase", "Synchronisation réussie")
                    } else {
                        Log.d("UsersUseCase", "Synchronisation désactivée, pas de rafraîchissement forcé")
                    }
                }
            } catch (e: Exception) {
                Log.e("UsersUseCase", "Erreur lors de la synchronisation avec le serveur", e)
                // Continuer même en cas d'erreur de synchronisation
            }
        }

        // Récupérer les données du repository et les convertir en modèles de vue
        return repository.getAll().map { users ->
            Log.d("UsersUseCase", "Conversion de ${users.size} utilisateurs en modèles de vue")
            users.map { user -> user.toModelView() }
        }
    }
}

private fun User.toModelView() = UserModelView(
    id = id,
    name = name,
    avatarUrl = avatarUrl,
    address = address,
    phoneNumber = phoneNumber,
    aboutMe = aboutMe,
    favorite = favorite,
    webSite = webSite
)