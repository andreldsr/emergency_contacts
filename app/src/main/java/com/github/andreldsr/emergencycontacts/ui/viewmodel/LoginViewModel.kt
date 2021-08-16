package com.github.andreldsr.emergencycontacts.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.github.andreldsr.emergencycontacts.model.Resource
import com.github.andreldsr.emergencycontacts.model.User
import com.github.andreldsr.emergencycontacts.repository.UserRepository
import kotlinx.coroutines.Dispatchers

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun login(login: String, password: String): LiveData<Resource<User>> {
        return liveData(Dispatchers.IO) {
            emitSource(userRepository.login(login, password))
        }
    }

    fun getUser(userLogin: String): LiveData<User?> {
        return liveData {
            emitSource(userRepository.getUserByLogin(userLogin))
        }
    }

}