package com.vama.android.handymen.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vama.android.data.model.User
import com.vama.android.data.repositories.UserRepository
import com.vama.android.handymen.model.UserModelView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UsersUseCase @Inject constructor(
    private val repository: UserRepository
) {
    /**
     * Retourne une LiveData<List<UserModelView>>
     * en faisant un appel au repository.
     *
     * @return LiveData contenant la liste des UserModelView
     */
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
