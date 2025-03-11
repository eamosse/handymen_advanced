package com.vama.android.handymen.domain

import com.vama.android.data.repositories.UserRepository
import com.vama.android.data.utils.DataResult
import javax.inject.Inject

class RefreshUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke() = repository.getAll()
}

/**
 * A helper function that maps a [DataResult] to a [DataResult] of another type
 */
fun <T : Any, K : Any> DataResult<T>.mapSuccess(mapper: (T) -> K): DataResult<K> = when (this) {
    is DataResult.Success -> DataResult.Success(mapper(data))
    is DataResult.Error -> this
    is DataResult.Working -> this

}

