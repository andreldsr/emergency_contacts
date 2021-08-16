package com.github.andreldsr.emergencycontacts.ui.viewmodel

import androidx.lifecycle.*
import com.github.andreldsr.emergencycontacts.model.Resource
import com.github.andreldsr.emergencycontacts.model.User
import com.github.andreldsr.emergencycontacts.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun register(user: User): LiveData<Resource<User>> {
        return liveData(Dispatchers.IO) {
            emitSource(userRepository.register(user))
        }
    }
}