package com.example.weather.mapfragment

import android.content.Context
import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
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


class MapsFragment() : Fragment() {
    lateinit var fusedClient: FusedLocationProviderClient
    lateinit var binding: FragmentMapsBinding
    lateinit var mapFragment: SupportMapFragment
    lateinit var mMap: GoogleMap
    lateinit var viewModel: FavoriteViewModel
    private lateinit var sharedPref: SharedPreferences
    private lateinit var edit: SharedPreferences.Editor
    private lateinit var location: String
    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap
        mMap.setOnMapClickListener {
            mMap.clear()
            mMap.addMarker(MarkerOptions().position(it))
            goToLatLng(it.latitude, it.longitude, 16f)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPref = requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE)
        location = sharedPref.getString("location", "gps").toString()
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        if (location == "map") binding.mapAddToFav.text = "Set Location"
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(callback)
        mapInitialize()
        return binding.root
    }


    private fun mapInitialize() {
        val locationRequest = LocationRequest()
        locationRequest.setInterval(5000)
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        locationRequest.setSmallestDisplacement(14f)
        locationRequest.setFastestInterval(3000)
        binding.searchEdt.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE
                || event.action == KeyEvent.ACTION_DOWN || event.action == KeyEvent.KEYCODE_ENTER
            ) {
                if (!binding.searchEdt.text.isNullOrEmpty())
                    goToSearchLocation()
            }

            false
        }
        fusedClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }


    private fun goToSearchLocation() {
        var searchLocation = binding.searchEdt.text.toString()
        var list = Geocoder(requireContext()).getFromLocationName(searchLocation, 1)
        if (list != null && list.size > 0) {
            var address: Address = list.get(0)
            goToLatLng(address.latitude, address.longitude, 16f)
        }

    }

    private fun goToLatLng(latitude: Double, longitude: Double, fl: Float) {
        var name = "Unknown !!!"
        var geocoder = Geocoder(requireContext()).getFromLocation(latitude, longitude, 1)
        if (geocoder!!.size > 0)
            name =
                "${geocoder?.get(0)?.locality}, ${geocoder?.get(0)?.adminArea}, ${geocoder?.get(0)?.countryName}"
        Log.i("mapEssam", "" + geocoder)
        var latlng = LatLng(latitude, longitude)
        var update: CameraUpdate = CameraUpdateFactory.newLatLngZoom(latlng, fl)
        mMap.addMarker(MarkerOptions().position(latlng))
        mMap.animateCamera(update)

        binding.mapAddToFav.setOnClickListener {
            if (location == "map") {
                saveMapLatLon(latitude, longitude)
                Navigation.findNavController(it).navigate(R.id.fromMapToHome)
            } else {
                val favFactory = FavoriteViewModelFactory(
                    Repository.getInstance(
                        WeatherClient.getInstance() as RemoteSource,
                        ConcreteLocalSource.getInstance(requireContext()) as LocalDataSource
                    ) as Repository
                )
                viewModel = ViewModelProvider(
                    requireActivity(),
                    favFactory
                ).get(FavoriteViewModel::class.java)
                viewModel.insertIntoFav(SavedDataFormula(latitude, longitude, name))
                Log.i("room", "inserted")
                Toast.makeText(requireContext(), "Location is added", Toast.LENGTH_SHORT)
                Navigation.findNavController(it).navigate(R.id.fromMapToFav)
            }
        }
    }

    private fun saveMapLatLon(latitude: Double, longitude: Double) {
        var sp = requireActivity().getSharedPreferences("mapData", Context.MODE_PRIVATE)
        edit = sp.edit()
        edit.putString("lat", "$latitude")
        edit.putString("lon", "$longitude")
        edit.commit()
    }

}