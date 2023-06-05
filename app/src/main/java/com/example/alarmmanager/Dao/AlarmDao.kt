package com.example.alarmmanager.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.alarmmanager.model.Alarm

@Dao
interface AlarmDao {

    @Insert
    suspend fun insertAlarm(alarm: Alarm)

    @Update
    suspend fun updateAlarm(alarm: Alarm)

    @Query("SELECT * FROM alarm_database")
    fun getAll() : LiveData<List<Alarm>>


}