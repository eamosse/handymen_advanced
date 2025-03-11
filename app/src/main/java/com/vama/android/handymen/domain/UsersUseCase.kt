package com.vama.android.handymen.domain

import androidx.lifecycle.map
import com.vama.android.data.repositories.UserRepository
import com.vama.android.handymen.mappers.toModelView
import javax.inject.Inject

class UsersUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke() = repository.users.map { result ->
        result.mapSuccess { users -> users.map { it.toModelView() } }
    }
}


