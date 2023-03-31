package com.example.weather.home.view

import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.weather.R
import com.example.weather.database.ConcreteLocalSource
import com.example.weather.database.LocalDataSource
import com.example.weather.databinding.FragmentHomeBinding
import com.example.weather.home.viewmodel.HomeViewModel
import com.example.weather.home.viewmodel.HomeViewModelFactory
import com.example.weather.model.MyResponse
import com.example.weather.model.Repository
import com.example.weather.network.NetworkListener
import com.example.weather.network.RemoteSource
import com.example.weather.network.WeatherClient
import com.example.weather.utilities.FacilitateWork
import com.example.weather.utilities.TimeConverter
import com.google.android.material.snackbar.Snackbar

class Home : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var hourAdapter: HoursAdapter
    private lateinit var dayAdapter: DailyAdapter
    private lateinit var manger: LinearLayoutManager
    private lateinit var manger2: LinearLayoutManager
    private lateinit var viewModel: HomeViewModel
    private lateinit var sharedPref: SharedPreferences
    private lateinit var lang: String
    private lateinit var location: String
    private lateinit var speed: String
    private lateinit var temp: String
    private var units: String = ""
    private var degreeType: String = ""
    private lateinit var progressDialog: ProgressDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.setTitle(R.string.menu_home)
        sharedPref = requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE)
        lang = sharedPref.getString("language", "en").toString()
        location = sharedPref.getString("location", "gps").toString()
        speed = sharedPref.getString("speed", "meter").toString()
        temp = sharedPref.getString("temp", "kel").toString()
        units = FacilitateWork.getTempUnitAndSign(temp, requireActivity()).first
        degreeType = FacilitateWork.getTempUnitAndSign(temp, requireActivity()).second
        progressDialog = ProgressDialog(context)
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        manger = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        manger2 = LinearLayoutManager(context)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        FacilitateWork.locale(lang, resources)
        progressDialog.setMessage("Loading...")
        progressDialog.show()
        var weatherFactory = HomeViewModelFactory(
            Repository.getInstance(
                WeatherClient.getInstance() as RemoteSource,
                ConcreteLocalSource.getInstance(requireContext()) as LocalDataSource
            ) as Repository, requireContext()
        )
        viewModel = ViewModelProvider(this, weatherFactory).get(HomeViewModel::class.java)
        if (NetworkListener.getConnectivity(requireContext())) {
            if(location =="map"){
                val sp = requireActivity().getSharedPreferences("mapData", Context.MODE_PRIVATE)
                val lat = sp.getString("lat","${0.0}")?.toDouble() as Double
                val lon = sp.getString("lon","${0.0}")?.toDouble() as Double
                viewModel.getWeatherData(lat ,lon ,lang ,units)
            }else {

                viewModel.getLatitude_Longitude(lang, units)
            }
            viewModel.homeData.observe(viewLifecycleOwner) {

                setHomeScreenData(it)
                setHomeScreenAdapter(it)
                progressDialog.dismiss()
                viewModel.insertHomeDataIntoDataBase(it)
            }
        } else {

            viewModel.getHomeDataFromDataBase()
            viewModel.homeData.observe(viewLifecycleOwner) {
                setHomeScreenData(it)
                setHomeScreenAdapter(it)
            }
            progressDialog.dismiss()
            Snackbar.make(
                binding.imageView3,
                "There is no internet connection",
                Snackbar.LENGTH_LONG
            ).show()
        }

    }

    private fun setHomeScreenData(it: MyResponse) {
        progressDialog.show()
        binding.dateTxt.text = TimeConverter.convertHomeDate(it.current?.dt)
        binding.countryTxt.text = it.timezone
        binding.degeeTxt.text = it.current?.temp?.toInt().toString()
        binding.degreeType.text = degreeType
        binding.weatherStatus.text = it.current?.weather?.get(0)?.description
        val url = "https://openweathermap.org/img/wn/${it.current?.weather?.get(0)?.icon}@2x.png"
        Glide.with(requireContext()).load(url).into(binding.weatherImage)
        binding.pressureValue.text = "${it.current?.pressure} hpa"
        binding.cloudValue.text = "${it.current?.clouds}%"
        binding.visibilityValue.text = "${it.current?.visibility}m"
        binding.humidityValue.text = "${it.current?.humidity}%"
        binding.ultravilotValue.text = it.current?.uvi.toString()
        binding.windValue.text = "${it.current?.wind_speed} m/s"
    }

    private fun setHomeScreenAdapter(it: MyResponse) {
        hourAdapter = HoursAdapter(it.hourly)
        dayAdapter = DailyAdapter(it.daily)
        binding.hoursRV.adapter = hourAdapter
        binding.hoursRV.layoutManager = manger
        binding.daysRV.adapter = dayAdapter
        binding.daysRV.layoutManager = manger2
    }


}