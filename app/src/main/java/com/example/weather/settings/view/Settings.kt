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
import com.example.weather.databinding.FragmentSettingsBinding
import com.example.weather.network.NetworkListener
import com.example.weather.utilities.FacilitateWork

class Settings : Fragment() {

    lateinit var settingsBinding: FragmentSettingsBinding
    lateinit var sharedPref: SharedPreferences
    lateinit var edit: SharedPreferences.Editor
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
            if (group.checkedRadioButtonId == R.id.AR_Radio) {
                edit.putString("language", "ar")
                edit.commit()
                FacilitateWork.locale("ar", resources)
                activity?.recreate()
            } else {
                edit.putString("language", "en")
                edit.commit()
                FacilitateWork.locale("en", resources)
                activity?.recreate()
            }
        }
        if (NetworkListener.getConnectivity(requireContext())) {
            settingsBinding.gpsRadio.isEnabled = true
            settingsBinding.mapRadio.isEnabled = true
            settingsBinding.LocationGroup.setOnCheckedChangeListener { group, checkedId ->
                if (group.checkedRadioButtonId == R.id.gpsRadio) {
                    edit.putString("location", "gps")
                    edit.commit()
                    settingsBinding.gpsRadio.isChecked = true
                } else {
                    edit.putString("location", "map")
                    edit.commit()
                    settingsBinding.mapRadio.isChecked = true
                    Navigation.findNavController(group).navigate(R.id.fromSettingsToMap)
                }
            }
        }
        else{
            settingsBinding.gpsRadio.isEnabled = false
            settingsBinding.mapRadio.isEnabled = false
        }

        if (NetworkListener.getConnectivity(requireContext())) {
            settingsBinding.TempGroup.setOnCheckedChangeListener { group, checkedId ->
                settingsBinding.CelRadio.isEnabled = true
                settingsBinding.KelRadio.isEnabled = true
                settingsBinding.FehRadio.isEnabled = true
                if (group.checkedRadioButtonId == R.id.Cel_Radio) {
                    edit.putString("temp", "cel")
                    edit.commit()
                    settingsBinding.CelRadio.isChecked = true
                } else if (group.checkedRadioButtonId == R.id.Kel_Radio) {
                    edit.putString("temp", "kel")
                    edit.commit()
                    settingsBinding.KelRadio.isChecked = true
                } else {
                    edit.putString("temp", "feh")
                    edit.commit()
                    settingsBinding.FehRadio.isChecked = true
                }
            }
        } else {
            settingsBinding.CelRadio.isEnabled = false
            settingsBinding.KelRadio.isEnabled = false
            settingsBinding.FehRadio.isEnabled = false
        }


        return settingsBinding.root
    }

    private fun setChoises() {
        when (sharedPref.getString("temp", "kel")) {
            "cel" -> settingsBinding.CelRadio.isChecked = true
            "kel" -> settingsBinding.KelRadio.isChecked = true
            else -> settingsBinding.FehRadio.isChecked = true
        }
        when (sharedPref.getString("language", "en")) {
            "en" -> settingsBinding.ENRadio.isChecked = true
            else -> settingsBinding.ARRadio.isChecked = true
        }
        when (sharedPref.getString("location", "gps")) {
            "gps" -> settingsBinding.gpsRadio.isChecked = true
            else -> settingsBinding.mapRadio.isChecked = true
        }
    }

}