package com.example.autocars.db.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.VideoView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.autocars.MainApplication
import com.example.autocars.R
import com.example.autocars.db.entities.CarMedia
import com.example.autocars.ui.base.ICarMediaCallback


class CarMediaListAdapter(private var callback: ICarMediaCallback) : PagingDataAdapter<CarMedia, RecyclerView.ViewHolder>(DataDifferntiator) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? CarMediaLisViewHolder)?.bind(item = getItem(position))
        (holder as? CarMediaLisViewHolder)?.ivCarsMain?.setOnClickListener {
            callback.passVideoLink(getItem(position), position)
        }
        (holder as? CarMediaLisViewHolder)?.video?.setOnClickListener {
            callback.passVideoLink(getItem(position), position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CarMediaLisViewHolder.getInstance(parent)
    }

    class CarMediaLisViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        companion object {
            //get instance of the PopularMakesViewHolder
            fun getInstance(parent: ViewGroup): CarMediaLisViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val view = inflater.inflate(R.layout.cars_item_image_card, parent, false)
                return CarMediaLisViewHolder(view)
            }
        }

        var ivCarsMain: ImageView = view.findViewById(R.id.scroll_image)
        var video : VideoView = view.findViewById(R.id.video_view)
         fun bind(item: CarMedia?) {
             Glide.with(MainApplication.instance!!.applicationContext)
                     .load(item?.url)
                     .placeholder(R.drawable.carlogoicon)
                     .into(ivCarsMain)
            }
//             if(item!!.type.contains("image")){
//                 ivCarsMain.load(item?.url) {
//                     placeholder(R.drawable.carlogoicon)
//                 }
//             }
//            if(item?.type!!.contains("video")){
//                video.visibility = View.VISIBLE
//                ivCarsMain.visibility = View.INVISIBLE
//                video.setVideoURI(Uri.parse(item?.url))
//            }

    }

    object DataDifferntiator : DiffUtil.ItemCallback<CarMedia>() {
        override fun areItemsTheSame(oldItem: CarMedia, newItem: CarMedia): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CarMedia, newItem: CarMedia): Boolean {
            return oldItem.equals(newItem)
        }
    }

}