package com.example.weather.splash_screen

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import com.example.weather.MainActivity
import com.example.weather.R
import com.example.weather.databinding.ActivitySplashScreenBinding
import com.example.weather.databinding.HomeDialogLayoutBinding
import com.example.weather.network.NetworkListener
import com.example.weather.utilities.FacilitateWork
import kotlinx.coroutines.delay

class SplashScreen : AppCompatActivity() {
    private lateinit var sharedPref: SharedPreferences
    private lateinit var binding:ActivitySplashScreenBinding
    private lateinit var edit:SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        sharedPref = getSharedPreferences("settings", Context.MODE_PRIVATE)
        edit = sharedPref.edit()
        val isFirst = sharedPref.getBoolean("isFirst",true)
        FacilitateWork.locale(sharedPref.getString("language","en").toString(), resources)
        Handler().postDelayed({
            binding.lottieLayerName.visibility = View.GONE
            when(isFirst){
                true -> showSettingsDialog()
                else -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }, 5000)

    }

    private fun showSettingsDialog() {
        val dialogBinding = HomeDialogLayoutBinding.inflate(layoutInflater)
        val dialog= Dialog(this)
        dialog.setContentView(dialogBinding.root)
        if(NetworkListener.getConnectivity(this)){
            dialogBinding.dialogMap.isEnabled=true
            dialogBinding.dialogGps.isEnabled=true
            dialogBinding.dialogBtn.isEnabled=true

            dialogBinding.dialogGroup.setOnCheckedChangeListener{group , checkId ->
                if(group.checkedRadioButtonId == R.id.dialogMap) {
                    edit.putBoolean("mapFromDialog", true)
                    edit.putString("location","map")
                    edit.commit()
                }
                else{
                    edit.putString("location","gps")
                    edit.commit()
                }

            }
            dialogBinding.dialogBtn.setOnClickListener{
                edit.putBoolean("isFirst",false)
                edit.commit()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }else{
            dialogBinding.dialogMap.isEnabled=false
            dialogBinding.dialogGps.isEnabled=false
            dialogBinding.dialogBtn.isEnabled=false
        }

        dialog.show()


    }

}