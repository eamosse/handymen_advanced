package com.vama.android.data.api.online

import com.squareup.moshi.Json

/**
 * Data class representing a request to create or update a user via the API.
 */
data class UserRequest(
    @Json(name = "name") val name: String,
    @Json(name = "phone") val phone: String,
    @Json(name = "address") val address: String,
    @Json(name = "isFavorite") val isFavorite: Boolean = false,
    @Json(name = "avatarUrl") val avatarUrl: String = "https://api.dicebear.com/9.x/miniavs/png",
    @Json(name = "aboutMe") val aboutMe: String = "",
    @Json(name = "webSite") val webSite: String = ""
)