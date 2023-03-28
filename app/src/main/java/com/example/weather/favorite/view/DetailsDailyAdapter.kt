package com.example.weather.favorite.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weather.Utils.Constants
import com.example.weather.databinding.DailyLayoutBinding
import com.example.weather.home.view.DailyAdapter
import com.example.weather.model.Daily
import java.text.SimpleDateFormat
import java.util.*

class DetailsDailyAdapter (private val days:List<Daily>): RecyclerView.Adapter<DetailsDailyAdapter.MyViewHolder>() {
    lateinit var context: Context
    lateinit var dailyBinding: DailyLayoutBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater: LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        dailyBinding = DailyLayoutBinding.inflate(inflater,parent,false)
        context=parent.context
        return MyViewHolder(dailyBinding)
    }

    override fun getItemCount(): Int {
        return days.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var day = days[position]
        val url = "https://openweathermap.org/img/wn/${day.weather.get(0).icon}@2x.png"
        Glide.with(context).load(url).into(holder.viewBinding.dayImg)
        holder.viewBinding.dayDate.text = convertToDay(day.dt)
        holder.viewBinding.dayDescription.text = day.weather.get(0).description
        holder.viewBinding.dayMinDegree.text = "${day.temp.min.toInt()}${Constants.cel}"
        holder.viewBinding.dayMaxDegree.text = "${day.temp.max.toInt()}${Constants.cel}"
    }
    private fun convertToDay(date: Long): String {
        var simpleDate = SimpleDateFormat("dd-MM-yyyy")
        var currentDate = simpleDate.format(date.times(1000))
        var date: Date = simpleDate.parse(currentDate)
        Log.i("TAG", currentDate)
        var outFormat = SimpleDateFormat("EEEE")
        var goal = outFormat.format(date)
        return goal.toString()
    }
    class MyViewHolder(val viewBinding: DailyLayoutBinding): RecyclerView.ViewHolder(viewBinding.root)
}