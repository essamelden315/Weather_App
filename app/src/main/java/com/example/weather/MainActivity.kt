package com.example.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : AppCompatActivity() {
    lateinit var cons:ConstraintLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cons = findViewById(R.id.CL)
        cons.setBackgroundResource(R.drawable.gradient_night_background)
        window.statusBarColor = getColor(R.color.nightStart)
    }
}