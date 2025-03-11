package com.vama.android.handymen.domain

import android.util.Log
import com.vama.android.data.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SyncDatauseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(forceRefresh: Boolean = true) {
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
                        Log.d(
                            "UsersUseCase",
                            "Synchronisation désactivée, pas de rafraîchissement forcé"
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("UsersUseCase", "Erreur lors de la synchronisation avec le serveur", e)
                // Continuer même en cas d'erreur de synchronisation
            }
        }
    }
}
