package com.example.autocars.db.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.autocars.MainApplication
import com.example.autocars.R
import com.example.autocars.db.entities.Favorites
import com.example.autocars.db.entities.PopularList
import com.example.autocars.ui.base.IFavoriteCallback
import com.example.autocars.ui.base.Util

class FavoritesMainAdapter(private val favorites : List<Favorites>, val callback : IFavoriteCallback):
    RecyclerView.Adapter<FavoritesMainAdapter.FavoritesMainAdapterViewHolder> () {

   class FavoritesMainAdapterViewHolder : RecyclerView.ViewHolder {
     var carImage : ImageView
     var guaranto_name : TextView
     var address : TextView
     var occupation : TextView
     var textViewOptions : TextView
     var parent_linear : LinearLayout

        constructor(itemView: View) : super(itemView) {
            carImage = itemView.findViewById(R.id.guaranto_id)
            guaranto_name = itemView.findViewById(R.id.guaranto_name)
            address = itemView.findViewById(R.id.address)
            occupation = itemView.findViewById(R.id.occupation)
            textViewOptions = itemView.findViewById(R.id.textViewOptions)
            parent_linear = itemView.findViewById(R.id.parent_linear)
//            itemView.setOnClickListener{
//             //  // callback.openData(seriesList[adapterPosition],"")
//            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoritesMainAdapter.FavoritesMainAdapterViewHolder {
        var inFlated =  LayoutInflater.from(MainApplication.instance!!.applicationContext).inflate(R.layout.favorite_list_card,parent,false)
        return FavoritesMainAdapterViewHolder(inFlated)
    }

    override fun getItemCount(): Int {
         return favorites.size
    }

    override fun onBindViewHolder(
        holder: FavoritesMainAdapter.FavoritesMainAdapterViewHolder,
        position: Int
    ) {
        val seriesList1 = favorites[position]
         holder.guaranto_name.text   = seriesList1.title
        Glide.with(MainApplication.instance!!.applicationContext)
                .load(seriesList1.imageUrl)
                .placeholder(R.drawable.carlogoicon)
                .into(holder.carImage)
        holder.address.text = seriesList1.year
        holder.occupation.text = Util.getNairaUnitFormat(seriesList1.marketplacePrice.toString())
        holder.parent_linear.setOnClickListener {
            callback.passSearchLink1(seriesList1,1)
        }
        holder.textViewOptions.setOnClickListener {
            val popUp = PopupMenu(MainApplication.instance!!.applicationContext,holder.textViewOptions)
            popUp.inflate(R.menu.favorite_menu)
            popUp.show()
            popUp.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.delete -> {
                        callback.passSearchLink1(seriesList1,2)
                    }
                }
                false
            }
            true
        }


    }
}