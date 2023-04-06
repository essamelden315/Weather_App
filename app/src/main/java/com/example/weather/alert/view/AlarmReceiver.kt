package com.example.weather.alert.view

import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.window.SplashScreen
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.weather.MainActivity
import com.example.weather.R
import com.example.weather.database.ConcreteLocalSource
import com.example.weather.database.LocalDataSource
import com.example.weather.model.MyResponse
import com.example.weather.model.Repository
import com.example.weather.network.RemoteSource
import com.example.weather.network.WeatherClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmReceiver:BroadcastReceiver() {

    private val CHANEL = "CHANNEL_ID"
    private lateinit var  repo:Repository
    private lateinit var result:MyResponse

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onReceive(context: Context?, intent: Intent?) {
        var msg="Every thing is okay"
        repo =  Repository.getInstance(
            WeatherClient.getInstance() as RemoteSource,
            ConcreteLocalSource.getInstance(context!!) as LocalDataSource
        ) as Repository

        CoroutineScope(Dispatchers.IO).launch {
           result= repo.getRetrofitWeatherData(31.0,31.0,"minutely","en","metric") as MyResponse
            if(!result.alerts.isNullOrEmpty())
                msg = (result.alerts.get(0).description).toString()
            val intent2 = Intent(context, MainActivity::class.java)
            intent?.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            val pendingIntent = PendingIntent.getActivity(context, 0,intent2,0)
            val builder = NotificationCompat.Builder(context!!, CHANEL)
                .setSmallIcon(R.drawable.alert_011)
                .setContentTitle(result.timezone)
                .setContentText(msg)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
            val notificationManager = NotificationManagerCompat.from(context)

            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {}
            notificationManager.notify(1, builder.build())
        }

    }

}
