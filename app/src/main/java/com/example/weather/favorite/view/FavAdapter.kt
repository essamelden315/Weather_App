package com.example.weather.favorite.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SimpleAdapter.ViewBinder
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorDestinationBuilder
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.databinding.FavLayoutBinding
import com.example.weather.model.SavedDataFormula

class FavAdapter (private var favList: List<SavedDataFormula>,val listner: ListnerInterface):RecyclerView.Adapter<FavAdapter.myViewHolder>() {
    lateinit var context: Context
    lateinit var favBinding:FavLayoutBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val inflater:LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        favBinding = FavLayoutBinding.inflate(inflater,parent,false)
        context=parent.context
        return myViewHolder(favBinding)
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        var favItem = favList[position]
        holder.viewBinding.favCardTitle.text = favItem.locationName
        holder.viewBinding.favCardDeleteBtn.setOnClickListener {
            listner.deleteDatafromDB(favItem)
            notifyDataSetChanged()
        }
        holder.viewBinding.favCardView.setOnClickListener {
            val action = FavoriteDirections.fromFavToDetails(favItem)
            Navigation.findNavController(it).navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return favList.size
    }
    fun setList(favouriteList: List<SavedDataFormula>){
        favList = favouriteList
        notifyDataSetChanged()
    }

    class myViewHolder(val viewBinding: FavLayoutBinding):RecyclerView.ViewHolder(viewBinding.root)
}