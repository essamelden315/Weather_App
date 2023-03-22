package com.example.weather

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.weather.network.ProductClient
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
        lifecycleScope.launch (Dispatchers.IO){
            val ref =ProductClient.getInstance()
            Log.i("gedoTag", ""+ref?.getRetrofitList()?.current?.windSpeed)
        }

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
        //setupActionBarWithNavController(navController,appBarConfiguration)
        val actionBar = supportActionBar
        actionBar?.setHomeAsUpIndicator(R.drawable.baseline_menu_24)
        actionBar?.setDisplayShowHomeEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)


    }


    /* override fun onSupportNavigateUp(): Boolean {
       val navController:NavController=findNavController(R.id.nav_host_fragment)
       return navController.navigateUp(appBarConfiguration)||super.onSupportNavigateUp()
   }*/
}