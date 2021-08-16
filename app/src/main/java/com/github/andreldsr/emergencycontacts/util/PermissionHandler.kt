package com.github.andreldsr.emergencycontacts.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class PermissionHandler {
    companion object {
        fun checkCallPermision(context: Activity): Boolean {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CALL_PHONE
                ) == PackageManager.PERMISSION_DENIED
            ) {
                context.requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), 3333)
                return false
            }
            return true
        }

        fun checkReadContacts(context: Activity): Boolean {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_CONTACTS
                ) == PackageManager.PERMISSION_DENIED
            ) {
                context.requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), 3333)
                return false
            }
            return true
        }
    }
}