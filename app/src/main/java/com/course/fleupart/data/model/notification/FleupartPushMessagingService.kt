package com.course.fleupart.data.model.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.course.fleupart.MainActivity
import com.course.fleupart.R
import com.course.fleupart.data.repository.NotificationRepository
import com.course.fleupart.data.resource.Resource
import com.course.fleupart.di.Injection
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FleupartPushMessagingService: FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Panggil repository untuk kirim token ke backend (harus di background thread)
        CoroutineScope(Dispatchers.IO).launch {
            Injection.provideNotificationRepository(Resource.appContext).saveToken(token)
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Priority: Baca dari data payload dulu, lalu fallback ke notification payload
        val notifType = remoteMessage.data["type"] ?: "general"

        val title = remoteMessage.data["title"]
            ?: remoteMessage.notification?.title
            ?: "Fleupart"

        val body = remoteMessage.data["body"]
            ?: remoteMessage.notification?.body
            ?: "Ada notifikasi baru dari Fleupart"

        val channelId = when (notifType) {
            "promo" -> NotificationChannels.PROMO_CHANNEL_ID
            "order" -> NotificationChannels.ORDER_CHANNEL_ID
            else -> NotificationChannels.GENERAL_CHANNEL_ID
        }

        showNotification(title, body, channelId)
    }

    private fun showNotification(title: String, message: String, channelId: String) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_fleura_id)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(pendingIntent)

        notificationManager.notify(channelId.hashCode(), notificationBuilder.build())
    }
}