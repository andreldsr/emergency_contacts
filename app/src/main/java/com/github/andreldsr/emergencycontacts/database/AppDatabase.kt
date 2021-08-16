package com.github.andreldsr.emergencycontacts.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.github.andreldsr.emergencycontacts.database.dao.ContactDAO
import com.github.andreldsr.emergencycontacts.database.dao.UserDAO
import com.github.andreldsr.emergencycontacts.model.Contact
import com.github.andreldsr.emergencycontacts.model.User

private const val DATABASE_NAME = "contacts.db"

@Database(entities = [User::class, Contact::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract val userDAO: UserDAO
    abstract val contactDAO: ContactDAO


    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance: AppDatabase? = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        DATABASE_NAME
                    ).fallbackToDestructiveMigration()
                        .build()
                }
                return instance
            }
        }

    }

}