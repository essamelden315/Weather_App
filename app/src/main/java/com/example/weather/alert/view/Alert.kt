package com.example.weather.alert.view

import android.app.*
import android.app.PendingIntent.getBroadcast
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather.R
import com.example.weather.alert.viewmodel.AlertViewModel
import com.example.weather.alert.viewmodel.AlertViewModelFactory
import com.example.weather.database.ConcreteLocalSource
import com.example.weather.database.LocalDataSource
import com.example.weather.databinding.AlertDialogBinding
import com.example.weather.databinding.FragmentAlertBinding
import com.example.weather.model.AlertData
import com.example.weather.model.Repository
import com.example.weather.network.NetworkListener
import com.example.weather.network.RemoteSource
import com.example.weather.network.WeatherClient
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class Alert : Fragment(),onClickLinsterInterface {
    private lateinit var CHANEL:String
    lateinit var binding: FragmentAlertBinding
    lateinit var alarmManager: AlarmManager
    lateinit var pendingIntent: PendingIntent
    lateinit var alertFactory: AlertViewModelFactory
    lateinit var viewModel: AlertViewModel
    lateinit var myAdapter: AlertAdapter
    lateinit var manager: LinearLayoutManager
    lateinit var sharedPreferences:SharedPreferences
    lateinit var latLonSP:SharedPreferences
    lateinit var edit:SharedPreferences.Editor
    lateinit var choise:String
    lateinit var lat:String
    lateinit var lon:String
    var requestCode:Long =0
    var cashDate1: String? = null
    var cashDate2: String? = null
    var cashTime1: String? = null
    var cashTime2: String? = null
    var cashCalenderFromTime: Long = 0
    var cashCalenderFromDate: Long = 0
    var cashCalenderToTime: Long = 0
    var cashCalenderToDate: Long = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.setTitle(R.string.menu_alerts)
        binding = FragmentAlertBinding.inflate(inflater, container, false)
        sharedPreferences = context?.getSharedPreferences("choise", Context.MODE_PRIVATE) as SharedPreferences
        edit = sharedPreferences.edit()
        choise = sharedPreferences.getString("what","notification") as String
        //
        latLonSP = context?.getSharedPreferences("mapData", Context.MODE_PRIVATE) as SharedPreferences
         lat = latLonSP.getString("lat","${0.0}").toString()
         lon = latLonSP.getString("lon","${0.0}").toString()
        alertFactory = AlertViewModelFactory(
            Repository.getInstance(
                WeatherClient.getInstance() as RemoteSource,
                ConcreteLocalSource.getInstance(requireContext()) as LocalDataSource
            ) as Repository
        )
        viewModel = ViewModelProvider(requireActivity(), alertFactory).get(AlertViewModel::class.java)
        myAdapter= AlertAdapter(listOf(),this)
        manager = LinearLayoutManager(context)
        binding.swipeRefresh.setOnRefreshListener {
            observeAtData()
            binding.swipeRefresh.isRefreshing =false
        }
        observeAtData()
        binding.alertRV.adapter = myAdapter
        binding.alertRV.layoutManager = manager
        binding.alertFAB.setOnClickListener {
            if (NetworkListener.getConnectivity(requireContext())) {
                if (!Settings.canDrawOverlays(requireContext())){
                    requestOverAppPermission()
                }
                showDialog()
            } else
                Snackbar.make(
                    binding.alertFAB,
                    "There is no internet connection",
                    Snackbar.LENGTH_LONG
                ).show()
        }
        return binding.root
    }
    fun observeAtData(){
        viewModel.alertData.observe(viewLifecycleOwner) { data ->
            if (data.isNotEmpty()) {
                binding.lottieLayerName.visibility = View.GONE
            }else
                binding.lottieLayerName.visibility = View.VISIBLE
            myAdapter.setList(data)
        }

    }
    //show dialog
    fun showDialog() {
        val dialogBinding = AlertDialogBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())
        dialog.setContentView(dialogBinding.root)
        dialogBinding.fromBtn.setOnClickListener {
            pickTime(dialogBinding, 1)
            pickDate(dialogBinding, 1)
        }
        dialogBinding.toBtn.setOnClickListener {
            pickTime(dialogBinding, 2)
            pickDate(dialogBinding, 2)
        }
        dialogBinding.timeCalenderRadioGroup.setOnCheckedChangeListener{group , checkedId ->
            if(group.checkedRadioButtonId == R.id.notificationRBtn) {
                edit.putString("what","notification")
                edit.commit()
            }
            else if(group.checkedRadioButtonId==R.id.alarmRBtn) {
                edit.putString("what","alarm")
                edit.commit()
            }
        }

        dialogBinding.OkBtn.setOnClickListener {
            if (cashTime1 != null && cashDate1 != null &&
                cashTime2 != null && cashDate2 != null &&
                (dialogBinding.notificationRBtn.isChecked ||
                dialogBinding.alarmRBtn.isChecked) &&
                trigerTime(cashCalenderToDate,cashCalenderToTime).timeInMillis >
                trigerTime(cashCalenderFromDate,cashCalenderFromTime).timeInMillis

            ) {
                dialog.dismiss()
                requestCode= (Calendar.getInstance().timeInMillis)-100
                CHANEL = requestCode.toString()
                edit.putString("channel",CHANEL)
                edit.commit()
                viewModel.insertIntoAlert(
                    AlertData(
                        cashTime1!!,
                        cashDate1!!,
                        cashTime2!!,
                        cashDate2!!,
                        cashCalenderFromTime,
                        cashCalenderFromDate,
                        cashCalenderToTime,
                        cashCalenderToDate,
                        requestCode
                    )
                )

                setAlarm(sharedPreferences.getString("what","notification").toString())
                cashDate1 = null
                cashDate2 = null
                cashTime1 = null
                cashTime2 = null
            } else
                Toast.makeText(
                    requireContext(),
                    "not allowed to let any filed empty or to set the end date before start date",
                    Toast.LENGTH_LONG
                ).show()
        }
        dialog.show()
    }

    // picker time and picker date
    private fun pickTime(dialogBinding: AlertDialogBinding, choose: Int) {
        val calendarTime = Calendar.getInstance()
        val timePicker = TimePickerDialog.OnTimeSetListener { timePicker, hourOfDay, minute ->
            calendarTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendarTime.set(Calendar.MINUTE, minute)
            calendarTime.timeZone = TimeZone.getDefault()
            updateTimeLabel(calendarTime, dialogBinding, choose)
        }
        TimePickerDialog(
            requireContext(),
            timePicker,
            calendarTime.get(Calendar.HOUR_OF_DAY),
            calendarTime.get(Calendar.MINUTE),
            false
        ).show()
    }
    private fun pickDate(dialogBinding: AlertDialogBinding, choose: Int){
        val calenderDate = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            calenderDate.set(Calendar.YEAR, year)
            calenderDate.set(Calendar.MONTH, month)
            calenderDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateLabel(calenderDate, dialogBinding, choose)
        }
        DatePickerDialog(
            requireContext(),
            datePicker,
            calenderDate.get(Calendar.YEAR),
            calenderDate.get(Calendar.MONTH),
            calenderDate.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
    //update ui
    private fun updateTimeLabel(
        calendarTime: Calendar,
        dialogBinding: AlertDialogBinding,
        choose: Int
    ) {
        val format = SimpleDateFormat("hh:mm:aa")
        val time = format.format(calendarTime.time)
        when (choose) {
            1 -> {
                dialogBinding.fromTime.text = time
                cashTime1 = time
                cashCalenderFromTime = calendarTime.timeInMillis
            }
            2 -> {
                dialogBinding.toTime.text = time
                cashTime2 = time
                cashCalenderToTime = calendarTime.timeInMillis
            }

        }
    }

    private fun updateDateLabel(
        calenderDate: Calendar,
        dialogBinding: AlertDialogBinding,
        choose: Int
    ) {
        val day = SimpleDateFormat("dd").format(calenderDate.time)
        val month = SimpleDateFormat("MM").format(calenderDate.time)
        val year = SimpleDateFormat("yyyy").format(calenderDate.time)
        when (choose) {
            1 -> {
                dialogBinding.fromCalender.text = "${day}/${month}/${year}"
                cashDate1 = "${day}/${month}/${year}"
                cashCalenderFromDate = calenderDate.timeInMillis
            }
            2 -> {
                dialogBinding.toCalender.text = "${day}/${month}/${year}"
                cashDate2 = "${day}/${month}/${year}"
                cashCalenderToDate = calenderDate.timeInMillis
            }
        }
    }


    // set Alarm
    private fun setAlarm(what:String) {
        var noOfDays = cashCalenderToDate - cashCalenderFromDate
        val days = TimeUnit.MILLISECONDS.toDays(noOfDays)
        val dayInMilliSecond = 24 * 60 * 60 * 1000
        alarmManager = activity?.getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), AlarmReceiver::class.java)
        intent.putExtra("ess",CHANEL)
        intent.putExtra("what",what)
        intent.putExtra("lat",lat)
        intent.putExtra("lon",lon)

        for (i in 0..days) {
            createNotificationChannel()
            pendingIntent = getBroadcast(requireContext(), (requestCode+i).toInt(), intent, 0)
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP, trigerTime(cashCalenderFromDate,cashCalenderFromTime).timeInMillis + (i * dayInMilliSecond), pendingIntent
            )
        }
    }
    fun trigerTime(Date:Long,Time:Long): Calendar{
        var testCanlender = Calendar.getInstance()
        testCanlender.timeInMillis = Date
        val trigerCalender = Calendar.getInstance()
        trigerCalender.set(Calendar.DAY_OF_MONTH,testCanlender.get(Calendar.DAY_OF_MONTH))
        trigerCalender.set(Calendar.MONTH,testCanlender.get(Calendar.MONTH))
        trigerCalender.set(Calendar.YEAR,testCanlender.get(Calendar.YEAR))
        testCanlender.timeInMillis = Time
        trigerCalender.set(Calendar.HOUR_OF_DAY,testCanlender.get(Calendar.HOUR_OF_DAY))
        trigerCalender.set(Calendar.MINUTE,testCanlender.get(Calendar.MINUTE))
        return trigerCalender
    }
    //create notification
    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "channel display name"
            val description = "Channel from alarm manager"
            var importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANEL, name, importance)
            channel.description = description
            val notificationManager = activity?.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }

     override fun cancleAlarm(alertData: AlertData) {
        var noOfDays = alertData.milleDateTo - alertData.milleDateFrom
        val days = TimeUnit.MILLISECONDS.toDays(noOfDays)
        alarmManager = activity?.getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(activity, AlarmReceiver::class.java)

        for (i in 0..days) {
            pendingIntent = getBroadcast(context, (alertData.requestCode+i).toInt(), intent, 0)
            alarmManager.cancel(pendingIntent)
        }
        delete(alertData)
    }

    fun delete(alertData: AlertData) {
        viewModel.deleteFromAlert(alertData)

    }
    private fun requestOverAppPermission() {
        startActivityForResult(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION),20)
    }
}