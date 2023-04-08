package com.example.weather.alert.view

import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.PixelFormat
import android.media.MediaPlayer
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.window.SplashScreen
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.weather.MainActivity
import com.example.weather.R
import com.example.weather.database.ConcreteLocalSource
import com.example.weather.database.LocalDataSource
import com.example.weather.model.AlertData
import com.example.weather.model.MyResponse
import com.example.weather.model.Repository
import com.example.weather.network.RemoteSource
import com.example.weather.network.WeatherClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlarmReceiver:BroadcastReceiver(){

    private val CHANEL = "CHANNEL_ID"
    private lateinit var  repo:Repository
    private lateinit var result:MyResponse
    private lateinit var sharedPreferences: SharedPreferences
    var c:Int?= 0
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onReceive(context: Context?, intent: Intent?) {

        c=intent?.getIntExtra("d",-1)
        Log.i("aaaaaa", "onReceive: $c")
        sharedPreferences = context?.getSharedPreferences("mapData", Context.MODE_PRIVATE) as SharedPreferences
        val lat = sharedPreferences.getString("lat","${0.0}")?.toDouble() as Double
        val lon = sharedPreferences.getString("lon","${0.0}")?.toDouble() as Double
        var msg="Every thing is okay"
        repo =  Repository.getInstance(
            WeatherClient.getInstance() as RemoteSource,
            ConcreteLocalSource.getInstance(context!!) as LocalDataSource
        ) as Repository

        CoroutineScope(Dispatchers.IO).launch {
           result= repo.getRetrofitWeatherData(lat,lon,"minutely","en","metric") as MyResponse
            if(!result.alerts.isNullOrEmpty())
                msg = (result.alerts.get(0).description).toString()
            val intent2 = Intent(context, MainActivity::class.java)
            intent?.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            val pendingIntent = PendingIntent.getActivity(context, 0,intent2,0)
            if (c == 1){
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
            else{
                alarm(context,msg)
            }

        }

    }
  private suspend fun alarm(context: Context,msg:String) {
      val LAYOUT_FLAG = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
          WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
      else
          WindowManager.LayoutParams.TYPE_PHONE

      val mediaPlayer = MediaPlayer.create(context, Settings.System.DEFAULT_ALARM_ALERT_URI)

      val view = LayoutInflater.from(context).inflate(R.layout.alarm_window, null, false)
      val message = view.findViewById<TextView>(R.id.message)
      val dismissBtn = view.findViewById<Button>(R.id.dismissBtn)

        val layoutParams =
            WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )

        layoutParams.gravity = Gravity.TOP

        val windowManager = context.getSystemService(WINDOW_SERVICE) as WindowManager

        withContext(Dispatchers.Main) {
            windowManager.addView(view, layoutParams)
            view.visibility = View.VISIBLE
             message.text = msg
        }
        mediaPlayer.start()
        mediaPlayer.isLooping = true
         dismissBtn.setOnClickListener {
             mediaPlayer?.release()
             windowManager.removeView(view)
         }
    }
}
