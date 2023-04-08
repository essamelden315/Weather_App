package com.example.weather.alert.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.databinding.AlertLayoutBinding
import com.example.weather.model.AlertData
import java.util.*


class AlertAdapter (private var alertList: List<AlertData>,var onClick: onClickLinsterInterface): RecyclerView.Adapter<AlertAdapter.myViewHolder>() {
    lateinit var context: Context
    lateinit var alertBinding: AlertLayoutBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val inflater: LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        alertBinding = AlertLayoutBinding.inflate(inflater,parent,false)
        context=parent.context
        return myViewHolder(alertBinding)
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        var alertItem = alertList[position]
        val endTime = alertItem.milleTimeTo + (alertItem.milleTimeTo - alertItem.milleTimeFrom)
            if(Calendar.getInstance().timeInMillis > endTime)
                onClick.cancleAlarm(alertItem)
            holder.viewBinding.fromTimeCard.text = alertItem.fromTime
            holder.viewBinding.fromDateCard.text = alertItem.fromDate
            holder.viewBinding.toTimeCard.text = alertItem.toTime
            holder.viewBinding.toDateCard.text = alertItem.toDate
            holder.viewBinding.alertCardDeleteBtn.setOnClickListener{
                onClick.cancleAlarm(alertItem)
                notifyDataSetChanged()
            }

    }

    override fun getItemCount(): Int {
        return alertList.size
    }
    fun setList(List: List<AlertData>){
        alertList = List
        notifyDataSetChanged()
    }

    class myViewHolder(val viewBinding: AlertLayoutBinding): RecyclerView.ViewHolder(viewBinding.root)
}
