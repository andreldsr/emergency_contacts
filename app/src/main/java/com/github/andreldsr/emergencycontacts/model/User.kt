package com.github.andreldsr.emergencycontacts.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val login: String,
    val password: String,
    val email: String,
    @ColumnInfo(name = "keep_logged")
    val keepLogged: Boolean = false,
    @ColumnInfo(name = "dark_theme")
    val darkTheme: Boolean = false,
): Serializable {
}