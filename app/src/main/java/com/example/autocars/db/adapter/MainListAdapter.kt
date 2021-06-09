package com.example.autocars.db.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.api.load
import coil.decode.SvgDecoder
import coil.request.LoadRequest
import com.bumptech.glide.Glide
import com.example.autocars.MainApplication
import com.example.autocars.R
import com.example.autocars.db.entities.PopularMakes
import org.w3c.dom.Text
import java.util.*

class MainListAdapter : PagingDataAdapter<PopularMakes, RecyclerView.ViewHolder>(DataDifferntiator) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? PopularMakesViewHolder)?.bind(item = getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PopularMakesViewHolder.getInstance(parent)
    }

    class PopularMakesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        companion object {
            //get instance of the PopularMakesViewHolder
            fun getInstance(parent: ViewGroup): PopularMakesViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val view = inflater.inflate(R.layout.cars_item_list, parent, false)
                return PopularMakesViewHolder(view)
            }
        }

        var ivCarsMain: ImageView = view.findViewById(R.id.scroll_image)
        var carName : TextView = view.findViewById(R.id.P_car_name);

        fun bind(item: PopularMakes?) {

//            ivCarsMain.load(item?.imageUrl) {
//                placeholder(R.drawable.carlogoicon)
//            }
            ivCarsMain.loadSvgOrOthers(item?.imageUrl)
            carName.text = item?.name
        }
    }

    object DataDifferntiator : DiffUtil.ItemCallback<PopularMakes>() {
        override fun areItemsTheSame(oldItem: PopularMakes, newItem: PopularMakes): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PopularMakes, newItem: PopularMakes): Boolean {
            return oldItem.equals(newItem)
        }
    }




}

fun ImageView.loadSvgOrOthers(myUrl: String?) {
    myUrl?.let {
        if (it.toLowerCase(Locale.ENGLISH).endsWith("svg")) {
            val imageLoader = ImageLoader.Builder(this.context)
                .componentRegistry {
                    add(SvgDecoder(this@loadSvgOrOthers.context))
                }
                .build()
            val request = LoadRequest.Builder(this.context)
                .data(it)
                .target(this)
                .placeholder(R.drawable.carlogoicon)
                .build()
            imageLoader.execute(request)
        } else {
            this.load(myUrl)
        }
    }
}
