package com.github.andreldsr.emergencycontacts.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.andreldsr.emergencycontacts.database.dao.UserDAO
import com.github.andreldsr.emergencycontacts.repository.UserRepository
import com.github.andreldsr.emergencycontacts.ui.viewmodel.LoginViewModel

@Suppress("UNCHECKED_CAST")
class LoginViewModelFactory(
    private val userRepository: UserRepository
): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel(userRepository) as T
    }
}