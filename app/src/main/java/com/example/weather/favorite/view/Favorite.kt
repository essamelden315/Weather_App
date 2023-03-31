package com.example.weather.favorite.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.transaction
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather.R
import com.example.weather.database.ConcreteLocalSource
import com.example.weather.database.LocalDataSource
import com.example.weather.databinding.FragmentFavoriteBinding
import com.example.weather.favorite.viewmodel.FavoriteViewModel
import com.example.weather.favorite.viewmodel.FavoriteViewModelFactory
import com.example.weather.home.viewmodel.HomeViewModel
import com.example.weather.mapfragment.MapsFragment
import com.example.weather.model.Repository
import com.example.weather.model.SavedDataFormula
import com.example.weather.model.Weather
import com.example.weather.network.NetworkListener
import com.example.weather.network.RemoteSource
import com.example.weather.network.WeatherClient
import com.google.android.material.snackbar.Snackbar


class Favorite : Fragment(),ListnerInterface {
    lateinit var binding: FragmentFavoriteBinding
    lateinit var favAdapter: FavAdapter
    lateinit var manager: LinearLayoutManager
    lateinit var viewModel:FavoriteViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.setTitle(R.string.menu_favorites)
        binding=FragmentFavoriteBinding.inflate(inflater, container, false)
        manager = LinearLayoutManager(context)

        val favFactory =FavoriteViewModelFactory(
            Repository.getInstance(WeatherClient.getInstance() as RemoteSource
                ,ConcreteLocalSource.getInstance(requireContext()) as LocalDataSource) as Repository
        )
        viewModel = ViewModelProvider(requireActivity(), favFactory).get(FavoriteViewModel::class.java)
        favAdapter = FavAdapter(listOf(), this)
        viewModel.favData.observe(viewLifecycleOwner) { data ->
               favAdapter.setList(data)
        }
        binding.FavRV.adapter = favAdapter
        binding.FavRV.layoutManager = manager
        binding.favFAB.setOnClickListener() {
            if(NetworkListener.getConnectivity(requireContext()))
            Navigation.findNavController(it).navigate(R.id.fromFavToMap)
            else
                Snackbar.make(binding.favFAB,"There is no internet connection",Snackbar.LENGTH_LONG).show()
        }
        return binding.root
    }


    override fun insertData(savedDataFormula: SavedDataFormula) {
        viewModel.insertIntoFav(savedDataFormula)
    }

    override fun deleteDatafromDB(savedDataFormula: SavedDataFormula) {
        viewModel.deleteFromFav(savedDataFormula)
    }

}