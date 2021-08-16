package com.github.andreldsr.emergencycontacts.ui.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.github.andreldsr.emergencycontacts.R
import com.github.andreldsr.emergencycontacts.database.AppDatabase
import com.github.andreldsr.emergencycontacts.model.Contact
import com.github.andreldsr.emergencycontacts.model.User
import com.github.andreldsr.emergencycontacts.repository.ContactRepository
import com.github.andreldsr.emergencycontacts.ui.activity.extensions.logout
import com.github.andreldsr.emergencycontacts.ui.activity.extensions.navigatePassingUser
import com.github.andreldsr.emergencycontacts.ui.activity.extensions.showMessage
import com.github.andreldsr.emergencycontacts.ui.viewmodel.ChangeContactsViewModel
import com.github.andreldsr.emergencycontacts.ui.viewmodel.factory.ChangeContactsViewModelFactory
import com.github.andreldsr.emergencycontacts.util.PermissionHandler
import kotlinx.android.synthetic.main.activity_change_contacts.*

class ChangeContactsActivity : AppCompatActivity() {
    val user by lazy {
        intent.extras?.get(USER_KEY) as User
    }
    val viewModel by lazy {
        val repository = ContactRepository(AppDatabase.getInstance(this).contactDAO)
        val factory = ChangeContactsViewModelFactory(repository)
        ViewModelProvider(this, factory).get(ChangeContactsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_contacts)
        setNavigationMenuItem()
        setupList()
        setupListeners()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.activity_main_logout_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.activity_main_logout_menu_item -> {
                logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupList() {
        viewModel.getContactList().observe(this) {
            for (j in 0..it.size) {
                val adapter: ArrayAdapter<String?> =
                    ArrayAdapter(
                        this,
                        R.layout.list_view_layout,
                        it.map { contact -> contact.name })
                activity_change_contacts_lv_contact_list.adapter = adapter
                activity_change_contacts_lv_contact_list.onItemClickListener =
                    OnItemClickListener { _, _, j, _ ->
                        createDialog(it[j]).show()
                    }
            }
        }
    }

    private fun createDialog(contact: Contact): AlertDialog {
        return AlertDialog.Builder(this)
            .setMessage(R.string.confirm_add_contact)
            .setTitle(R.string.add_contact)
            .setCancelable(true)
            .setNegativeButton(R.string.cancel) { dialog, id ->
                dialog.cancel()
            }
            .setPositiveButton(R.string.confirm) { dialog, id ->
                run {
                    val contactsLiveData = viewModel.getContacts(user)
                    contactsLiveData
                        .observe(this) {
                            if (it.size < 5) {
                                addContact(contact)
                                contactsLiveData.removeObservers(this)
                            } else {
                                showMessage(getString(R.string.contact_full_list_error))
                            }
                        }
                }
            }
            .create()
    }

    private fun addContact(contact: Contact) {
        viewModel.addContact(contact).observe(this) {
            if (it != null)
                showMessage("Sucesso")
        }
    }

    private fun setNavigationMenuItem() {
        activity_change_contacts_bottom_navigation.selectedItemId =
            R.id.navigation_menu_change_contacts
    }

    private fun setupListeners() {
        activity_change_contacts_bt_search_button.setOnClickListener {
            if(!PermissionHandler.checkReadContacts(this))
                return@setOnClickListener
            val searchName = activity_change_contacts_edt_search_field.text.toString()
            val cr = contentResolver
            viewModel.updateContactList(searchName, user.id, cr)
        }
        activity_change_contacts_bottom_navigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_menu_call -> navigatePassingUser(
                    ContactListActivity::class.java,
                    user
                )
                R.id.navigation_menu_profile -> navigatePassingUser(
                    UpdateUserActivity::class.java,
                    user
                )
                else -> false
            }
        }
    }
}