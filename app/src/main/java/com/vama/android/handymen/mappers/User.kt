package com.vama.android.handymen.mappers

import com.vama.android.data.model.User
import com.vama.android.handymen.model.UserModelView

fun User.toModelView() = UserModelView(
    id = id,
    name = name,
    avatarUrl = avatarUrl,
    address = address,
    phoneNumber = phoneNumber,
    aboutMe = aboutMe,
    favorite = favorite,
    webSite = webSite
)