package com.example.alarmmanager

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.example.alarmmanager.service.AlarmReceiver

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
               AlarmReceiver.CHANNEL_ID,
                "Alarm Manager",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            channel.description = "Channel for Alarm Manager"
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}