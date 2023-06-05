package com.example.alarmmanager

import android.provider.Settings.Global
import androidx.lifecycle.ViewModel
import com.example.alarmmanager.Dao.AlarmDao
import com.example.alarmmanager.model.Alarm
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainViewModel(val dao : AlarmDao) : ViewModel() {

    val alarms = dao.getAll()

    fun insert(alarm: Alarm){
        GlobalScope.launch {
            dao.insertAlarm(alarm)
        }
    }

    fun update(alarm: Alarm){
        GlobalScope.launch {
            dao.updateAlarm(alarm)
        }
    }
}