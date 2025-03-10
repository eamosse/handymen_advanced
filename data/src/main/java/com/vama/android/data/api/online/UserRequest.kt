package com.vama.android.data.api.online

/**
 * Data class representing a request to create or update a user via the API.
 */
data class UserRequest(
    val name: String,
    val phoneNumber: String,
    val address: String,
    val favorite: Boolean = false,
    val id: String? = null // Optional ID field, used for updates
)