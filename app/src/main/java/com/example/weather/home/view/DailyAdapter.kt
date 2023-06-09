package com.example.weather.home.view

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weather.databinding.DailyLayoutBinding
import com.example.weather.model.Daily
import com.example.weather.utilities.ChangeIcons
import com.example.weather.utilities.FacilitateWork
import com.example.weather.utilities.TimeConverter
import java.text.SimpleDateFormat
import java.util.*

class DailyAdapter(private val days:List<Daily>):RecyclerView.Adapter<DailyAdapter.MyViewHolder>() {
    lateinit var context: Context
    lateinit var dailyBinding:DailyLayoutBinding
    private lateinit var sharedPref: SharedPreferences
    private lateinit var temp:String
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater:LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        dailyBinding = DailyLayoutBinding.inflate(inflater,parent,false)
        context=parent.context
        sharedPref = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        temp = sharedPref.getString("temp","kel").toString()
        return MyViewHolder(dailyBinding)
    }

    override fun getItemCount(): Int {
        return days.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var day = days[position]
        val sign = FacilitateWork.getTempUnitAndSign(temp,context as FragmentActivity).second
        holder.viewBinding.dayImg.setImageResource(ChangeIcons.newIcon((day.weather.get(0).icon).toString()))
        holder.viewBinding.dayDate.text = TimeConverter.convertToWeekDays(day.dt)
        holder.viewBinding.dayDescription.text = day.weather.get(0).description
        holder.viewBinding.dayMinDegree.text = "${day.temp.min.toInt()}${sign}"
        holder.viewBinding.dayMaxDegree.text = "${day.temp.max.toInt()}${sign}"
    }

    class MyViewHolder(val viewBinding: DailyLayoutBinding):RecyclerView.ViewHolder(viewBinding.root)
}