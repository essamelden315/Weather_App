package com.example.weather.home.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weather.R
import com.example.weather.Utils.Constants
import com.example.weather.databinding.HourLayoutBinding
import com.example.weather.model.Current
import java.text.SimpleDateFormat
import java.util.*


class HoursAdapter(private val hours: List<Current>) : RecyclerView.Adapter<HoursAdapter.MyViewHolder>() {
    lateinit var context: Context
    lateinit var hourBinding:HourLayoutBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater:LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        hourBinding = HourLayoutBinding.inflate(inflater, parent, false)
        context = parent.context
        return MyViewHolder(hourBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var hour = hours[position]
        val url = "https://openweathermap.org/img/wn/${hour.weather.get(0).icon}@2x.png"
        Glide.with(context).load(url).into(holder.viewBinding.hourImage)
        holder.viewBinding.hourDate.text = convertToDay(hour.dt)
        holder.viewBinding.hourDegree.text = "${hour.temp.toInt()}${Constants.cel}"
    }

    override fun getItemCount(): Int {
        return 24
    }

    private fun convertToDay(date: Long): String {
        var date = Date(date * 1000L)
        var sdf = SimpleDateFormat("hh:mm a")
        sdf.timeZone = TimeZone.getDefault()
        var formatedData = sdf.format(date)
        return formatedData.toString()
    }
    class MyViewHolder(val viewBinding: HourLayoutBinding) : RecyclerView.ViewHolder(viewBinding.root)
}