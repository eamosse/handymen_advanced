package com.vama.android.data.api.online

import com.vama.android.data.model.User

data class UserResponse(
    val id: Long = 0,
    val name: String = "",
    val avatarUrl: String = "",
    val address: String = "",
    val phoneNumber: String = "",
    val aboutMe: String = "",
    val favorite: Boolean = false,
    val webSite: String = ""
) {
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
}