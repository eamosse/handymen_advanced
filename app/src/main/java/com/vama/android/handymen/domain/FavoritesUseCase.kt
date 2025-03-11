package com.vama.android.handymen.domain

import com.vama.android.data.repositories.UserRepository
import com.vama.android.data.utils.DataResult
import com.vama.android.handymen.mappers.toModelView
import com.vama.android.handymen.model.UserModelView
import javax.inject.Inject

class FavoritesUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(): DataResult<List<UserModelView>> {
        return repository.getFavorites().mapSuccess {result ->
            result.map {
                it.toModelView()
            }
        }
    }
}