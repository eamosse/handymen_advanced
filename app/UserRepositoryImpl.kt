package com.vama.android.data.repositories

import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    // éventuelles dépendances
) : UserRepository {

    override fun getAll(): List<User> {
        // ...
    }
    override fun add(user: User) {
        // ...
    }
    // etc.
}
