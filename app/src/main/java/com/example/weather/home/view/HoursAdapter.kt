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
import com.example.weather.model.Current
import java.text.SimpleDateFormat
import java.util.*
import java.util.zip.Inflater


class HoursAdapter(private val hours:List<Current>):RecyclerView.Adapter<HoursAdapter.MyViewHolder>(){
lateinit var context:Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hour_layout,parent,false)
        context=parent.context
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var hour = hours[position]
        val url = "https://openweathermap.org/img/wn/${hour.weather.get(0).icon}@2x.png"
        Glide.with(context).load(url).into(holder.icon)
        holder.time.text=convertToDay(hour.dt)
        holder.degree.text = hour.temp.toInt().toString()+Constants.cel
    }

    override fun getItemCount(): Int {
        return 24
    }
    class MyViewHolder(val itemViw: View) : RecyclerView.ViewHolder(itemViw) {
        var time:TextView = this.itemViw.findViewById(R.id.hour_Date)
        var icon : ImageView = this.itemViw.findViewById(R.id.hour_Image)
        var degree : TextView = this.itemViw.findViewById(R.id.hour_Degree)
    }
    private fun convertToDay(date: Long): String {
        var date=Date(date*1000L)
        var sdf= SimpleDateFormat("hh:mm a")
        sdf.timeZone=TimeZone.getDefault()
        var formatedData=sdf.format(date)
        return formatedData.toString()
    }}