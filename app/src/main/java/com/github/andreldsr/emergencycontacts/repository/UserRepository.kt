package com.github.andreldsr.emergencycontacts.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.github.andreldsr.emergencycontacts.database.dao.UserDAO
import com.github.andreldsr.emergencycontacts.model.Resource
import com.github.andreldsr.emergencycontacts.model.User
import kotlinx.coroutines.Dispatchers


class UserRepository(private val userDAO: UserDAO) {

    suspend fun register(user: User): LiveData<Resource<User>> {
        return liveData(Dispatchers.IO) {
            validateUser(user).let {
                if (it != null) {
                    emit(Resource(error = it))
                    return@liveData
                }
            }
            if (userDAO.findByLogin(user.login) != null) {
                emit(Resource(error = "User already exists."))
                return@liveData
            }
            userDAO.save(user)
            emit(Resource(value = user))
        }
    }

    private fun validateUser(user: User): String? {
        val errorMessage = "You must fill all fields"
        if (user.name.isEmpty() || user.email.isEmpty() || user.password.isEmpty() || user.login.isEmpty())
            return errorMessage
        return null
    }

    suspend fun login(login: String, password: String): LiveData<Resource<User>> {
        return liveData(Dispatchers.IO){
            val user = userDAO.findByLogin(login)

            if (user == null || user.password != password) {
                emit(Resource(error = "Login/Password incorrect"))
                return@liveData
            }
            emit(Resource(value = user))
        }
    }

    suspend fun update(user: User): LiveData<Resource<User>> {
        return liveData(Dispatchers.IO){
            validateUser(user).let {
                if (it != null) {
                    emit(Resource(error = it))
                    return@liveData
                }
            }
            userDAO.save(user)
            emit(Resource(value = user))
        }
    }

    fun getUserByLogin(userLogin: String): LiveData<User?> {
        return liveData(Dispatchers.IO){
            val user = userDAO.findByLogin(userLogin)
            emit(user)
        }
    }

}