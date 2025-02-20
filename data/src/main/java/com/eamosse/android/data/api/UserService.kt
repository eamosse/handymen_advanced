package com.eamosse.android.data.api

import com.eamosse.android.data.model.User

internal interface UserService {
    fun getAll(): List<User>
}

// We don't want this implementation to be visible by other modules
// Only the interface should be visible
internal class InMemoryUserService : UserService {
    private val _users = Dummy_Users.toMutableList()
    override fun getAll(): List<User> {
        // We return a copy of the list
        return _users.toList()
    }

}

