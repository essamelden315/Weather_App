package com.example.weather.favorite.view

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.SimpleAdapter.ViewBinder
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.Constraints
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorDestinationBuilder
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.databinding.FavLayoutBinding
import com.example.weather.model.SavedDataFormula
import com.example.weather.network.NetworkListener
import com.google.android.material.snackbar.Snackbar

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
            dialogDeleteConfirmation(favItem)
        }

            holder.viewBinding.favCardView.setOnClickListener {
                if(NetworkListener.getConnectivity(context)){
                val action = FavoriteDirections.fromFavToDetails(favItem)
                Navigation.findNavController(it).navigate(action)
                }else{
                    Snackbar.make(holder.viewBinding.favCardView,"There is no internet connection", Snackbar.LENGTH_LONG).show()
                }
            }


    }

    override fun getItemCount(): Int {
        return favList.size
    }
    fun setList(favouriteList: List<SavedDataFormula>){
        favList = favouriteList
        notifyDataSetChanged()
    }
    fun dialogDeleteConfirmation(favItem:SavedDataFormula) {
        var dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.delete_dialog)
        val window: Window? = dialog.getWindow()
        window?.setLayout(
            Constraints.LayoutParams.MATCH_PARENT,
            Constraints.LayoutParams.WRAP_CONTENT

        )
        window?.setBackgroundDrawableResource(R.color.white);
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()


        dialog.findViewById<Button>(R.id.warn_deletBtn).setOnClickListener {
            listner.deleteDatafromDB(favItem)
            notifyDataSetChanged()
            dialog.dismiss()
        }
        dialog.findViewById<Button>(R.id.warn_cancelBtn).setOnClickListener() {
            dialog.dismiss()
        }
    }
    class myViewHolder(val viewBinding: FavLayoutBinding):RecyclerView.ViewHolder(viewBinding.root)
}