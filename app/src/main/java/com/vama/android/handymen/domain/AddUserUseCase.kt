package com.vama.android.handymen.domain

import com.vama.android.data.model.AddUser
import com.vama.android.data.model.Identity
import com.vama.android.data.model.User
import com.vama.android.data.repositories.UserRepository
import com.vama.android.data.utils.DataResult
import com.vama.android.handymen.mappers.toModelView
import com.vama.android.handymen.model.UserModelView
import javax.inject.Inject

class AddUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(user: AddUser): DataResult<User> =
        repository.add(user)
}