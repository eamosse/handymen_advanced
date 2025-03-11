package com.vama.android.data.model

data class Identity(val localId: Long?, val externalId: String?) : Comparable<Identity> {
    override fun compareTo(other: Identity): Int {
        return when {
            localId != null && other.localId != null -> localId.compareTo(other.localId)
            externalId != null && other.externalId != null -> externalId.compareTo(other.externalId)
            else -> 0
        }
    }
}


data class User(
    val id: Identity,
    val name: String,
    val avatarUrl: String,
    val address: String,
    val phoneNumber: String,
    val aboutMe: String,
    val favorite: Boolean,
    val webSite: String
)

data class AddUser(
    val name: String,
    val avatarUrl: String,
    val address: String,
    val phoneNumber: String,
    val aboutMe: String,
    val favorite: Boolean,
    val webSite: String
)