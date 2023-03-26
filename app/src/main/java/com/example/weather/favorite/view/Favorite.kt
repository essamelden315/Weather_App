package com.example.weather.favorite.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.weather.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Favorite : Fragment() {

    lateinit var fab:FloatingActionButton
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewObj =inflater.inflate(R.layout.fragment_favorite, container, false)
        fab = viewObj.findViewById(R.id.fav_FAB)
        fab.setOnClickListener(){
            Navigation.findNavController(it).navigate(R.id.fromFavToMap)
        }
        return viewObj
    }

}