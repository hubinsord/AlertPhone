package com.example.alertphone.architecture.pushnotification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log.i
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.alertphone.R
import com.example.alertphone.features.alert.MainActivity

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        i(MyFirebaseMessagingService::class.java.name, "NewToken arrived: $p0")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        i("TEST", "TEST fire base not")
//        val title = remoteMessage.notification?.title
//        val message = remoteMessage.notification?.body
        val map: Map<String,String> = remoteMessage.data
        val title = map["title"]
        val message = map["message"]
        sendNotification(title, message)
    }

    private fun sendNotification(title: String?, message: String?) {
        i("TEST", "TEST sendNotification")

        val intent = MainActivity.newIntent(this, true)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, FLAG_UPDATE_CURRENT);
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "alert_channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(channelId, notificationManager)
        }
        val defaultSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);

        val notificationBuilder =
            buildNotification(channelId, title, message, defaultSoundUri, pendingIntent)

        notificationManager.notify(0, notificationBuilder)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(
        channelId: String,
        notificationManager: NotificationManager,
    ) {
        val channelName = "Channel human readable title"
        val channel =
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(channel)
    }

    private fun buildNotification(
        channelId: String,
        title: String?,
        message: String?,
        defaultSoundUri: Uri,
        pendingIntent: PendingIntent?,
    ) = NotificationCompat.Builder(this, channelId)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setChannelId(channelId)
        .setContentTitle(title)
        .setContentText(message)
        .setAutoCancel(true)
        .setSound(defaultSoundUri)
        .setContentIntent(pendingIntent)
        .build()
}
