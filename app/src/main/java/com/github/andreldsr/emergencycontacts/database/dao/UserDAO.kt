package com.github.andreldsr.emergencycontacts.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.github.andreldsr.emergencycontacts.model.User

@Dao
interface UserDAO {
    @Insert(onConflict = REPLACE)
    suspend fun save(user: User)
    @Query("SELECT * FROM User WHERE login = :login")
    fun findByLogin(login: String): User?
    @Delete
    suspend fun delete(user: User)
}