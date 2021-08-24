package com.hubertpawlowski.alertphone.features.alert

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.hubertpawlowski.alertphone.Constants
import com.google.firebase.messaging.FirebaseMessaging
import com.hubertpawlowski.alertphone.BuildConfig

class GroupAlertSubscriber(private val context: Context) {

    fun subscribeToGroup()  {
        val topic = getGroupName()
        val path = "/topics/${BuildConfig.FLAVOR}$topic"
        FirebaseMessaging.getInstance().subscribeToTopic(path)
    }

    fun getGroupName(): String? {
        val prefs = context.getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE)
        return prefs.getString(Constants.PREF_GROUP_NAME, "")
    }
}