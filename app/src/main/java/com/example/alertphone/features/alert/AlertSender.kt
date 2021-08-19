package com.example.alertphone.features.alert

import android.util.Log
import com.example.alertphone.data.main.api.NotificationApiInstance
import com.example.alertphone.data.main.model.NotificationData
import com.example.alertphone.data.main.model.PushNotification
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class AlertSender {

    init {
        getToken()
    }

    fun send(title: String, message: String, groupName: String) {
        val pushNotification = createPushNotification(title, message, groupName)
        sendNotification(pushNotification)
    }

    private fun getToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            val token = task.result         // Gets new FCM registration token
            Log.d("Refreshed token: ", token!!)
        })
    }

    private fun createPushNotification(
        title: String,
        message: String,
        groupName: String?,
    ): PushNotification {
        val notificationData = NotificationData(title, message)
        return PushNotification(notificationData, "/topics/$groupName")
    }

    private fun sendNotification(pushNotification: PushNotification) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = NotificationApiInstance.api.postNotification(pushNotification)
                if (response.isSuccessful)
                    Log.d("TAG", "Response successful}")
                else
                    Log.e("TAG", response.errorBody().toString())
            } catch (e: Exception) {
                Log.e("TAG", e.toString())
            }
        }
}
