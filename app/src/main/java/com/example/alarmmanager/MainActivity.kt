package com.example.alarmmanager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.alarmmanager.databinding.ActivityMainBinding
import com.example.alarmmanager.service.AlarmReceiver
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var picker : MaterialTimePicker
    private lateinit var calendar : Calendar
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.selectedTime.setOnClickListener {
            showTimePicker()
        }

        binding.alarmSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                setAlarm()
            }else{
                cancelAlarm()
            }
        }

    }

    private fun cancelAlarm() {
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this , AlarmReceiver::class.java)

        pendingIntent = PendingIntent.getBroadcast(this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE)

        alarmManager.cancel(pendingIntent)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setAlarm() {
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this , AlarmReceiver::class.java)

        pendingIntent = PendingIntent.getBroadcast(this,
            2342,
            intent,
            PendingIntent.FLAG_IMMUTABLE)

        alarmManager.set(
            AlarmManager.RTC_WAKEUP,calendar.timeInMillis,
            pendingIntent
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.startForegroundService(intent)
        } else {
            this.startService(intent)
        }

        Toast.makeText(this,"done",Toast.LENGTH_SHORT).show()

        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val currentMinute = Calendar.getInstance().get(Calendar.MINUTE)
        var hourDifference = picker.hour - currentHour
        if (hourDifference < 0) {
            hourDifference += 24
        }
        var minuteDifference = picker.minute - currentMinute
        if (minuteDifference < 0) {
            minuteDifference += 60
        }
        val timeDifference = "Ring in ${hourDifference} h $minuteDifference minutes"
        Toast.makeText(this,timeDifference,Toast.LENGTH_SHORT).show()


    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun showTimePicker() {
        picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Select Alarm Time").build()

        picker.show(supportFragmentManager,"AlarmManager")

        picker.addOnPositiveButtonClickListener{
            if (picker.hour > 12){

                binding.selectedTime.text =
                    String.format("%02d",picker.hour - 12) + ":" + String.format("%02d" , picker.minute) + "PM"

            }else{
                binding.selectedTime.text =
                    String.format("%02d",picker.hour ) + ":" + String.format("%02d" , picker.minute) + "AM"
            }

            calendar = Calendar.getInstance()
            calendar[Calendar.HOUR_OF_DAY] = picker.hour
            calendar[Calendar.MINUTE] = picker.minute
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0

        }
    }
}