package com.github.andreldsr.emergencycontacts.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.andreldsr.emergencycontacts.repository.ContactRepository
import com.github.andreldsr.emergencycontacts.ui.viewmodel.ContactListViewModel

@Suppress("UNCHECKED_CAST")
class ContactListViewModelFactory(
    private val contactRepository: ContactRepository
): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ContactListViewModel(contactRepository) as T
    }
}