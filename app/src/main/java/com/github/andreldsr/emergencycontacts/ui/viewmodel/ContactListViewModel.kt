package com.github.andreldsr.emergencycontacts.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.github.andreldsr.emergencycontacts.model.Contact
import com.github.andreldsr.emergencycontacts.model.User
import com.github.andreldsr.emergencycontacts.repository.ContactRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContactListViewModel(private val contactRepository: ContactRepository) : ViewModel() {
    fun getContactList(user: User): LiveData<List<Contact>> {
        return liveData(Dispatchers.IO) {
            emitSource(contactRepository.getListByUser(user.id))
        }
    }

    fun removeContact(contact: Contact) {
        viewModelScope.launch {
            contactRepository.remove(contact)
        }
    }
}