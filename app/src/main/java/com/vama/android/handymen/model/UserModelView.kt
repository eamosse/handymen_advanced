package com.vama.android.handymen.model

import com.vama.android.data.model.Identity

data class UserModelView(
    val id: Identity,
    val name: String,
    val avatarUrl: String,
    val address: String,
    val phoneNumber: String,
    val aboutMe: String,
    val favorite: Boolean,
    val webSite: String
)