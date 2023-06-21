package com.example.alarmmanager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alarmmanager.Dao.AlarmDao
import com.example.alarmmanager.model.Alarm
import kotlinx.coroutines.launch

class MainViewModel(val dao : AlarmDao) : ViewModel() {

    val alarms = dao.getAll()

    fun insert(alarm: Alarm){
        viewModelScope.launch {
            dao.insertAlarm(alarm)
        }
    }


    fun update(alarm: Alarm){
        viewModelScope.launch {
            dao.updateAlarm(alarm)
        }
    }
}