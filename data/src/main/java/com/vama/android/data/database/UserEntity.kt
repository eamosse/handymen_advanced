package com.vama.android.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vama.android.data.model.Identity
import com.vama.android.data.model.User

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    // TODO make sure that externalId is unique to avoid duplicates when syncing with the server
    val externalId: String?,
    val name: String,
    val avatarUrl: String,
    val address: String,
    val phoneNumber: String,
    val aboutMe: String,
    val favorite: Boolean,
    val webSite: String
)
