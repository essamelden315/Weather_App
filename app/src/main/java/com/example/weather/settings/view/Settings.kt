package com.example.weather.settings.view

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.weather.R
import com.example.weather.databinding.FragmentSettingsBinding
import java.util.*

class Settings : Fragment() {

    lateinit var settingsBinding: FragmentSettingsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.setTitle("Settings")
        settingsBinding = FragmentSettingsBinding.inflate(inflater, container, false)
        settingsBinding.LanguageGroup.setOnCheckedChangeListener { group, checkedId ->
            var item = group.checkedRadioButtonId
            if (item == R.id.AR_Radio)
            {
                val locale = Locale("ar")
                Locale.setDefault(locale)
                val config = Configuration()
                config.locale = locale
                resources.updateConfiguration(config, resources.displayMetrics)
                settingsBinding.ARRadio.isChecked = true
            }
            else {
                Toast.makeText(requireContext(), "English", Toast.LENGTH_SHORT).show()
                val locale = Locale("en")
                Locale.setDefault(locale)
                val config = Configuration()
                config.locale = locale
                resources.updateConfiguration(config, resources.displayMetrics)
                settingsBinding.ENRadio.isChecked = true
            }
        }
        return settingsBinding.root
    }

}