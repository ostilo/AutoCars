package com.example.autocars.db.adapter

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.widget.ImageViewCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.autocars.MainApplication
import com.example.autocars.R
import com.example.autocars.db.entities.PopularList
import com.example.autocars.ui.base.ICarSearchCallback
import com.example.autocars.ui.base.Util
import com.example.autocars.ui.base.Util.getNairaUnitFormat
import es.dmoral.toasty.Toasty



class MainListSearchAdapter(private var callback: ICarSearchCallback): PagingDataAdapter<PopularList, RecyclerView.ViewHolder>(DataDifferntiator) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? PopularMakesViewHolder)?.bind(item = getItem(position), callback = callback)
        (holder as? PopularMakesViewHolder)?.searchParent?.setOnClickListener {
            callback.passSearchLink(getItem(position), 1)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PopularMakesViewHolder.getInstance(parent)
    }


    class PopularMakesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        companion object {
            //get instance of the PopularMakesViewHolder
            fun getInstance(parent: ViewGroup): PopularMakesViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val view = inflater.inflate(R.layout.full_cars_item_card, parent, false)
                return PopularMakesViewHolder(view)
            }
        }

        var ivCarsMain: ImageView = view.findViewById(R.id.full_scroll_image)
        private var carName : TextView = view.findViewById(R.id.full_name)
        private var gradeScore : TextView = view.findViewById(R.id.grade_score)
        private var newPriceSale : TextView = view.findViewById(R.id.new_price_sale)
        private var oldPriceSale : TextView = view.findViewById(R.id.old_price_sale)
        private var carYear : TextView = view.findViewById(R.id.car_year)
        private var fullCarChar : TextView = view.findViewById(R.id.full_car_char)
        var searchParent : RelativeLayout = view.findViewById(R.id.search_parent)
        var favorite1 : ImageView = view.findViewById(R.id.favorite1)
        private var favorite : ImageView = view.findViewById(R.id.favorite)

        var floatAdd : ImageView = view.findViewById(R.id.float_add)
        fun bind(item: PopularList?, callback: ICarSearchCallback) {
            carName.text = item?.title
            fullCarChar.text = item?.title?.substringBefore(" ")
            newPriceSale.text = Util.getNairaUnitFormat(item?.marketplacePrice.toString(), true)
            carYear.text = item?.year.toString()
            oldPriceSale.apply {
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                text =  getNairaUnitFormat(item?.marketplaceOldPrice.toString(),true)
            }
            gradeScore.text = String.format("%.1f", item?.gradeScore);

            ivCarsMain.load(item?.imageUrl) {
                placeholder(R.drawable.carlogoicon)
            }
                    favorite1?.setOnClickListener {
                        if(item?.salFavorite!!){
                            // setColorFilter(resources.getColor(R.color.red))
                            favorite1.apply{
                                item?.salFavorite = false
                                favorite.visibility = View.GONE
                                DrawableCompat.setTint(favorite1.getDrawable().mutate(), ContextCompat.getColor(favorite1.context, R.color.black))
                                callback.passSearchLink(item, 3)
                            }
                        }else{
                            favorite1.apply {
                                favorite.visibility = View.VISIBLE
                                DrawableCompat.setTint(favorite1.getDrawable().mutate(), ContextCompat.getColor(favorite1.context, R.color.red))
                                //setColorFilter(resources.getColor(R.color.black))
                                item?.salFavorite = true
                                callback.passSearchLink(item, 2)
                            }
                        }

                    }
            floatAdd.setOnClickListener{
                Toasty.success(MainApplication.instance!!, MainApplication.instance!!.applicationContext.getString(R.string.cart_sucess), Toast.LENGTH_SHORT, true).show()
            }

        }
    }
    object DataDifferntiator : DiffUtil.ItemCallback<PopularList>() {
        override fun areItemsTheSame(oldItem: PopularList, newItem: PopularList): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PopularList, newItem: PopularList): Boolean {
            return oldItem == newItem
        }
    }

}