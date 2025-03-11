package com.vama.android.data.utils

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException


internal fun <T : Any, K : Any> Response<T>.parse(parser: (T) -> K): DataResult<K> {
    return if (isSuccessful) {
        body()?.let {
            DataResult.Success(parser(it))
        } ?: run {
            DataResult.Error(
                exception = NoDataException(),
                message = "Aucune donnée",
                code = 404
            )
        }
    } else {
        DataResult.Error(
            exception = Exception(),
            message = message(),
            code = code()
        )
    }
}

class NoDataException : Exception()
class NetworkException : Exception()
class UnAuthenticatedException : Exception()

/**
 * An utility function to make a safe call to the any data source
 * This will guarantee that the call is made on the IO thread and will handle any exception
 *
 * @param execute the suspend function to execute
 * @return DataResult<T>
 */
internal suspend fun <T : Any> safeCall(execute: suspend () -> DataResult<T>): DataResult<T> {
    return try {
        withContext(Dispatchers.IO) {
            execute()
        }
    } catch (e: Exception) {
        Log.e("Error in API CALL", e.message, e)
        when (e) {
            is IOException -> {
                DataResult.Error(
                    exception = NetworkException(),
                    message = "Problème d'accès au réseau",
                    code = -1
                )
            }

            is HttpException -> {
                if (e.code() == 401) {
                    DataResult.Error(
                        exception = UnAuthenticatedException(),
                        message = "Problème d'authentification",
                        code = e.code()
                    )
                } else {
                    DataResult.Error(
                        exception = e,
                        message = e.message ?: "No message",
                        code = e.code()
                    )
                }
            }

            else -> {
                DataResult.Error(
                    exception = e,
                    message = e.message ?: "No message",
                    code = -1
                )
            }
        }
    }
}