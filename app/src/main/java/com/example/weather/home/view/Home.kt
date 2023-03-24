package com.example.weather.home.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.weather.Utils.Constants
import com.example.weather.databinding.FragmentHomeBinding
import com.example.weather.home.viewmodel.HomeViewModel
import com.example.weather.home.viewmodel.HomeViewModelFactory
import com.example.weather.model.MyResponse
import com.example.weather.model.Repository
import com.example.weather.network.ProductClient
import com.example.weather.network.RemoteSource
import java.text.SimpleDateFormat
import java.util.*


class Home : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var hourAdapter: HoursAdapter
    private lateinit var dayAdapter: DailyAdapter
    lateinit var manger: LinearLayoutManager
    lateinit var manger2: LinearLayoutManager
    lateinit var viewModel: HomeViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        manger = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        manger2 = LinearLayoutManager(context)
        var weatherFactory = HomeViewModelFactory(
            Repository.getInstance(ProductClient.getInstance() as RemoteSource) as Repository
        )
        viewModel = ViewModelProvider(this, weatherFactory).get(HomeViewModel::class.java)
        viewModel.products.observe(viewLifecycleOwner) {
            if (it != null) {
                setHomeScreenData(it)
                setHomeScreenAdapter(it)
            }
        }
        return binding.root
    }
    fun convertDate(time:Long):String{
        var simpleDate = SimpleDateFormat("dd-MM-yyyy")
        var currentDate = simpleDate.format(time.times(1000))
        var date1: Date = simpleDate.parse(currentDate)
        val split = date1.toString().split(" ")
        var myDate= "${split[0]},${split[2]} ${split[1]} "
        return myDate
    }
    private fun setHomeScreenData(it:MyResponse){
        binding.dateTxt.text = convertDate(it.current.dt)
        binding.countryTxt.text = it.timezone
        binding.degeeTxt.text = it.current.temp.toInt().toString()
        binding.degreeType.text = Constants.cel
        binding.weatherStatus.text = it.current.weather.get(0).description
        val url = "https://openweathermap.org/img/wn/${it.current.weather.get(0).icon}@2x.png"
        Glide.with(requireContext()).load(url).into(binding.weatherImage)
        binding.pressureValue.text = "${it.current.pressure} hpa"
        binding.cloudValue.text = "${it.current.clouds}%"
        binding.visibilityValue.text = "${it.current.visibility}m"
        binding.humidityValue.text = "${it.current.humidity}%"
        binding.ultravilotValue.text = it.current.uvi.toString()
        binding.windValue.text = "${it.current.wind_speed} m/s"
    }
    private fun setHomeScreenAdapter(it:MyResponse){
        hourAdapter = HoursAdapter(it.hourly)
        dayAdapter = DailyAdapter(it.daily)
        binding.hoursRV.adapter = hourAdapter
        binding.hoursRV.layoutManager = manger
        binding.daysRV.adapter = dayAdapter
        binding.daysRV.layoutManager = manger2
    }

}