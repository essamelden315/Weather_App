package com.example.weather.alert.view

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import com.example.weather.MainActivity
import com.example.weather.R
import com.example.weather.databinding.FragmentAlertBinding
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.Calendar


class Alert : Fragment() {
    private val CHANEL = "CHANNEL_ID"
    lateinit var binding: FragmentAlertBinding
    lateinit var picker : MaterialTimePicker
    lateinit var calendar: Calendar
    lateinit var alarmManager: AlarmManager
    lateinit var pendingIntent: PendingIntent
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.setTitle(R.string.menu_alerts)
        createNotificationChannel()
        setAlarm()
        binding = FragmentAlertBinding.inflate(inflater,container,false)
        binding.alertFAB.setOnClickListener{
            showTimePacker()
        }
        return binding.root
    //binding.lottieLayerName.visibility = View.GONE
    }

    private fun setAlarm() {
        alarmManager = activity?.getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(),AlarmReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(requireContext(),0,intent,0)
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,1680650460000,pendingIntent
        )
    }

    private fun showTimePacker() {
        picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Select Alarm Time")
            .build()
        picker.show(activity?.supportFragmentManager!!,CHANEL)
        picker.addOnPositiveButtonClickListener{
            Log.i("alertEssam", ""+picker.hour)
            calendar = Calendar.getInstance()
            calendar[Calendar.HOUR_OF_DAY] = picker.hour
            calendar[Calendar.MINUTE] = picker.minute
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0
        }
    }

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "channel display name"
            val description= "Channel from alarm manager"
            var importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANEL, name, importance)
            channel.description = description
            val notificationManager = activity?.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }

}