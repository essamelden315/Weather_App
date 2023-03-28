package com.example.weather.favorite.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weather.Utils.Constants
import com.example.weather.databinding.HourLayoutBinding
import com.example.weather.model.Current
import java.text.SimpleDateFormat
import java.util.*

class DetailsHoursAdapter (private val hours: List<Current>) : RecyclerView.Adapter<DetailsHoursAdapter.MyViewHolder>() {
    lateinit var context: Context
    lateinit var hourBinding: HourLayoutBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater: LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
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