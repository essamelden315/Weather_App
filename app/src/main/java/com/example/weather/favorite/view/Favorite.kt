package com.example.weather.favorite.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.weather.model.Repository
import com.example.weather.model.SavedDataFormula
import com.example.weather.model.Weather
import com.example.weather.network.RemoteSource
import com.example.weather.network.WeatherClient


class Favorite : Fragment(),ListnerInterface {
    lateinit var binding: FragmentFavoriteBinding
    lateinit var favAdapter: FavAdapter
    lateinit var manager: LinearLayoutManager
    lateinit var viewModel:FavoriteViewModel
    lateinit var myList:List<SavedDataFormula>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentFavoriteBinding.inflate(inflater, container, false)
        manager = LinearLayoutManager(context)



        binding.favFAB.setOnClickListener(){
            Navigation.findNavController(it).navigate(R.id.fromFavToMap)
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val favFactory =FavoriteViewModelFactory(
            Repository.getInstance(WeatherClient.getInstance() as RemoteSource
                ,ConcreteLocalSource.getInstance(requireContext()) as LocalDataSource) as Repository
        )
        viewModel = ViewModelProvider(this, favFactory).get(FavoriteViewModel::class.java)
        viewModel.favData.observe(viewLifecycleOwner){data ->
            if(data!=null){
                favAdapter = FavAdapter(listOf(),this)
                binding.FavRV.adapter = favAdapter
                binding.FavRV.layoutManager = manager
                favAdapter.setList(data)
            }else{
                // write lottie animation code here
            }

        }
    }

    override fun deleteDatafromDB(savedDataFormula: SavedDataFormula) {
        viewModel.deleteFromFav(savedDataFormula)
    }

}