package com.github.andreldsr.emergencycontacts.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.andreldsr.emergencycontacts.repository.ContactRepository
import com.github.andreldsr.emergencycontacts.ui.viewmodel.ChangeContactsViewModel

@Suppress("UNCHECKED_CAST")
class ChangeContactsViewModelFactory(
    private val contactRepository: ContactRepository
): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ChangeContactsViewModel(contactRepository) as T
    }
}