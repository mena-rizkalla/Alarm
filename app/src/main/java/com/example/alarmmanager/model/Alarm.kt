package com.example.alarmmanager.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarm_database")
data class Alarm(@PrimaryKey(autoGenerate = true)  var alarmId : Int = 0,
                 @ColumnInfo(name = "Hour")  var hour:Int =0 ,
                 @ColumnInfo(name = "Minute")  var minute : Int =0 ,
                 @ColumnInfo(name = "State")  var state : String ="AM" ,
                 @ColumnInfo(name = "Checked")  var checked : Boolean = false, ){

}