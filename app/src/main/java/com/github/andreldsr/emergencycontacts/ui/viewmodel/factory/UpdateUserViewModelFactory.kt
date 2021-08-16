package com.github.andreldsr.emergencycontacts.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.andreldsr.emergencycontacts.repository.UserRepository
import com.github.andreldsr.emergencycontacts.ui.viewmodel.UpdateUserViewModel

@Suppress("UNCHECKED_CAST")
class UpdateUserViewModelFactory(
    private val userRepository: UserRepository
): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UpdateUserViewModel(userRepository) as T
    }
}