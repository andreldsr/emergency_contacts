package com.github.andreldsr.emergencycontacts.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.github.andreldsr.emergencycontacts.R
import com.github.andreldsr.emergencycontacts.database.AppDatabase
import com.github.andreldsr.emergencycontacts.model.User
import com.github.andreldsr.emergencycontacts.repository.UserRepository
import com.github.andreldsr.emergencycontacts.ui.activity.extensions.showMessage
import com.github.andreldsr.emergencycontacts.ui.viewmodel.RegisterViewModel
import com.github.andreldsr.emergencycontacts.ui.viewmodel.factory.RegisterViewModelFactory
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    private val viewModel by lazy {
        val repository = UserRepository(AppDatabase.getInstance(this).userDAO)
        val factory = RegisterViewModelFactory(repository)
        ViewModelProvider(this, factory).get(RegisterViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setupListeners()
    }

    private fun setupListeners() {
        activity_register_btn_register.setOnClickListener {
            register()
        }
    }

    private fun register() {
        val name = activity_register_edt_name.text.toString()
        val login = activity_register_edt_login.text.toString()
        val email = activity_register_edt_email.text.toString()
        val password = activity_register_edt_password.text.toString()
        val keepLogged = activity_register_sw_keep_logged.isChecked
        val user = User(
            name = name,
            login = login,
            email = email,
            password = password,
            keepLogged = keepLogged,
            darkTheme = false
        )
        viewModel.register(user).observe(this) {
            if (it?.value != null)
                finish()
            it?.error?.let { it1 -> showMessage(it1) }
        }
    }

}