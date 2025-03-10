package com.vama.android.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vama.android.data.model.User

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val avatarUrl: String,
    val address: String,
    val phoneNumber: String,
    val aboutMe: String,
    val favorite: Boolean,
    val webSite: String
) {
    // TODO On utilise généralement des fonctions d'extensions pour convertir des entités en modèles et vice versa
    fun toUser(): User = User(
        id = id,
        name = name,
        avatarUrl = avatarUrl,
        address = address,
        phoneNumber = phoneNumber,
        aboutMe = aboutMe,
        favorite = favorite,
        webSite = webSite
    )

    companion object {
        // TODO Fonctions d'extensions
        fun fromUser(user: User): UserEntity = UserEntity(
            id = user.id,
            name = user.name,
            avatarUrl = user.avatarUrl,
            address = user.address,
            phoneNumber = user.phoneNumber,
            aboutMe = user.aboutMe,
            favorite = user.favorite,
            webSite = user.webSite
        )
    }
}
