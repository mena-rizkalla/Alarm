package com.example.alarmmanager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.alarmmanager.Dao.AlarmDao

class AlarmFactory(private val alarmDao : AlarmDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java))
            return MainViewModel(alarmDao) as T

        throw IllegalArgumentException("Unknown view model")
    }
}