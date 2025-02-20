package com.vama.android.handymen.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vama.android.data.di.DataModule
import com.vama.android.data.model.User
import com.vama.android.handymen.model.UserModelView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UsersUseCase {
    private val repository = DataModule.repository()

    suspend operator fun invoke(): LiveData<List<UserModelView>> {
        return withContext(Dispatchers.IO) {
            MutableLiveData<List<UserModelView>>().apply {
                val users = repository.getAll()
                postValue(users.map { it.toModelView() })
            }
        }
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