package com.example.autocars

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.VideoView
import androidx.navigation.Navigation
import coil.api.load
import com.example.autocars.db.adapter.loadSvgOrOthers
import com.example.autocars.db.entities.CarDetails
import com.example.autocars.db.entities.CarMedia
import com.example.autocars.db.viewmodel.MainViewModel
import com.github.chrisbanes.photoview.PhotoView

class PhotoViewFragment : Fragment() {
    private  lateinit var photo_view : PhotoView
    private lateinit var  btnBackArrow : ImageView
    private lateinit var txtToolbarTitle : TextView
    private lateinit var badge2 : RelativeLayout
    private lateinit var mpw_video_player : VideoView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupData(view)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_photo_view, container, false)
                arguments?.let {
                    var customer : CarMedia? = it.getParcelable(getString(R.string.card_key_photo))
                    if (customer != null) {
                        initiateView(view,customer)
                    }
                }
                return view
    }


    fun initiateView(view:View,customer: CarMedia){
        photo_view = view.findViewById(R.id.photo_view)
        mpw_video_player = view.findViewById(R.id.mpw_video_player)
        if(customer.type!!.contains("video")){
            try {
                mpw_video_player.visibility = View.VISIBLE
                photo_view.visibility = View.GONE
                mpw_video_player.setVideoURI(Uri.parse(customer.url))
                mpw_video_player.start()
            } catch(ex: Exception) {
                Log.i ("aa",ex.localizedMessage)
            }
        }else{
            mpw_video_player.visibility = View.GONE
            photo_view.visibility = View.VISIBLE
            photo_view.loadSvgOrOthers(customer.url)
        }
    }

    fun setupData(view:View){
        btnBackArrow = view.findViewById(R.id.btnBackArrow)
        txtToolbarTitle = view.findViewById(R.id.txtToolbarTitle)
        badge2 = view.findViewById(R.id.badge2)
        txtToolbarTitle.text = getString(R.string.profile)
        badge2.visibility = View.GONE
        btnBackArrow.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_arrow_back_ios_24))
        btnBackArrow.setOnClickListener {
            Navigation.findNavController(view).navigateUp()
        }
    }

}