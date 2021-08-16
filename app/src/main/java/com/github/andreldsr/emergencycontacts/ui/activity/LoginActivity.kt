package com.github.andreldsr.emergencycontacts.ui.activity

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.github.andreldsr.emergencycontacts.R
import com.github.andreldsr.emergencycontacts.database.AppDatabase
import com.github.andreldsr.emergencycontacts.model.User
import com.github.andreldsr.emergencycontacts.repository.UserRepository
import com.github.andreldsr.emergencycontacts.ui.activity.extensions.navigatePassingUser
import com.github.andreldsr.emergencycontacts.ui.activity.extensions.saveKeepLogged
import com.github.andreldsr.emergencycontacts.ui.activity.extensions.showMessage
import com.github.andreldsr.emergencycontacts.ui.viewmodel.LoginViewModel
import com.github.andreldsr.emergencycontacts.ui.viewmodel.factory.LoginViewModelFactory
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {
    private val viewModel by lazy {
        val repository = UserRepository(AppDatabase.getInstance(this).userDAO)
        val factory = LoginViewModelFactory(repository)
        ViewModelProvider(this, factory).get(LoginViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setupListeners()
        checkKeepLogged()
    }

    private fun checkKeepLogged() {
        val sharedPreferences = ContextWrapper(this).getSharedPreferences("contacts",Context.MODE_PRIVATE)
        val userLogin = sharedPreferences.getString(KEEP_LOGGED_KEY, "")
        viewModel.getUser(userLogin!!).observe(this) {
            if (it == null)
                return@observe
            activity_login_edt_login.setText(it.login)
            activity_login_edt_password.setText(it.password)
            login()
        }

    }

    private fun setupListeners() {
        activity_login_btn_register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        activity_login_btn_login.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val login = activity_login_edt_login.text.toString()
        val password = activity_login_edt_password.text.toString()
        viewModel.login(login, password).observe(this) {
            if (it.error != null) {
                showMessage(it.error)
            }
            it.value?.let { user ->
                if (user.keepLogged)
                    saveKeepLogged(user)
                navigatePassingUser(ContactListActivity::class.java, user)
            }
        }
    }

}