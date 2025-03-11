package com.vama.android.data.services

import com.vama.android.data.model.AddUser
import com.vama.android.data.model.Identity
import com.vama.android.data.model.SortCriteria
import com.vama.android.data.model.User
import com.vama.android.data.utils.DataResult

interface UserService {
    suspend fun getAll(): DataResult<List<User>>
    suspend fun getById(id: Identity): DataResult<User>
    suspend fun add(user: AddUser): DataResult<User>
    suspend fun update(user: User): DataResult<User>
    suspend fun delete(id: Identity): DataResult<Unit>
    suspend fun search(query: String): DataResult<List<User>>
    suspend fun toggleFavorite(id: Identity, toggle: Boolean): DataResult<User>
    suspend fun getFavorites(): DataResult<List<User>>
    suspend fun sortBy(criteria: SortCriteria): DataResult<List<User>>
    suspend fun sync(users: List<User>): DataResult<Unit>
}
