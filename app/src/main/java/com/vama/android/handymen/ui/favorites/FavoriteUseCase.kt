package com.vama.android.handymen.domain

import com.vama.android.data.model.User
import com.vama.android.data.repositories.UserRepository
import com.vama.android.handymen.model.UserModelView
import javax.inject.Inject

/**
 * Use case qui gère les opérations liées aux favoris et les transformations de données
 */
class FavoritesUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    /**
     * Récupère la liste des utilisateurs favoris et les transforme en modèles de vue
     */
    suspend fun getFavorites(): List<UserModelView> {
        return userRepository.getFavorites().map { mapToUserModelView(it) }
    }

    /**
     * Bascule l'état favori d'un utilisateur
     */
    suspend fun toggleFavorite(userId: Long) {
        userRepository.toggleFavorite(userId)
    }

    /**
     * Supprime un utilisateur
     */
    suspend fun deleteUser(userId: Long) {
        userRepository.delete(userId)
    }

    /**
     * Convertit une entité User en UserModelView
     */
    private fun mapToUserModelView(user: User): UserModelView {
        return UserModelView(
            id = user.id,
            name = user.name,
            address = user.address,
            phoneNumber = user.phoneNumber,
            webSite = user.webSite,
            aboutMe = user.aboutMe,
            favorite = user.favorite,
            avatarUrl = user.avatarUrl
        )
    }
}