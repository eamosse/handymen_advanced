package com.vama.android.data.api.online

package com.vama.android.data.api.online

import com.vama.android.data.model.User
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Data Transfer Object pour l'utilisateur utilisé dans les échanges avec l'API
 */
@JsonClass(generateAdapter = true)
data class UserDto (
    @Json(name = "id") val id: Long = 0,
    @Json(name = "name") val name: String,
    @Json(name = "avatar_url") val avatarUrl: String,
    @Json(name = "address") val address: String,
    @Json(name = "phone_number") val phoneNumber: String,
    @Json(name = "about_me") val aboutMe: String,
    @Json(name = "is_favorite") val isFavorite: Boolean,
    @Json(name = "website") val webSite: String
) {
    /**
     * Convertit cet objet DTO en modèle de domaine User
     */
    fun toUser(): User {
        return User(
            id = id,
            name = name,
            avatarUrl = avatarUrl,
            address = address,
            phoneNumber = phoneNumber,
            aboutMe = aboutMe,
            favorite = isFavorite,
            webSite = webSite
        )
    }

    companion object {
        /**
         * Crée un DTO à partir d'un modèle de domaine User
         */
        fun fromUser(user: User): UserDto {
            return UserDto(
                id = user.id,
                name = user.name,
                avatarUrl = user.avatarUrl,
                address = user.address,
                phoneNumber = user.phoneNumber,
                aboutMe = user.aboutMe,
                isFavorite = user.favorite,
                webSite = user.webSite
            )
        }
    }
}