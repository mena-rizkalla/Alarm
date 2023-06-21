package com.example.alarmmanager.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.icu.util.Calendar
import android.os.Build
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.alarmmanager.MainActivity
import com.example.alarmmanager.R
import com.example.alarmmanager.databinding.AlarmItemBinding
import com.example.alarmmanager.model.Alarm

class AlarmAdapter(private val context : Context, private val alarms : List<Alarm>) : RecyclerView.Adapter<AlarmAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AlarmItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val handler = Handler()
        var runnable: Runnable? = null
        val alarm = alarms[position]
        holder.hour.text = "${alarm.hour} : ${alarm.minute} ${alarm.state}"
        if (alarm.checked) {
            holder.switch.isChecked = true
            holder.differenceTime.visibility = View.VISIBLE
            // Create a runnable
            // Create a runnable
             runnable = object : Runnable {
                override fun run() {
                    // Update the TextView with the current time difference
                    holder.differenceTime.text = getDifferenceTime(alarm)

                    // Post the runnable again with a one minute delay
                    handler.postDelayed(this, 60000)
                }
            }
            // Start the runnable with an initial delay
            // Start the runnable with an initial delay
            handler.postDelayed(runnable, 0)
        }else{
            holder.switch.isChecked = false

        }
        holder.hour.setOnClickListener {
            if (context is MainActivity){
                context.showTimePicker(alarm)
            }
        }

        holder.switch.setOnCheckedChangeListener { _, isChecked ->
            if (context is MainActivity){
                if (isChecked){
                    context.setAlarm(alarm)
                    holder.differenceTime.visibility = View.VISIBLE
                    holder.differenceTime.text = getDifferenceTime(alarm)
                }else{
                    context.cancelAlarm(alarm)
                    holder.differenceTime.visibility = View.INVISIBLE
                    if (runnable != null) {
                        handler.removeCallbacks(runnable)
                    }
                }
            }

        }


        if (position % 3 == 0){
            holder.alarmCardView.setCardBackgroundColor(ContextCompat.getColor(context,R.color.firstColor))
        }else if (position % 3 == 1){
            holder.alarmCardView.setCardBackgroundColor(ContextCompat.getColor(context,R.color.secondColor))
        }else if (position % 3 == 2){
            holder.alarmCardView.setCardBackgroundColor(ContextCompat.getColor(context,R.color.thirdColor))
        }else{
            holder.alarmCardView.setCardBackgroundColor(ContextCompat.getColor(context,R.color.fourthColor))
        }

    }

    override fun getItemCount(): Int {
        return alarms.size
    }



    class ViewHolder(binding: AlarmItemBinding) : RecyclerView.ViewHolder(binding.root){
        val hour = binding.selectedTime
        val switch = binding.alarmSwitch
        val differenceTime = binding.differenceTime
        val alarmCardView = binding.alarmCardView

    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getDifferenceTime(alarm: Alarm): String {
        val timeInHour : Int = if(alarm.state == "PM"){
            alarm.hour + 12
        }else{
            alarm.hour

        }
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        var hourDifference = timeInHour - currentHour
        if (hourDifference < 0) {
            hourDifference += 24
        }

        val currentMinute = Calendar.getInstance().get(Calendar.MINUTE)
        var minuteDifference = alarm.minute - currentMinute
        if (minuteDifference < 0) {
            minuteDifference += 60
            hourDifference -=1
        }
        if (hourDifference < 0) {
            hourDifference += 24
        }
        return "Ring in $hourDifference h $minuteDifference minutes"
    }



}