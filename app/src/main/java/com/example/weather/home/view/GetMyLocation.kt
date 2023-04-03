package com.example.weather.home.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Proxy.getHost
import android.net.Uri
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.*


private const val My_LOCATION_PERMISSION_ID = 5005

class GetMyLocation(private val context: Context) {

    var fusedClient: FusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(context)
    private var _location: MutableLiveData<List<Double>> = MutableLiveData<List<Double>>()
    val location: LiveData<List<Double>> = _location

    @SuppressLint("MissingPermission")
    fun getLastLocation() {
        if (checkPermissions()) {
            if (isEnabled()) {
                requestNewLocationData()
            } else {
                Toast.makeText(context, "Turn on location", Toast.LENGTH_LONG)
                val setting: Intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                context.startActivity(setting)
            }
        } else
            requestPersmission()
    }


    fun checkPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                ||
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    fun isEnabled(): Boolean {
        val locationManager: LocationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                ||
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    fun requestNewLocationData() {
        val locationRequest = LocationRequest.create().apply {
            interval = 0
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        fusedClient.requestLocationUpdates(locationRequest, locationCallBack, Looper.myLooper())
    }
    val locationCallBack = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation
            _location.postValue( listOf( mLastLocation.latitude,mLastLocation.longitude))
            stopLocation()
        }
    }
    fun stopLocation(){
        fusedClient.removeLocationUpdates(locationCallBack)
    }
    fun requestPersmission() {
        ActivityCompat.requestPermissions(
            context as Activity, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            My_LOCATION_PERMISSION_ID
        )
    }
}





