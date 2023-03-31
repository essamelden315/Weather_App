package com.example.weather.settings.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.weather.R
import com.example.weather.utilities.FacilitateWork
import com.example.weather.databinding.FragmentSettingsBinding
class Settings : Fragment() {

    lateinit var settingsBinding: FragmentSettingsBinding
    lateinit var sharedPref:SharedPreferences
    lateinit var edit:SharedPreferences.Editor
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.setTitle(R.string.menu_settings)
        //shared preference initialization
        sharedPref = requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE)
        edit = sharedPref.edit()

        settingsBinding = FragmentSettingsBinding.inflate(inflater, container, false)
        setChoises()
        settingsBinding.LanguageGroup.setOnCheckedChangeListener { group, checkedId ->
            if (group.checkedRadioButtonId == R.id.AR_Radio)
            {
                edit.putString("language","ar")
                edit.commit()
                FacilitateWork.locale("ar",resources)
            }
            else {
                edit.putString("language","en")
                edit.commit()
                FacilitateWork.locale("en",resources)

            }
        }
        settingsBinding.LocationGroup.setOnCheckedChangeListener { group, checkedId ->
            if (group.checkedRadioButtonId == R.id.gpsRadio)
            {
                edit.putString("location","gps")
                edit.commit()
                settingsBinding.gpsRadio.isChecked = true
            }
            else {
                edit.putString("location","map")
                edit.commit()
                settingsBinding.mapRadio.isChecked = true
                Navigation.findNavController(group).navigate(R.id.fromSettingsToMap)
            }
        }

        settingsBinding.TempGroup.setOnCheckedChangeListener { group, checkedId ->
            if (group.checkedRadioButtonId == R.id.Cel_Radio)
            {
                edit.putString("temp","cel")
                edit.commit()
                settingsBinding.CelRadio.isChecked = true
            }
            else if(group.checkedRadioButtonId == R.id.Kel_Radio){
                edit.putString("temp","kel")
                edit.commit()
                settingsBinding.KelRadio.isChecked = true
            }
            else{
                edit.putString("temp","feh")
                edit.commit()
                settingsBinding.FehRadio.isChecked = true
            }
        }

        settingsBinding.SpeedGroup.setOnCheckedChangeListener { group, checkedId ->
            if (group.checkedRadioButtonId == R.id.Meter_Radio)
            {
                edit.putString("speed","meter")
                edit.commit()
                settingsBinding.MeterRadio.isChecked = true
            }
            else {
                edit.putString("speed","mile")
                edit.commit()
                settingsBinding.MileRadio.isChecked = true
            }

        }
        return settingsBinding.root
    }

    private fun setChoises() {
        when(sharedPref.getString("temp","kel")){
            "cel" ->settingsBinding.CelRadio.isChecked = true
            "kel" ->settingsBinding.KelRadio.isChecked = true
            else  ->settingsBinding.FehRadio.isChecked = true
        }
        when(sharedPref.getString("language","en")){
            "en" ->settingsBinding.ENRadio.isChecked = true
            else ->settingsBinding.ARRadio.isChecked = true
        }
        when(sharedPref.getString("speed","meter")){
            "meter" ->settingsBinding.MeterRadio.isChecked = true
            else    ->settingsBinding.MileRadio.isChecked = true
        }
        when(sharedPref.getString("location","gps")){
            "gps" ->settingsBinding.gpsRadio.isChecked = true
            else  ->settingsBinding.mapRadio.isChecked = true
        }
    }

}