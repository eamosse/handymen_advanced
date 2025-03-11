package com.vama.android.data.online

import android.util.Log
import com.vama.android.data.model.AddUser
import com.vama.android.data.model.Identity
import com.vama.android.data.model.SortCriteria
import com.vama.android.data.model.User
import com.vama.android.data.online.api.ApiService
import com.vama.android.data.online.mappers.toRequest
import com.vama.android.data.services.UserService
import com.vama.android.data.utils.DataResult
import com.vama.android.data.utils.parse
import com.vama.android.data.utils.safeCall
import javax.inject.Inject

private fun Identity.toExternalId() =
    externalId ?: throw IllegalArgumentException("User does not have an id")

/**
 * Service to interact with the online API. No logic should be implemented here. Whatever logic should be implemented in the API
 */

class OnlineUserService @Inject constructor(
    private val apiService: ApiService
) : UserService {

    override suspend fun getAll(): DataResult<List<User>> = safeCall {
        Log.d("OnlineUserService", "Récupération de tous les utilisateurs")
        val users = apiService.getAll().parse {
            it.map { userResponse ->
                userResponse.toUser()
            }
        }
        users
    }


    override suspend fun getById(id: Identity): DataResult<User> = safeCall {
        val user = apiService.get(id.toExternalId()).parse {
            it.toUser()
        }
        user
    }

    override suspend fun add(user: AddUser): DataResult<User> = safeCall {
        val request = user.toRequest()
        apiService.create(request).parse {
            it.toUser()
        }
    }

    override suspend fun update(user: User): DataResult<User> = safeCall {
        val request = user.toRequest()
        apiService.update(
            user.id.externalId ?: throw IllegalArgumentException("User does not have an API ID"),
            request
        ).parse {
            it.toUser()
        }
    }

    override suspend fun delete(id: Identity): DataResult<Unit> = safeCall {
        apiService.delete(id.toExternalId()).parse {
            Unit
        }

    }

    override suspend fun search(query: String): DataResult<List<User>> = safeCall {
        apiService.search(query).parse {
            it.map { userResponse ->
                userResponse.toUser()
            }
        }
    }

    override suspend fun toggleFavorite(id: Identity, toggle: Boolean) = safeCall {
        apiService.toggle(id.toExternalId(), toggle).parse {
            it.toUser()
        }
    }

    override suspend fun getFavorites(): DataResult<List<User>> = safeCall {
        apiService.getFavorites().parse {
            it.map { userResponse ->
                userResponse.toUser()
            }
        }
    }

    override suspend fun sortBy(criteria: SortCriteria): DataResult<List<User>> = safeCall {
        apiService.getAll(criteria).parse {
            it.map { userResponse ->
                userResponse.toUser()
            }
        }
    }

    override suspend fun sync(users: List<User>) = safeCall {
        apiService.sync(users.map { it.toRequest() }).parse {

        }
    }
}