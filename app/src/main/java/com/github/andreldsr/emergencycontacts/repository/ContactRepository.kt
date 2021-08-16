package com.github.andreldsr.emergencycontacts.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.github.andreldsr.emergencycontacts.database.dao.ContactDAO
import com.github.andreldsr.emergencycontacts.model.Contact
import kotlinx.coroutines.Dispatchers

class ContactRepository(private val contactDAO: ContactDAO) {
    suspend fun getListByUser(id: Long): LiveData<List<Contact>> {
        return liveData(Dispatchers.IO){
            emitSource(contactDAO.findByUser(id))
        }
    }

    suspend fun save(contact: Contact): LiveData<Contact> {
        return liveData(Dispatchers.IO){
            contactDAO.save(contact)
            emit(contact)
        }
    }

    suspend fun remove(contact: Contact) {
        contactDAO.delete(contact)
    }
}