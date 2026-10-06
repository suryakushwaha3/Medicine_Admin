package com.example.medicineadmin.view.Notification


import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.medicineadmin.R

object NotificationHelper {

    const val CHANNEL_ID = "medicine_admin_notifications"

    fun createChannel(context: Context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val manager =
                context.getSystemService(
                    Context.NOTIFICATION_SERVICE
                ) as NotificationManager

            val channel = NotificationChannel(
                CHANNEL_ID,
                "Medicine Admin Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Medicine Admin alerts"
                enableVibration(true)
            }

            manager.createNotificationChannel(channel)
        }
    }

    fun show(
        context: Context,
        title: String,
        message: String,
        notificationId: Int
    ) {

        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        createChannel(context)

        val notification =
            NotificationCompat.Builder(
                context,
                CHANNEL_ID
            )
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(message)
                )
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(
                    NotificationCompat.CATEGORY_MESSAGE
                )
                .setAutoCancel(true)
                .build()

        NotificationManagerCompat
            .from(context)
            .notify(
                notificationId,
                notification
            )
    }
}