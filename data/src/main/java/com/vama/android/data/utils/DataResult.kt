package com.vama.android.data.utils

/**
 * A generic class that holds the state of data source operation.
 * @param <T>
 */
sealed class DataResult<out T : Any> {
    data object Working : DataResult<Nothing>()
    data class Success<out T : Any>(val data: T) : DataResult<T>()
    data class Error(
        val exception: Throwable,
        val code: Int,
        val message: String
    ) : DataResult<Nothing>()
}