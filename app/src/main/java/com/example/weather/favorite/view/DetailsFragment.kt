package com.example.weather.favorite.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.weather.R
import com.example.weather.Utils.Constants
import com.example.weather.database.ConcreteLocalSource
import com.example.weather.database.LocalDataSource
import com.example.weather.databinding.FragmentDetailsBinding
import com.example.weather.databinding.FragmentHomeBinding
import com.example.weather.home.view.DailyAdapter
import com.example.weather.home.view.HoursAdapter
import com.example.weather.home.viewmodel.HomeViewModel
import com.example.weather.home.viewmodel.HomeViewModelFactory
import com.example.weather.model.MyResponse
import com.example.weather.model.Repository
import com.example.weather.network.RemoteSource
import com.example.weather.network.WeatherClient
import java.text.SimpleDateFormat
import java.util.*

class DetailsFragment : Fragment() {
    lateinit var detailsBinding: FragmentDetailsBinding
    val args: DetailsFragmentArgs by navArgs()
    private lateinit var hourAdapter: DetailsHoursAdapter
    private lateinit var dayAdapter: DetailsDailyAdapter
    lateinit var manger: LinearLayoutManager
    lateinit var manger2: LinearLayoutManager
    lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        detailsBinding = FragmentDetailsBinding.inflate(inflater, container, false)
        manger = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        manger2 = LinearLayoutManager(context)
        return detailsBinding.root
    }

    override fun onResume() {
        super.onResume()
        var weatherFactory = HomeViewModelFactory(
            Repository.getInstance(
                WeatherClient.getInstance() as RemoteSource
                , ConcreteLocalSource.getInstance(requireContext()) as LocalDataSource
            ) as Repository,
            requireContext()
        )
        viewModel = ViewModelProvider(this, weatherFactory).get(HomeViewModel::class.java)
        viewModel.getWeatherData(args.locationData.myLatitude,args.locationData.myLongitude)
        viewModel.homeData.observe(viewLifecycleOwner) {
            if (it != null) {
                setHomeScreenData(it)
                setHomeScreenAdapter(it)
            }
        }
    }

    fun convertDate(time: Long?): String {
        var simpleDate = SimpleDateFormat("dd-MM-yyyy")
        var currentDate = simpleDate.format(time?.times(1000))
        var date1: Date = simpleDate.parse(currentDate)
        val split = date1.toString().split(" ")
        var myDate = "${split[0]},${split[2]} ${split[1]} "
        return myDate
    }
    private fun setHomeScreenData(it: MyResponse) {
        detailsBinding.detailsDateTxt.text = convertDate(it.current?.dt)
        detailsBinding.detailsCountryTxt.text = it.timezone
        detailsBinding.detailsDegeeTxt.text = it.current?.temp?.toInt().toString()
        detailsBinding.detailsDegreeType.text = Constants.cel
        detailsBinding.detailsWeatherStatus.text = it.current?.weather?.get(0)?.description
        val url = "https://openweathermap.org/img/wn/${it.current?.weather?.get(0)?.icon}@2x.png"
        Glide.with(requireContext()).load(url).into(detailsBinding.detailsWeatherImage)
        detailsBinding.detailsPressureValue.text = "${it.current?.pressure} hpa"
        detailsBinding.detailsCloudValue.text = "${it.current?.clouds}%"
        detailsBinding.detailsVisibilityValue.text = "${it.current?.visibility}m"
        detailsBinding.detailsHumidityValue.text = "${it.current?.humidity}%"
        detailsBinding.detailsUltravilotValue.text = it.current?.uvi.toString()
        detailsBinding.detailsWindValue.text = "${it.current?.wind_speed} m/s"
    }
    private fun setHomeScreenAdapter(it: MyResponse) {
        hourAdapter = DetailsHoursAdapter(it.hourly)
        dayAdapter = DetailsDailyAdapter(it.daily)
        detailsBinding.detailsHoursRV.adapter = hourAdapter
        detailsBinding.detailsHoursRV.layoutManager = manger
        detailsBinding.detailsDaysRV.adapter = dayAdapter
        detailsBinding.detailsDaysRV.layoutManager = manger2
    }


}