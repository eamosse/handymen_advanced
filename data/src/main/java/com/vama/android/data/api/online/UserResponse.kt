package com.vama.android.data.api.online

import com.squareup.moshi.Json
import com.vama.android.data.model.User
// Dans UserResponse.kt

import android.util.Log
import kotlin.math.absoluteValue

data class UserResponse(
    @Json(name = "_id") val id: String = "",
    @Json(name = "name") val name: String = "",
    @Json(name = "avatarUrl") val avatarUrl: String = "",
    @Json(name = "address") val address: String = "",
    @Json(name = "phone") val phoneNumber: String = "",
    @Json(name = "aboutMe") val aboutMe: String = "",
    @Json(name = "isFavorite") val favorite: Boolean = false,
    @Json(name = "webSite") val webSite: String = ""
) {
    fun toUser(): User {
        // Ajout de logs pour voir la conversion
        Log.d("UserResponse", "Conversion de réponse API en objet User")
        Log.d("UserResponse", "ID original: $id")

        val userId = try {
            // MongoDB retourne des IDs comme "60f5e8b3e6b3a50015b9e1a2"
            // qui ne peuvent pas être convertis en Long directement
            // Utilisons le hashCode comme solution temporaire
            id.hashCode().toLong().absoluteValue
        } catch (e: Exception) {
            Log.e("UserResponse", "Erreur lors de la conversion de l'ID", e)
            0L
        }

        Log.d("UserResponse", "ID converti: $userId")

        return User(
            id = userId,
            name = name,
            avatarUrl = avatarUrl,
            address = address,
            phoneNumber = phoneNumber,
            aboutMe = aboutMe,
            favorite = favorite,
            webSite = webSite
        )
    }
}