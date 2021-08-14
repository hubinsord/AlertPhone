package com.example.alertphone.features.alert

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.alertphone.Constants
import com.google.firebase.messaging.FirebaseMessaging

class GroupAlertSubscriber(private val context: Context) {

    fun subscribeToGroup()  {
        val topic = getGroupName()
        FirebaseMessaging.getInstance().subscribeToTopic(topic!!)
    }

    fun getGroupName(): String? {
        val prefs = context.getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE)
        return prefs.getString(Constants.PREF_GROUP_NAME, "")
    }
}