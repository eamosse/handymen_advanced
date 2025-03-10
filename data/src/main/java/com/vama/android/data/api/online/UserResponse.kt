package com.vama.android.data.api.online

import com.squareup.moshi.Json
import com.vama.android.data.model.User
import android.util.Log
import kotlin.math.absoluteValue

// Map pour stocker la correspondance entre IDs MongoDB et IDs locaux
private val idMapping = mutableMapOf<String, Long>()

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
        Log.d("UserResponse", "Conversion de réponse API en objet User")
        Log.d("UserResponse", "ID original: $id")

        // Vérifier si nous avons déjà une correspondance pour cet ID
        val userId = if (idMapping.containsKey(id)) {
            idMapping[id]!!
        } else {
            // Générer un nouvel ID local et le stocker dans la map
            val newId = id.hashCode().toLong().absoluteValue
            idMapping[id] = newId
            newId
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

    companion object {
        // Méthode pour retrouver l'ID MongoDB à partir d'un ID local
        fun getMongoId(localId: Long): String? {
            return idMapping.entries.find { it.value == localId }?.key
        }

        // Méthode pour ajouter un mapping entre ID MongoDB et ID local
        fun addMapping(mongoId: String, localId: Long) {
            Log.d("UserResponse", "Ajout d'un mapping: MongoDB ID $mongoId -> Local ID $localId")
            idMapping[mongoId] = localId
        }
    }
}