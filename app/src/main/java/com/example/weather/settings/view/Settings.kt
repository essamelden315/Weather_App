package com.example.weather.settings.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weather.R
import com.example.weather.databinding.FragmentSettingsBinding

class Settings : Fragment() {

    lateinit var settingsBinding: FragmentSettingsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.setTitle("Settings")
        settingsBinding = FragmentSettingsBinding.inflate(inflater, container, false)
        return settingsBinding.root
    }


}