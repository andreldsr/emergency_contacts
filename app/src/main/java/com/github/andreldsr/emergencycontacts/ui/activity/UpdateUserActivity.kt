package com.github.andreldsr.emergencycontacts.ui.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.github.andreldsr.emergencycontacts.R
import com.github.andreldsr.emergencycontacts.database.AppDatabase
import com.github.andreldsr.emergencycontacts.model.User
import com.github.andreldsr.emergencycontacts.repository.UserRepository
import com.github.andreldsr.emergencycontacts.ui.activity.extensions.*
import com.github.andreldsr.emergencycontacts.ui.viewmodel.UpdateUserViewModel
import com.github.andreldsr.emergencycontacts.ui.viewmodel.factory.UpdateUserViewModelFactory
import kotlinx.android.synthetic.main.activity_update_user.*

class UpdateUserActivity : AppCompatActivity() {
    val user by lazy {
        intent.extras?.get(USER_KEY) as User
    }

    private val viewModel by lazy {
        val repository = UserRepository(AppDatabase.getInstance(this).userDAO)
        val factory = UpdateUserViewModelFactory(repository)
        ViewModelProvider(this, factory).get(UpdateUserViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_user)
        setNavigationMenuItem()
        setupListeners()
        fillFormFields()
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

    private fun fillFormFields() {
        activity_update_user_edt_name.setText(user.name)
        activity_update_user_edt_login.setText(user.login)
        activity_update_user_edt_email.setText(user.email)
        activity_update_user_edt_password.setText(user.password)
        activity_update_user_sw_keep_logged.isChecked = user.keepLogged
    }

    private fun setNavigationMenuItem() {
        activity_update_user_bottom_navigation.selectedItemId = R.id.navigation_menu_profile
    }

    private fun setupListeners() {
        activity_update_user_btn_update.setOnClickListener {
            val updateUser = getUserFromFormFields()
            viewModel.update(updateUser).observe(this){
                it.error?.let { error -> showMessage(error) }
                it.value?.let { user -> run {
                    if(user.keepLogged){
                        saveKeepLogged(user)
                    }else{
                        removeKeepLogged()
                    }
                    navigatePassingUser(this::class.java, user)
                } }
            }
        }
        activity_update_user_bottom_navigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_menu_change_contacts -> navigatePassingUser(
                    ChangeContactsActivity::class.java,
                    user
                )
                R.id.navigation_menu_call -> navigatePassingUser(
                    ContactListActivity::class.java,
                    user
                )
                else -> false
            }
        }
    }

    private fun getUserFromFormFields(): User {
        val name = activity_update_user_edt_name.text.toString()
        val login = activity_update_user_edt_login.text.toString()
        val email = activity_update_user_edt_email.text.toString()
        val password = activity_update_user_edt_password.text.toString()
        val keepLogged = activity_update_user_sw_keep_logged.isChecked
        return User(
            id = user.id,
            name,
            login, password, email,
            keepLogged,
            darkTheme = user.darkTheme
        )
    }
}