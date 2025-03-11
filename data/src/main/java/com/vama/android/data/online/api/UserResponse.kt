package com.vama.android.data.online.api

import com.squareup.moshi.Json
import com.vama.android.data.model.Identity
import com.vama.android.data.model.User


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
        return User(
            // well, since we get the data from the API, we can't have a local ID. At least not yet.
            id = Identity(localId = null, externalId = id),
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