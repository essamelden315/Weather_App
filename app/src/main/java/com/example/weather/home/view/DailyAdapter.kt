package com.example.weather.home.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weather.R
import com.example.weather.Utils.Constants
import com.example.weather.model.Daily
import java.text.SimpleDateFormat
import java.util.*

class DailyAdapter(private val days:List<Daily>):RecyclerView.Adapter<DailyAdapter.MyViewHolder>() {
    lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.daily_layout,parent,false)
        context=parent.context
        return DailyAdapter.MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return days.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var day = days[position]
        val url = "https://openweathermap.org/img/wn/${day.weather.get(0).icon}@2x.png"
        Glide.with(context).load(url).into(holder.icon)
        holder.day.text = convertToDay(day.dt)
        holder.desription.text = day.weather.get(0).description
        holder.min.text = day.temp.min.toInt().toString()+Constants.cel
        holder.max.text = day.temp.max.toInt().toString()+Constants.cel
    }

    class MyViewHolder(val itemView: View):RecyclerView.ViewHolder(itemView) {
        var day:TextView = this.itemView.findViewById(R.id.day_date)
        var icon:ImageView = this.itemView.findViewById(R.id.day_img)
        var desription:TextView = this.itemView.findViewById(R.id.day_description)
        var min:TextView = this.itemView.findViewById(R.id.day_minDegree)
        var max:TextView = this.itemView.findViewById(R.id.day_maxDegree)
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
}