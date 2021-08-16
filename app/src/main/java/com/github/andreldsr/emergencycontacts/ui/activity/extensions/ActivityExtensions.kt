package com.github.andreldsr.emergencycontacts.ui.activity.extensions

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.widget.Toast
import com.github.andreldsr.emergencycontacts.model.User
import com.github.andreldsr.emergencycontacts.ui.activity.ChangeContactsActivity
import com.github.andreldsr.emergencycontacts.ui.activity.KEEP_LOGGED_KEY
import com.github.andreldsr.emergencycontacts.ui.activity.LoginActivity
import com.github.andreldsr.emergencycontacts.ui.activity.USER_KEY

fun Activity.showMessage(error: String) {
    Toast.makeText(
        this,
        error,
        Toast.LENGTH_LONG
    ).show()
}

fun Activity.navigatePassingUser(activity: Class<*>, user: User): Boolean {
    val intent = Intent(this, activity)
    intent.putExtra(USER_KEY, user)
    startActivity(intent)
    finish()
    return true
}

fun Activity.logout(){
    removeKeepLogged()
    val intent = Intent(this, LoginActivity::class.java)
    startActivity(intent)
    finish()
}

fun Activity.saveKeepLogged(user: User) {
    val sharedPreferences = ContextWrapper(this).getSharedPreferences("contacts", Context.MODE_PRIVATE)
    with(sharedPreferences.edit()){
        putString(KEEP_LOGGED_KEY, user.login)
        commit()
    }
}

fun Activity.removeKeepLogged(){
    val sharedPreferences = ContextWrapper(this).getSharedPreferences("contacts", Context.MODE_PRIVATE)
    with(sharedPreferences.edit()){
        remove(KEEP_LOGGED_KEY)
        commit()
    }
}