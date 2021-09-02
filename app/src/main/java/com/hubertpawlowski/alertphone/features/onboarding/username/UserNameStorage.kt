package com.hubertpawlowski.alertphone.features.onboarding.username

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.hubertpawlowski.alertphone.Constants

class UserNameStorage(private val context: Context) {

    fun setUserNameSharedPreferences(userName: String) {
        val editor =
            context.getSharedPreferences(Constants.PREF_NAME, AppCompatActivity.MODE_PRIVATE).edit()
        editor.putString(Constants.PREF_USER_NAME, userName)
        editor.apply()
    }
}