package com.example.weather.home.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.weather.R


class Home : Fragment() {
    lateinit var cons: ConstraintLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewObj:View = inflater.inflate(R.layout.fragment_home, container, false)
        cons = viewObj.findViewById(R.id.homeConstraint)
       /* val light = true
        if(light) {
            cons.setBackgroundResource(R.drawable.gradient_night_background)
            //activity?.window?.statusBarColor = getColor(requireContext(),R.color.nightStart)
            activity?.actionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#09203f")))
        }
        else{

            cons.setBackgroundResource(R.drawable.gradient_morning_background)
            //activity?.window?.statusBarColor = getColor(requireContext(),R.color.endLigtBlue)
            requireActivity().actionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#a1c4fd")))
        }*/
        return viewObj
    }


}