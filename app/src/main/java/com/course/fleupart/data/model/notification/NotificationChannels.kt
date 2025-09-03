package com.course.fleupart.data.model.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

object NotificationChannels {
    // ID dan info setiap channel
    const val GENERAL_CHANNEL_ID = "seller_general_channel"
    const val GENERAL_CHANNEL_NAME = "General Notifications"
    const val GENERAL_CHANNEL_DESC = "Notifikasi umum untuk penjual"

    const val PROMO_CHANNEL_ID = "seller_promo_channel"
    const val PROMO_CHANNEL_NAME = "Promo & Penawaran"
    const val PROMO_CHANNEL_DESC = "Informasi promo dan penawaran untuk penjual"

    const val ORDER_CHANNEL_ID = "seller_order_channel"
    const val ORDER_CHANNEL_NAME = "Pesanan & Transaksi"
    const val ORDER_CHANNEL_DESC = "Update status pesanan dan transaksi penjual"
}

object NotificationChannelUtil {
    fun createAllNotificationChannels(context: Context) {
        val channels = listOf(
            NotificationChannel(
                NotificationChannels.GENERAL_CHANNEL_ID,
                NotificationChannels.GENERAL_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply { description = NotificationChannels.GENERAL_CHANNEL_DESC },

            NotificationChannel(
                NotificationChannels.PROMO_CHANNEL_ID,
                NotificationChannels.PROMO_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply { description = NotificationChannels.PROMO_CHANNEL_DESC },

            NotificationChannel(
                NotificationChannels.ORDER_CHANNEL_ID,
                NotificationChannels.ORDER_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply { description = NotificationChannels.ORDER_CHANNEL_DESC }
        )
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        channels.forEach { notificationManager.createNotificationChannel(it) }
    }
}