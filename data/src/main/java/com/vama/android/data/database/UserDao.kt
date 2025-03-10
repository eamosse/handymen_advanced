package com.vama.android.data.database

import androidx.room.*

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getAll(): List<UserEntity>

    @Query("SELECT * FROM users WHERE id = :id")
    fun getById(id: Long): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: UserEntity): Long

    @Update
    fun update(user: UserEntity)

    @Query("DELETE FROM users WHERE id = :id")
    fun delete(id: Long)

    @Query("SELECT * FROM users WHERE name LIKE '%' || :query || '%' OR phoneNumber LIKE '%' || :query || '%' OR address LIKE '%' || :query || '%'")
    fun search(query: String): List<UserEntity>

    @Query("UPDATE users SET favorite = NOT favorite WHERE id = :id")
    fun toggleFavorite(id: Long)

    @Query("SELECT * FROM users WHERE favorite = 1")
    fun getFavorites(): List<UserEntity>
}