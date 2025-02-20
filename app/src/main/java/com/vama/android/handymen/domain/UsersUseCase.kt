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
        val liveData = MutableLiveData<List<UserModelView>>()
        doInBackGround(liveData)
        return liveData
    }

    private suspend fun doInBackGround(liveData: MutableLiveData<List<UserModelView>>) =
        withContext(Dispatchers.IO) {
            val users = repository.getAll()
            users.map { it.toModelView() }.also {
                // be careful to use postValue instead of setValue in a background thread
                liveData.postValue(it)
            }
        }
}

private fun User.toModelView() = UserModelView(
    id = id,
    name = name
)