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
    lateinit var calendarTimeFrom: Calendar
    var showDate: String? = null
    var showTime: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.setTitle(R.string.menu_alerts)
        binding = FragmentAlertBinding.inflate(inflater, container, false)
        binding.alertFAB.setOnClickListener {
            if (NetworkListener.getConnectivity(requireContext())) {
                showDialog()
            } else
                Snackbar.make(
                    binding.alertFAB,
                    "There is no internet connection",
                    Snackbar.LENGTH_LONG
                ).show()
            //binding.lottieLayerName.visibility = View.GONE
        }

        return binding.root
    }

    fun showDialog() {
        val dialogBinding = TimeCalenderDialogBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())
        dialog.setContentView(dialogBinding.root)

        dialogBinding.calenderBtn.setOnClickListener {
            val calenderDate = Calendar.getInstance()
            val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                calenderDate.set(Calendar.YEAR, year)
                calenderDate.set(Calendar.MONTH, month)
                calenderDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateLabel(calenderDate, dialogBinding)
            }
            DatePickerDialog(
                requireContext(),
                datePicker,
                calenderDate.get(Calendar.YEAR),
                calenderDate.get(Calendar.MONTH),
                calenderDate.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        dialogBinding.timeBtn.setOnClickListener {
            calendarTimeFrom = Calendar.getInstance()
            val timePicker = TimePickerDialog.OnTimeSetListener { timePicker, hourOfDay, minute ->
                calendarTimeFrom.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendarTimeFrom.set(Calendar.MINUTE, minute)
                calendarTimeFrom.timeZone = TimeZone.getDefault()
                updateTimeLabel(calendarTimeFrom, dialogBinding)
            }
            TimePickerDialog(
                requireContext(),
                timePicker,
                calendarTimeFrom.get(Calendar.HOUR_OF_DAY),
                calendarTimeFrom.get(Calendar.MINUTE),
                false
            ).show()
        }
        dialogBinding.OKBtn.setOnClickListener {
            if (showDate != null && showTime != null) {
                dialog.dismiss()
                Toast.makeText(
                    requireContext(),
                    "the date is $showDate \n the time is $showTime",
                    Toast.LENGTH_LONG
                ).show()
                setAlarm()
                showTime = null
                showDate = null
            } else
                Toast.makeText(
                    requireContext(),
                    "not allowed to let any filed empty",
                    Toast.LENGTH_LONG
                ).show()
        }
        dialog.show()
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
            AlarmManager.RTC_WAKEUP, calendarTimeFrom.timeInMillis, pendingIntent
        )

    }
    private fun updateTimeLabel(calendarTime: Calendar, dialogBinding: TimeCalenderDialogBinding) {
        val format = SimpleDateFormat("hh:mm:aa")
        val time = format.format(calendarTime.time)
        showTime = time
        dialogBinding.timeText.text = time
    }

    private fun updateDateLabel(calenderDate: Calendar, dialogBinding: TimeCalenderDialogBinding) {
        val day = SimpleDateFormat("dd").format(calenderDate.time)
        val month = SimpleDateFormat("MM").format(calenderDate.time)
        val year = SimpleDateFormat("yyyy").format(calenderDate.time)
        dialogBinding.calenderTxt.text = "${day}/${month}/${year}"
        showDate = "${day}/${month}/${year}"
    }


}