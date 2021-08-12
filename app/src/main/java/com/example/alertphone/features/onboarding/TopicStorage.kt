package com.example.alertphone.features.onboarding

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.alertphone.Constants

class TopicStorage(private val context: Context) {

     fun setGroupNameSharedPreferences(topic: String) {
        val editor = context.getSharedPreferences(Constants.PREF_NAME, AppCompatActivity.MODE_PRIVATE).edit()
        editor.putString(Constants.PREF_GROUP_NAME, topic)
        editor.apply()
    }
}