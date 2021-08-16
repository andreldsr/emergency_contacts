package com.github.andreldsr.emergencycontacts.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.github.andreldsr.emergencycontacts.model.Contact

@Dao
interface ContactDAO {
    @Query("SELECT c.* FROM Contact c WHERE c.userId = :userId")
    fun findByUser(userId: Long): LiveData<List<Contact>>

    @Delete
    suspend fun delete(contact: Contact)

    @Insert(onConflict = REPLACE)
    suspend fun save(contact: Contact)
}