package com.example.weather

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.fragment.app.Fragment
import com.example.weather.databinding.FragmentMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapsFragment : Fragment() {
    lateinit var fusedClient: FusedLocationProviderClient
    lateinit var binding: FragmentMapsBinding
    lateinit var mapFragment: SupportMapFragment
    lateinit var mMap:GoogleMap
    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        mMap = googleMap
        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
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
        locationRequest.setSmallestDisplacement(16f)
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
        var geocoder:Geocoder = Geocoder(requireContext())
        var list =geocoder.getFromLocationName(searchLocation,1)
        if(list != null && list.size > 0){
            var address:Address = list.get(0)
            var location:String = address.adminArea
            var latitude = address.latitude
            var longitude = address.longitude
            goToLatLng(latitude,longitude,14f)
        }

    }

    private fun goToLatLng(latitude: Double, longitude: Double, fl: Float) {
        var latlng:LatLng = LatLng(latitude,longitude)
        var update: CameraUpdate = CameraUpdateFactory.newLatLngZoom(latlng,fl)

        mMap.animateCamera(update)
    }


}