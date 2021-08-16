package com.github.andreldsr.emergencycontacts.ui.viewmodel

import android.content.ContentResolver
import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.*
import com.github.andreldsr.emergencycontacts.model.Contact
import com.github.andreldsr.emergencycontacts.model.User
import com.github.andreldsr.emergencycontacts.repository.ContactRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChangeContactsViewModel(private val contactRepository: ContactRepository) : ViewModel() {
    private val liveData = MutableLiveData<List<Contact>>()

    fun getContactList() = liveData

    fun updateContactList(searchName: String, userId: Long, cr: ContentResolver): LiveData<List<Contact>> {
        val consulta = ContactsContract.Contacts.DISPLAY_NAME + " LIKE ?"
        val argumentosConsulta = arrayOf("%$searchName%")
        val cursor = cr.query(
            ContactsContract.Contacts.CONTENT_URI, null,
            consulta, argumentosConsulta, null
        )
        val nomesContatos = arrayOfNulls<String>(cursor!!.count)
        val telefonesContatos = arrayOfNulls<String>(cursor.count)
        val contacts = mutableListOf<Contact>()
        Log.v("PDM", "Tamanho do cursor:" + cursor.count)

        var i = 0
        while (cursor.moveToNext()) {
            val indiceNome =
                cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME)
            val contatoNome = cursor.getString(indiceNome)
            Log.v("PDM", "Contato $i, Nome:$contatoNome")
            nomesContatos[i] = contatoNome
            val indiceContatoID = cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID)
            val contactID = cursor.getString(indiceContatoID)
            val consultaPhone =
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactID
            val phones = cr.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, consultaPhone, null, null
            )
            while (phones?.moveToNext() == true) {
                val number =
                    phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                telefonesContatos[i] = number //Salvando só último telefone
            }
            contacts.add(
                Contact(
                    name = nomesContatos[i]!!,
                    number = telefonesContatos[i]!!,
                    userId = userId
                )
            )
            i++
        }
        liveData.value = contacts
        return liveData
    }

    fun getContacts(user: User): LiveData<List<Contact>> {
        return liveData(Dispatchers.IO){
            emitSource(contactRepository.getListByUser(user.id))
        }
    }

    fun addContact(contact: Contact): LiveData<Contact> {
        return liveData(Dispatchers.IO) {
            emitSource(contactRepository.save(contact))
        }
    }
}