package com.vama.android.handymen.model

import com.vama.android.data.model.User
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Classe utilitaire pour mapper entre les différents modèles d'utilisateur
 * Suit le pattern Mapper qui isole la logique de conversion
 */
@Singleton
class UserMapper @Inject constructor() {

    /**
     * Convertit un modèle de création en entité User pour la persistance
     */
    fun toUser(createUserModel: CreateUserModel): User {
        return User(
            id = 0, // L'ID sera généré par la source de données
            name = createUserModel.name,
            avatarUrl = generateAvatarUrl(createUserModel.name),
            address = createUserModel.address,
            phoneNumber = createUserModel.phoneNumber,
            aboutMe = createUserModel.aboutMe,
            favorite = false, // Par défaut, un nouvel utilisateur n'est pas en favori
            webSite = createUserModel.webSite
        )
    }

    /**
     * Génère une URL d'avatar basée sur le nom
     */
    private fun generateAvatarUrl(name: String): String {
        // Encode le nom pour l'URL si nécessaire
        val encodedName = name.replace(" ", "+")
        return "https://api.dicebear.com/9.x/miniavs/png?seed=$encodedName"
    }
}