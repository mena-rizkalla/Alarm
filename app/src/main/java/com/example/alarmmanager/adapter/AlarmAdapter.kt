package com.example.alarmmanager.adapter

import android.content.Context
import android.graphics.Color
import android.os.Build
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


     var diffTime : String =""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AlarmItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val alarm = alarms[position]
        holder.hour.text = "${alarm.hour} : ${alarm.minute}"
        holder.switch.isChecked = alarm.checked
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
                    holder.differenceTime.text = diffTime
                }else{
                    context.cancelAlarm()
                    holder.differenceTime.visibility = View.INVISIBLE
                }
            }

        }
        if (position % alarms.size == 0){
            holder.alarmCardView.setCardBackgroundColor(ContextCompat.getColor(context,R.color.firstColor))
        }else if (position % alarms.size == 1){
            holder.alarmCardView.setCardBackgroundColor(ContextCompat.getColor(context,R.color.secondColor))
        }else if (position % alarms.size == 2){
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


}