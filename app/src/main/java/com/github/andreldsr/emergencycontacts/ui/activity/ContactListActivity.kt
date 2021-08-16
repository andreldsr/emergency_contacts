package com.github.andreldsr.emergencycontacts.ui.activity

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.view.ContextMenu.ContextMenuInfo
import android.widget.AdapterView.AdapterContextMenuInfo
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListAdapter
import android.widget.SimpleAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.github.andreldsr.emergencycontacts.R
import com.github.andreldsr.emergencycontacts.database.AppDatabase
import com.github.andreldsr.emergencycontacts.model.Contact
import com.github.andreldsr.emergencycontacts.model.User
import com.github.andreldsr.emergencycontacts.repository.ContactRepository
import com.github.andreldsr.emergencycontacts.ui.activity.extensions.logout
import com.github.andreldsr.emergencycontacts.ui.activity.extensions.navigatePassingUser
import com.github.andreldsr.emergencycontacts.ui.viewmodel.ContactListViewModel
import com.github.andreldsr.emergencycontacts.ui.viewmodel.factory.ContactListViewModelFactory
import com.github.andreldsr.emergencycontacts.util.PermissionHandler
import kotlinx.android.synthetic.main.activity_contact_list.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ContactListActivity : AppCompatActivity() {
    private val user by lazy {
        intent.extras?.get(USER_KEY) as User
    }
    val viewModel by lazy {
        val repository = ContactRepository(AppDatabase.getInstance(this).contactDAO)
        val factory = ContactListViewModelFactory(repository)
        ViewModelProvider(this, factory).get(ContactListViewModel::class.java)
    }
    private lateinit var simpleAdapter: ListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_list)
        setupListeners()
        setupList()
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

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater
            .inflate(R.menu.activity_contact_list_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        if (itemId == R.id.activity_contact_list_menu_remove) {
            removeContactDialog(item)
        }
        return super.onContextItemSelected(item)
    }

    private fun removeContactDialog(item: MenuItem) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.remove_contact))
            .setMessage(getString(R.string.confirm_remove_contact))
            .setPositiveButton(R.string.yes) { _: DialogInterface?, _: Int ->
                val menuInfo = item.menuInfo as AdapterContextMenuInfo
                val contactMap = simpleAdapter.getItem(menuInfo.position) as Map<String, Any?>
                viewModel.removeContact(contactFromMap(contactMap))
            }
            .setNegativeButton("Não", null)
            .show()
    }

    private fun contactFromMap(contactMap: Map<String, Any?>): Contact = Contact(
        id = contactMap["id"] as Long,
        name = contactMap["name"] as String,
        number = contactMap["number"] as String,
        userId = user.id
    )

    private fun setupList() {
        viewModel.getContactList(user).observe(this) {
            if (it == null)
                return@observe
            val itemDataList = getItemDataList(it)
            simpleAdapter = configureAdapter(itemDataList)
            activity_contact_list_lv_contact_list.adapter = simpleAdapter
            activity_contact_list_lv_contact_list.onItemClickListener =
                OnItemClickListener { adapterView, view, i, l ->
                    if (!PermissionHandler.checkCallPermision(this))
                        return@OnItemClickListener
                    callContact(itemDataList[i]["number"] as String)
                }
        }
        registerForContextMenu(activity_contact_list_lv_contact_list)

    }

    private fun callContact(number: String) {
        val uri = Uri.parse("tel:$number")
        val intent = Intent(Intent.ACTION_CALL, uri)
        startActivity(intent)
    }

    private fun configureAdapter(itemDataList: ArrayList<Map<String, Any?>>): ListAdapter =
        SimpleAdapter(
            this,
            itemDataList,
            R.layout.list_view_layout_imagem,
            arrayOf("imageId", "name", "abrevs"),
            intArrayOf(R.id.userImage, R.id.userTitle, R.id.userAbrev)
        )


    private fun getItemDataList(contactList: List<Contact>): ArrayList<Map<String, Any?>> {
        val itemDataList = ArrayList<Map<String, Any?>>()
        contactList.sortedBy { contact -> contact.name }.map { contact ->
            val listItemMap: MutableMap<String, Any?> = HashMap()
            listItemMap["imageId"] = R.drawable.ic_action_ligar_list
            listItemMap["name"] = contact.name
            listItemMap["abrevs"] = contact.name[0]
            listItemMap["number"] = contact.number
            listItemMap["id"] = contact.id
            itemDataList.add(listItemMap)
        }
        return itemDataList
    }

    private fun setupListeners() {
        activity_contact_list_bottom_navigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_menu_change_contacts -> navigatePassingUser(
                    ChangeContactsActivity::class.java,
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