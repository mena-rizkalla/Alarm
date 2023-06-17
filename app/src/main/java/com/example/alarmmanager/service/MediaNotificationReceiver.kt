package com.example.alarmmanager.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.alarmmanager.MusicControl

class MediaNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, p1: Intent?) {
       MusicControl.getInstance(context).stopMusic()
        Toast.makeText(context,"music",Toast.LENGTH_SHORT).show()
    }
}