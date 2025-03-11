package com.vama.android.data.database.mappers

import com.vama.android.data.database.UserEntity
import com.vama.android.data.model.AddUser
import com.vama.android.data.model.Identity
import com.vama.android.data.model.User

internal fun AddUser.toEntity() = UserEntity(
    externalId = null,
    name = name,
    avatarUrl = avatarUrl,
    address = address,
    phoneNumber = phoneNumber,
    aboutMe = aboutMe,
    favorite = favorite,
    webSite = webSite
)

fun UserEntity.toUser(): User = User(
    id = Identity(localId = id, externalId = null),
    name = name,
    avatarUrl = avatarUrl,
    address = address,
    phoneNumber = phoneNumber,
    aboutMe = aboutMe,
    favorite = favorite,
    webSite = webSite
)

fun User.toEntity() = UserEntity(
    id = id.localId ?: 0,
    externalId = id.externalId,
    name = this.name,
    avatarUrl = this.avatarUrl,
    address = this.address,
    phoneNumber = this.phoneNumber,
    aboutMe = this.aboutMe,
    favorite = this.favorite,
    webSite = this.webSite
)