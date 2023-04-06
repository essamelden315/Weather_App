package com.example.weather.alert.view

import android.app.*
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.weather.R
import com.example.weather.databinding.FragmentAlertBinding
import com.example.weather.databinding.TimeCalenderDialogBinding
import com.example.weather.network.NetworkListener
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*


class Alert : Fragment() {
    private val CHANEL = "CHANNEL_ID"
    lateinit var binding: FragmentAlertBinding
    lateinit var alarmManager: AlarmManager
    lateinit var pendingIntent: PendingIntent
    lateinit var calendarTime: Calendar
    lateinit var calenderDate: Calendar
    lateinit var cashCalenderTime: Calendar
    var cashDate1: String? = null
    var cashDate2: String? = null
    var cashTime1: String? = null
    var cashTime2: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.setTitle(R.string.menu_alerts)
        binding = FragmentAlertBinding.inflate(inflater, container, false)
        binding.alertFAB.setOnClickListener {
            if (NetworkListener.getConnectivity(requireContext())) {
                showDialog()
                binding.lottieLayerName.visibility = View.GONE
            } else
                Snackbar.make(
                    binding.alertFAB,
                    "There is no internet connection",
                    Snackbar.LENGTH_LONG
                ).show()

        }

        return binding.root
    }

    fun showDialog() {
        val dialogBinding = TimeCalenderDialogBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())
        dialog.setContentView(dialogBinding.root)

        dialogBinding.fromBtn.setOnClickListener {
            pick(dialogBinding, 1)
        }


        dialogBinding.toBtn.setOnClickListener {
            pick(dialogBinding, 2)
        }
        dialogBinding.OkBtn.setOnClickListener {
            if (cashTime1 != null && cashDate1 != null &&
                cashTime2 != null && cashDate2 != null
            ) {
                dialog.dismiss()
                Toast.makeText(
                    requireContext(),
                    "time from $cashTime1 to $cashDate1 \n " +
                            "date from $cashTime2 to $cashDate2",
                    Toast.LENGTH_LONG
                ).show()
                setAlarm()
                cashDate1 = null
                cashDate2 = null
                cashTime1 = null
                cashTime2 = null
            } else
                Toast.makeText(
                    requireContext(),
                    "not allowed to let any filed empty",
                    Toast.LENGTH_LONG
                ).show()
        }
        dialog.show()
    }

    private fun pick(dialogBinding: TimeCalenderDialogBinding, choose: Int) {
        calendarTime = Calendar.getInstance()
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
        calenderDate = Calendar.getInstance()
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

    private fun setAlarm() {
        createNotificationChannel()
        alarmManager = activity?.getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), AlarmReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, intent, 0)
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP, cashCalenderTime.timeInMillis, pendingIntent
        )

    }

    private fun updateTimeLabel(
        calendarTime: Calendar,
        dialogBinding: TimeCalenderDialogBinding,
        choose: Int
    ) {
        val format = SimpleDateFormat("hh:mm:aa")
        val time = format.format(calendarTime.time)
        when (choose) {
            1 -> {
                dialogBinding.fromTime.text = time
                cashTime1 = time
                cashCalenderTime = calendarTime
            }
            2 -> {
                dialogBinding.toTime.text = time
                cashTime2 = time
            }

        }
    }

    private fun updateDateLabel(
        calenderDate: Calendar,
        dialogBinding: TimeCalenderDialogBinding,
        choose: Int
    ) {
        val day = SimpleDateFormat("dd").format(calenderDate.time)
        val month = SimpleDateFormat("MM").format(calenderDate.time)
        val year = SimpleDateFormat("yyyy").format(calenderDate.time)
        when (choose) {
            1 -> {
                dialogBinding.fromCalender.text = "${day}/${month}/${year}"
                cashDate1 = "${day}/${month}/${year}"
            }
            2 -> {
                dialogBinding.toCalender.text = "${day}/${month}/${year}"
                cashDate2 = "${day}/${month}/${year}"
            }
        }
    }


}