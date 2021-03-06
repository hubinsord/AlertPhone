package com.hubertpawlowski.alertphone.features.alert

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.hubertpawlowski.alertphone.BuildConfig
import com.hubertpawlowski.alertphone.data.main.api.NotificationApiInstance
import com.hubertpawlowski.alertphone.data.main.model.NotificationData
import com.hubertpawlowski.alertphone.data.main.model.PushNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlertSender {

    init {
        getToken()
    }

    fun send(title: String, message: String, groupName: String, userId: Long) {
        val pushNotification = createPushNotification(title, message, groupName, userId)
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

    private fun createPushNotification(title: String, message: String, groupName: String?, userId: Long): PushNotification {
        val notificationData = NotificationData(title, message, userId)
        return PushNotification(notificationData, "/topics/${BuildConfig.FLAVOR}$groupName")
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
