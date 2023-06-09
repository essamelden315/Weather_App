package com.example.weather.favorite.view

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weather.databinding.HourLayoutBinding
import com.example.weather.model.Current
import com.example.weather.utilities.ChangeIcons
import com.example.weather.utilities.FacilitateWork
import com.example.weather.utilities.TimeConverter
import java.text.SimpleDateFormat
import java.util.*

class DetailsHoursAdapter (private val hours: List<Current>) : RecyclerView.Adapter<DetailsHoursAdapter.MyViewHolder>() {
    lateinit var context: Context
    lateinit var hourBinding: HourLayoutBinding
    private lateinit var sharedPref: SharedPreferences
    private lateinit var temp:String
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater: LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        hourBinding = HourLayoutBinding.inflate(inflater, parent, false)
        context = parent.context
        sharedPref = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        temp = sharedPref.getString("temp","kel").toString()
        return MyViewHolder(hourBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var hour = hours[position]
        val sign = FacilitateWork.getTempUnitAndSign(temp,context as FragmentActivity).second
        holder.viewBinding.hourImage.setImageResource(ChangeIcons.newIcon((hour.weather.get(0).icon).toString()))
        holder.viewBinding.hourDate.text = TimeConverter.convertToDayHours(hour.dt)
        holder.viewBinding.hourDegree.text = "${hour.temp.toInt()}${sign}"
    }

    override fun getItemCount(): Int {
        return 24
    }


    class MyViewHolder(val viewBinding: HourLayoutBinding) : RecyclerView.ViewHolder(viewBinding.root)
}