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

        return viewObj
    }


}