package com.example.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    lateinit var navController:NavController
    lateinit var drawerLayout:DrawerLayout
    lateinit var appBarConfiguration:AppBarConfiguration
    lateinit var navView:NavigationView
   // lateinit var listner:NavController.OnDestinationChangedListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createNavDrawer()

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
    }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
       if (item.itemId == android.R.id.home) {
           if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
               drawerLayout.closeDrawer(GravityCompat.START)
           } else {
               drawerLayout.openDrawer(GravityCompat.START)
           }
       }
       return super.onOptionsItemSelected(item)
   }
    fun createNavDrawer(){
        navController = findNavController(R.id.nav_host_fragment)
        drawerLayout = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.navigationView)
        navView.setupWithNavController(navController)
        appBarConfiguration = AppBarConfiguration(navController.graph,drawerLayout)
        val actionBar = supportActionBar
        /*actionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        actionBar?.setCustomView(R.layout.action_bar_layout)*/
        actionBar?.setHomeAsUpIndicator(R.drawable.baseline_menu_24)
        actionBar?.setDisplayShowHomeEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)

    }


}