package com.example.weather.settings.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
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
        settingsBinding.LanguageGroup.setOnCheckedChangeListener { group, checkedId ->
            var item = group.checkedRadioButtonId
            if (item == R.id.AR_Radio)
            {
                Toast.makeText(requireContext(),"arabic",Toast.LENGTH_SHORT).show()
            }
            else
                Toast.makeText(requireContext(),"English",Toast.LENGTH_SHORT).show()
        }

        return settingsBinding.root
    }
   /* var selectedItem = binding.IntialradioGroup.checkedRadioButtonId
    if(selectedItem == R.id.radio_map){
        editor.putString("location","map")
    }else
    {
        editor.putString("location", "gps")
    }*/

}