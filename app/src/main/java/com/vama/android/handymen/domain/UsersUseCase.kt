package com.vama.android.handymen.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.vama.android.data.model.User
import com.vama.android.data.repositories.UserRepository
import com.vama.android.handymen.model.UserModelView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UsersUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(): LiveData<List<UserModelView>> = repository.getAll().map {
        it.map { user -> user.toModelView() }
    }
}

private fun User.toModelView() = UserModelView(
    id = id,
    name = name,
    avatarUrl = avatarUrl,
    address = address,
    phoneNumber = phoneNumber,
    aboutMe = aboutMe,
    favorite = favorite,
    webSite = webSite
)
