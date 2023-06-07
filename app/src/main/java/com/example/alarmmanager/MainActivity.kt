package com.example.alarmmanager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.AbstractThreadedSyncAdapter
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alarmmanager.adapter.AlarmAdapter
import com.example.alarmmanager.database.AlarmDatabase
import com.example.alarmmanager.databinding.ActivityMainBinding
import com.example.alarmmanager.model.Alarm
import com.example.alarmmanager.service.AlarmReceiver
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var picker : MaterialTimePicker
    private lateinit var calendar : Calendar
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var viewModel: MainViewModel
    private lateinit var viewModelFactory: AlarmFactory
    private lateinit var adapter: AlarmAdapter

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val application = requireNotNull(this).application
        val dao = AlarmDatabase.getInstance(application).alarmDao
        viewModelFactory = AlarmFactory(dao)
        viewModel = ViewModelProvider(this,viewModelFactory)[MainViewModel::class.java]

        binding.recyclerView.layoutManager = GridLayoutManager(application,2)
        viewModel.alarms.observe(this, Observer {
            it.let{
                adapter = AlarmAdapter(this,it)
                binding.recyclerView.adapter = adapter
            }
        })

    }

     fun cancelAlarm() {
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this , AlarmReceiver::class.java)

        pendingIntent = PendingIntent.getBroadcast(this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE)

        alarmManager.cancel(pendingIntent)
    }

    @RequiresApi(Build.VERSION_CODES.N)
     fun setAlarm(alarm: Alarm? = null) {
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this , AlarmReceiver::class.java)

        pendingIntent = PendingIntent.getBroadcast(this,
            2342,
            intent,
            PendingIntent.FLAG_IMMUTABLE)

        val timeMillis : Long = alarm?.timeInMillis ?: calendar.timeInMillis

        alarmManager.set(
            AlarmManager.RTC_WAKEUP,timeMillis,
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
        var hourDifference = if (alarm != null) alarm.hour - currentHour else picker.hour - currentHour
        if (hourDifference < 0) {
            hourDifference += 24
        }

        var minuteDifference = if (alarm != null) alarm.minute - currentMinute else picker.minute - currentMinute
        if (minuteDifference < 0) {
            minuteDifference += 60
        }
        val timeDifference = "Ring in $hourDifference h $minuteDifference minutes"
        Toast.makeText(this,timeDifference,Toast.LENGTH_SHORT).show()
        adapter.diffTime = timeDifference


    }

    @RequiresApi(Build.VERSION_CODES.N)
      fun showTimePicker(alarm: Alarm? = null) {
        picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Select Alarm Time").build()

        picker.show(supportFragmentManager,"AlarmManager")

        var hour : Int
        var state : String
        picker.addOnPositiveButtonClickListener{
            if (picker.hour > 12){

                hour = picker.hour - 12
                state = "PM"

            }else{
                hour = picker.hour
                state = "AM"

            }

            calendar = Calendar.getInstance()
            calendar[Calendar.HOUR_OF_DAY] = picker.hour
            calendar[Calendar.MINUTE] = picker.minute
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0

            if (alarm != null){
                alarm.hour = hour
                alarm.minute = picker.minute
                alarm.state = state
                alarm.checked = false
                alarm.timeInMillis = calendar.timeInMillis
                viewModel.update(alarm)
            }else {
                val newAlarm = Alarm(hour = hour, minute = picker.minute, state = state, checked = false, timeInMillis = calendar.timeInMillis)
                viewModel.insert(newAlarm)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater;
        inflater.inflate(R.menu.main_menu, menu);
        return true
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.insertBtnMenu -> {
                showTimePicker()

            }
        }
        return super.onOptionsItemSelected(item)
    }
}