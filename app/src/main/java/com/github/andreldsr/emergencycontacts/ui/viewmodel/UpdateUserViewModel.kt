package com.github.andreldsr.emergencycontacts.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.github.andreldsr.emergencycontacts.model.Resource
import com.github.andreldsr.emergencycontacts.model.User
import com.github.andreldsr.emergencycontacts.repository.UserRepository
import kotlinx.coroutines.Dispatchers

class UpdateUserViewModel(
  private val userRepository: UserRepository
) :ViewModel() {
    fun update(user: User): LiveData<Resource<User>> {
        return liveData(Dispatchers.IO) {
            emitSource(userRepository.update(user))
        }
    }
}