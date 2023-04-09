package com.example.weather.home.view

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
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
import com.example.weather.network.ApiState
import com.example.weather.network.NetworkListener
import com.example.weather.network.RemoteSource
import com.example.weather.network.WeatherClient
import com.example.weather.utilities.ChangeIcons
import com.example.weather.utilities.FacilitateWork
import com.example.weather.utilities.TimeConverter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
    private lateinit var units: String
    private lateinit var degreeType: String
    private var mapDialog: Boolean = false
    private lateinit var progressDialog: ProgressDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        activity?.setTitle(R.string.menu_home)
        openAndGetDataFromSharedPrefrence()
        if(mapDialog)
            findNavController().navigate(R.id.fromHomeToMap)
        progressDialog = ProgressDialog(context)
        manger = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        manger2 = LinearLayoutManager(context)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        FacilitateWork.locale(lang,resources)
        progressDialog.setMessage(activity?.getString(R.string.dialogProgress))
        var weatherFactory = HomeViewModelFactory(
            Repository.getInstance(
                WeatherClient.getInstance() as RemoteSource,
                ConcreteLocalSource.getInstance(requireContext()) as LocalDataSource
            ) as Repository, requireContext()
        )
        binding.homeSwipeLayout.setOnRefreshListener {
            myHomeData(weatherFactory)
            binding.homeSwipeLayout.isRefreshing=false
        }
        myHomeData(weatherFactory)
    }
    fun myHomeData(weatherFactory: HomeViewModelFactory){
        viewModel = ViewModelProvider(this, weatherFactory).get(HomeViewModel::class.java)
        if (NetworkListener.getConnectivity(requireContext())) {
            viewModel.deleteHomeDaraFromDataBase()
            val sp = requireActivity().getSharedPreferences("mapData", Context.MODE_PRIVATE)
            if(location =="map"){
                val lat = sp.getString("lat","${0.0}")?.toDouble() as Double
                val lon = sp.getString("lon","${0.0}")?.toDouble() as Double
                viewModel.getWeatherData(lat ,lon ,lang ,units)
            }else if(location=="gps"){
                var getMyLocation= GetMyLocation(requireContext())
                getMyLocation.getLastLocation()
                getMyLocation.location.observe(context as LifecycleOwner){
                    val insideEdit = sp.edit()
                    insideEdit.putString("lat","${it.get(0)}")
                    insideEdit.putString("lon","${it.get(1)}")
                    insideEdit.commit()
                    viewModel.getWeatherData(it.get(0),it.get(1),lang,units)
                }
            }
            viewLifecycleOwner.lifecycleScope.launch{
                viewModel.homeData.collect{
                        when (it){
                            is ApiState.Loading ->{
                                progressDialog.show()
                            }
                            is ApiState.Success ->{
                                it.myResponse?.let { it1 -> viewModel.insertHomeDataIntoDataBase(it1) }
                                progressDialog.hide()
                                it.myResponse?.let { it1 -> setHomeScreenData(it1) }
                                it.myResponse?.let { it1 -> setHomeScreenAdapter(it1) }
                            }
                            else ->  Snackbar.make(
                                binding.imageView5,
                                "Failed to obtain data from api",
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                }
            }
        } else {
            viewModel.getHomeDataFromDataBase()
            viewLifecycleOwner.lifecycleScope.launch (Dispatchers.IO){
                viewModel.homeData.collect {
                    when (it) {
                        is ApiState.Loading ->{
                            progressDialog.show()
                        }
                        is ApiState.Success -> {
                            withContext(Dispatchers.Main){
                                progressDialog.hide()
                                it.myResponse?.let { it1 -> setHomeScreenData(it1) }
                                it.myResponse?.let { it1 -> setHomeScreenAdapter(it1) }
                            }
                        }
                        else -> Snackbar.make(
                            binding.imageView3,
                            "There is no internet connection",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }

            }
        }
    }
    private fun setHomeScreenData(it: MyResponse) {
        binding.dateTxt.text = TimeConverter.convertHomeDate(it.current?.dt)
        binding.countryTxt.text = it.timezone
        binding.degeeTxt.text = it.current?.temp?.toInt().toString()
        binding.degreeType.text = degreeType
        binding.weatherStatus.text = it.current?.weather?.get(0)?.description
        binding.weatherImage.setImageResource(ChangeIcons.newIcon((it.current?.weather?.get(0)?.icon).toString()))
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

    private fun openAndGetDataFromSharedPrefrence(){
        sharedPref = requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE)
        lang = sharedPref.getString("language", "en").toString()
        location = sharedPref.getString("location", "gps").toString()
        speed = sharedPref.getString("speed", "meter").toString()
        temp = sharedPref.getString("temp", "kel").toString()
        mapDialog = sharedPref.getBoolean("mapFromDialog",false)
        units = FacilitateWork.getTempUnitAndSign(temp, requireActivity()).first
        degreeType = FacilitateWork.getTempUnitAndSign(temp, requireActivity()).second
    }

}