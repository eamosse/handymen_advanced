package com.vama.android.data.online.mappers

import com.vama.android.data.model.AddUser
import com.vama.android.data.model.User
import com.vama.android.data.online.api.UserRequest

internal fun AddUser.toRequest() = UserRequest(
    name = this.name,
    phone = this.phoneNumber,
    address = this.address,
    isFavorite = this.favorite,
    avatarUrl = this.avatarUrl,
    aboutMe = this.aboutMe,
    webSite = this.webSite
)

internal fun User.toRequest() = UserRequest(
    name = this.name,
    phone = this.phoneNumber,
    address = this.address,
    isFavorite = this.favorite,
    avatarUrl = this.avatarUrl,
    aboutMe = this.aboutMe,
    webSite = this.webSite
)