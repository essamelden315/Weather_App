package com.example.weather.mapfragment

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weather.R
import com.example.weather.database.ConcreteLocalSource
import com.example.weather.database.LocalDataSource
import com.example.weather.databinding.FragmentMapsBinding
import com.example.weather.favorite.viewmodel.FavoriteViewModel
import com.example.weather.favorite.viewmodel.FavoriteViewModelFactory
import com.example.weather.model.Repository
import com.example.weather.model.SavedDataFormula
import com.example.weather.network.RemoteSource
import com.example.weather.network.WeatherClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapsFragment : Fragment(){
    lateinit var fusedClient: FusedLocationProviderClient
    lateinit var binding: FragmentMapsBinding
    lateinit var mapFragment: SupportMapFragment
    lateinit var mMap:GoogleMap
    lateinit var viewModel:FavoriteViewModel

    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap
        mMap.setOnMapClickListener {
            mMap.clear()
            mMap.addMarker(MarkerOptions().position(it))
            goToLatLng(it.latitude,it.longitude,16f)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(callback)
        mapInitialize()
        return binding.root
    }

    private fun mapInitialize(){
        val locationRequest:LocationRequest = LocationRequest()
        locationRequest.setInterval(5000)
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        locationRequest.setSmallestDisplacement(14f)
        locationRequest.setFastestInterval(3000)
        binding.searchEdt.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if(actionId==EditorInfo.IME_ACTION_SEARCH ||actionId==EditorInfo.IME_ACTION_DONE
                ||event.action == KeyEvent.ACTION_DOWN || event.action == KeyEvent.KEYCODE_ENTER)
            {
                goToSearchLocation()
            }

            false
        })
        fusedClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }


    private fun goToSearchLocation() {
        var searchLocation = binding.searchEdt.text.toString()
        var list =Geocoder(requireContext()).getFromLocationName(searchLocation,1)
        if(list != null && list.size > 0){
            var address:Address = list.get(0)
            goToLatLng(address.latitude,address.longitude,16f)
        }

    }

    private fun goToLatLng(latitude: Double, longitude: Double, fl: Float) {
        var geocoder = Geocoder(requireContext()).getFromLocation(latitude,longitude,1)
        var name = "${geocoder?.get(0)?.subAdminArea}, ${geocoder?.get(0)?.adminArea}"
        var latlng = LatLng(latitude,longitude)
        var update: CameraUpdate = CameraUpdateFactory.newLatLngZoom(latlng,fl)
        mMap.addMarker(MarkerOptions().position(latlng))
        mMap.animateCamera(update)
        binding.mapAddToFav.setOnClickListener{
            val favFactory = FavoriteViewModelFactory(
                Repository.getInstance(
                    WeatherClient.getInstance() as RemoteSource
                    , ConcreteLocalSource.getInstance(requireContext()) as LocalDataSource
                ) as Repository
            )
            viewModel = ViewModelProvider(this, favFactory).get(FavoriteViewModel::class.java)
            viewModel.insertIntoFav(SavedDataFormula(latitude,longitude,name))
            Log.i("room", "inserted")
            Toast.makeText(requireContext().applicationContext,"Data inserted successfully",Toast.LENGTH_SHORT)
        }

    }


}