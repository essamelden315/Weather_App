package com.example.weather.favorite.view

import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.weather.R
import com.example.weather.database.ConcreteLocalSource
import com.example.weather.database.LocalDataSource
import com.example.weather.databinding.FragmentDetailsBinding
import com.example.weather.home.viewmodel.HomeViewModel
import com.example.weather.home.viewmodel.HomeViewModelFactory
import com.example.weather.model.MyResponse
import com.example.weather.model.Repository
import com.example.weather.network.ApiState
import com.example.weather.network.RemoteSource
import com.example.weather.network.WeatherClient
import com.example.weather.utilities.ChangeIcons
import com.example.weather.utilities.FacilitateWork
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
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
    lateinit var sharedPref: SharedPreferences
    private lateinit var progressDialog: ProgressDialog
    lateinit var lang:String
    lateinit var location:String
    lateinit var speed:String
    lateinit var temp:String
    private var units:String=""
    private var degreeType:String=""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPref = requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE)
        lang=sharedPref.getString("language","en").toString()
        location=sharedPref.getString("location","gps").toString()
        speed=sharedPref.getString("speed","meter").toString()
        temp=sharedPref.getString("temp","kel").toString()
        units = FacilitateWork.getTempUnitAndSign(temp,requireActivity()).first
        degreeType = FacilitateWork.getTempUnitAndSign(temp,requireActivity()).second
        detailsBinding = FragmentDetailsBinding.inflate(inflater, container, false)
        manger = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        manger2 = LinearLayoutManager(context)
        progressDialog = ProgressDialog(context)
        progressDialog.setMessage(activity?.getString(R.string.dialogProgress))

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
        viewModel.getWeatherData(
            args.locationData.myLatitude,
            args.locationData.myLongitude,
            lang,
            units)
        lifecycleScope.launch{
            viewModel.homeData.collect{
                when (it){
                    is ApiState.Loading ->{
                        progressDialog.show()
                        Toast.makeText(context,"Loading", Toast.LENGTH_SHORT).show()
                    }
                    is ApiState.Success ->{
                        progressDialog.hide()
                        setDetailsScreenData(it.myResponse as MyResponse)
                        setDetailsScreenAdapter(it.myResponse as MyResponse)
                    }
                    else ->  Snackbar.make(
                        detailsBinding.imageView3,
                        "There is no internet connection",
                        Snackbar.LENGTH_LONG
                    ).show()
                }


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
    private fun setDetailsScreenData(it: MyResponse) {
        detailsBinding.detailsDateTxt.text = convertDate(it.current?.dt)
        detailsBinding.detailsCountryTxt.text = it.timezone
        detailsBinding.detailsDegeeTxt.text = it.current?.temp?.toInt().toString()
        detailsBinding.detailsDegreeType.text = degreeType
        detailsBinding.detailsWeatherStatus.text = it.current?.weather?.get(0)?.description
        detailsBinding.detailsWeatherImage.setImageResource(ChangeIcons.newIcon((it.current?.weather?.get(0)?.icon).toString()))
        detailsBinding.detailsPressureValue.text = "${it.current?.pressure} hpa"
        detailsBinding.detailsCloudValue.text = "${it.current?.clouds}%"
        detailsBinding.detailsVisibilityValue.text = "${it.current?.visibility}m"
        detailsBinding.detailsHumidityValue.text = "${it.current?.humidity}%"
        detailsBinding.detailsUltravilotValue.text = it.current?.uvi.toString()
        detailsBinding.detailsWindValue.text = "${it.current?.wind_speed} m/s"
    }
    private fun setDetailsScreenAdapter(it: MyResponse) {
        hourAdapter = DetailsHoursAdapter(it.hourly)
        dayAdapter = DetailsDailyAdapter(it.daily)
        detailsBinding.detailsHoursRV.adapter = hourAdapter
        detailsBinding.detailsHoursRV.layoutManager = manger
        detailsBinding.detailsDaysRV.adapter = dayAdapter
        detailsBinding.detailsDaysRV.layoutManager = manger2
    }


}