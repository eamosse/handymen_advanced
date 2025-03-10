package com.vama.android.data.database

import com.vama.android.data.model.User

/**
 * Fichier contenant les fonctions d'extension pour la conversion entre les entités
 * de base de données et les modèles du domaine.
 */

/**
 * Convertit une entité UserEntity en modèle de domaine User
 */
fun UserEntity.toUser(): User = User(
    id = id,
    name = name,
    avatarUrl = avatarUrl,
    address = address,
    phoneNumber = phoneNumber,
    aboutMe = aboutMe,
    favorite = favorite,
    webSite = webSite
)

/**
 * Convertit un modèle de domaine User en entité UserEntity
 */
fun User.toEntity(): UserEntity = UserEntity(
    id = id,
    name = name,
    avatarUrl = avatarUrl,
    address = address,
    phoneNumber = phoneNumber,
    aboutMe = aboutMe,
    favorite = favorite,
    webSite = webSite
)

/**
 * Fonction d'extension pour créer une entité UserEntity avec des valeurs par défaut
 * utile pour créer de nouvelles entités à partir de données partielles
 */
fun User.toNewEntity(): UserEntity = UserEntity(
    id = 0, // Forcer l'ID à 0 pour que Room génère un nouvel ID
    name = name,
    avatarUrl = avatarUrl,
    address = address,
    phoneNumber = phoneNumber,
    aboutMe = aboutMe,
    favorite = favorite,
    webSite = webSite
)