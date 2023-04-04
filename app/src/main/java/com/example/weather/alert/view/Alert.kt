package com.example.weather.alert.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import com.example.weather.MainActivity
import com.example.weather.R
import com.example.weather.databinding.FragmentAlertBinding



class Alert : Fragment() {
    private val CHANEL = "CHANNEL_ID"
    lateinit var binding: FragmentAlertBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.setTitle(R.string.menu_alerts)
        createNotificationChannel()
        binding = FragmentAlertBinding.inflate(inflater,container,false)
        return binding.root
    //binding.lottieLayerName.visibility = View.GONE
    }

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "channel display name"
            val description= "a developer"
            var importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANEL, name, importance)
            channel.description = description
            val notificationManager = activity?.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
            buildNotification(notificationManager)
        }
    }


    private fun buildNotification(notificationManager: NotificationManager?){

       val intent2 = Intent(requireActivity(), MainActivity::class.java)
        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
       val pendingIntent = TaskStackBuilder.create(requireContext()).run {
           addNextIntentWithParentStack(intent2)
           getPendingIntent(1,PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
           )
       }
        val builder = NotificationCompat.Builder(requireContext(), CHANEL)
            .setSmallIcon(R.drawable.alert_011)
            .setContentTitle("Weather")
            .setContentText("there is no danger")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        notificationManager?.notify(1, builder.build())
    }

}