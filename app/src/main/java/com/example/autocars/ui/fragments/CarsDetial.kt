package com.example.autocars.ui.fragments

import android.graphics.Paint
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.bumptech.glide.Glide
import com.example.autocars.MainApplication
import com.example.autocars.R
import com.example.autocars.core.ApiInterface
import com.example.autocars.db.adapter.CarMediaListAdapter
import com.example.autocars.db.entities.CarDetails
import com.example.autocars.db.entities.CarMedia
import com.example.autocars.db.entities.Messanger
import com.example.autocars.db.entities.PopularList
import com.example.autocars.db.viewmodel.MainViewModel
import com.example.autocars.db.viewmodel.MainViewModelFactory
import com.example.autocars.ui.base.BaseFragment
import com.example.autocars.ui.base.ICarMediaCallback
import com.example.autocars.ui.base.Util
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CarsDetial : BaseFragment() , ICarMediaCallback {

    private lateinit var  btnBackArrow : ImageView
    private lateinit var txtToolbarTitle : TextView
    private lateinit var badge2 : RelativeLayout
    private lateinit var car_title : TextView
    private lateinit var carlogoicon : ImageView
    private lateinit var addImageScrollView : RecyclerView
    private lateinit var old_price : TextView
    private lateinit var newPrice : TextView
    private lateinit var btnBuy : MaterialButton
    private lateinit var layoutDetails : LinearLayout

    private lateinit var btnMinus : ImageView
    private lateinit var btnAdd : ImageView
    private lateinit var QuantityValue : TextView
    private lateinit var totalQuantity : TextView
    private lateinit var btnAddToCart : MaterialButton
    private  lateinit var viewModel1 : MainViewModel
    private lateinit var detailsProgress : ProgressBar
    var amountReal : Double = 0.0
    private lateinit var carDetails: CarDetails

    private lateinit var main_parent : LinearLayout
    private lateinit var mpwVideoPlayer : VideoView
    private lateinit var adapterCar : CarMediaListAdapter
    private lateinit var videoProgress : ProgressBar
    private var  stringCarId : PopularList? = null
    private var stringCarIDVal : Messanger? = null
    private  var currentImage : String? = ""
    private lateinit var carMedia: CarMedia


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        initiateView(view)
        setUpView()
    }

    private fun setUpView() {
        txtToolbarTitle.text = getString(R.string.profile)
        badge2.visibility = View.GONE
        btnBackArrow.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_arrow_back_ios_24))
        UpdateDetails()
    }

    private fun UpdateDetails() {
        arguments?.let{
            stringCarIDVal = it.getParcelable(getString(R.string.messenger_key))
        }
        viewModel1.fetchCardetails(stringCarIDVal!!.id)
        lifecycleScope.launch {
            viewModel1.lisCarMedia(stringCarIDVal!!.id).collect{
                adapterCar.submitData(it)
            }

        }

//
//        carlogoicon.visibility = View.VISIBLE
//        mpwVideoPlayer.visibility = View.GONE
//        videoProgress.visibility = View.GONE
//        currentImage = (stringCarIDVal?.imageUrl)
//
//        Glide.with(MainApplication.instance!!.applicationContext)
//                .load(currentImage)
//                .placeholder(R.drawable.carlogoicon)
//                .into(carlogoicon)


        carlogoicon.setOnClickListener{
            //Todo here convert all images to Bitmap
           // Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap()
            val bundle : Bundle = Bundle()
            bundle.putParcelable(getString(R.string.card_key_photo),carMedia)
            NavHostFragment.findNavController(this).navigate(R.id.action_carsDetial_to_photoViewFragment,bundle)
        }
    }



    private fun initiateView(view: View) {
        detailsProgress = view.findViewById(R.id.detailsProgress)
        btnBackArrow = view.findViewById(R.id.btnBackArrow)
        txtToolbarTitle = view.findViewById(R.id.txtToolbarTitle)
        car_title = view.findViewById(R.id.car_title)
        badge2 = view.findViewById(R.id.badge2)
        btnMinus = view.findViewById(R.id.btnMinus)
        btnAdd = view.findViewById(R.id.btnAdd)
        addImageScrollView = view.findViewById(R.id.addImageScrollView)
        videoProgress = view.findViewById(R.id.videoProgress)
        carlogoicon = view.findViewById(R.id.carlogoicon)
        QuantityValue = view.findViewById(R.id.QuantityValue)
        totalQuantity = view.findViewById(R.id.totalQuantity)
        btnAddToCart = view.findViewById(R.id.btnAddToCart)
        old_price = view.findViewById(R.id.old_price)
        btnBuy = view.findViewById(R.id.btnBuy)
        newPrice = view.findViewById(R.id.newPrice)
        layoutDetails = view.findViewById(R.id.layoutDetails)
        main_parent = view.findViewById(R.id.main_parent)
        main_parent.visibility = View.INVISIBLE
        mpwVideoPlayer = view.findViewById(R.id.mpw_video_player)
        adapterCar = CarMediaListAdapter(this)
        addImageScrollView.apply {
            adapter = adapterCar
        }
        btnBackArrow.setOnClickListener {
            Navigation.findNavController(view).navigateUp()
        }
        viewModel1.carDetails.observe(viewLifecycleOwner, Observer {
            updateView(it,view)
        })

        viewModel1.carMedia.observe(viewLifecycleOwner, Observer {
            carMedia = it
            updateRecord(it)
        })

           btnMinus.setOnClickListener {
            val qty = QuantityValue.text.toString()
            if(qty.toInt() > 0){
                QuantityValue.text = (QuantityValue.text.toString().toInt() - 1).toString()
                val amount = (QuantityValue.text.toString()).toInt()
                val amt = "Total: ${Util.getNairaUnitFormat((amount * amountReal).toString(), false)}"
                totalQuantity.text =  amt
            }
        }
        btnAdd.setOnClickListener {
            QuantityValue.text = (QuantityValue.text.toString().toInt() + 1).toString()
            val amount = (QuantityValue.text.toString()).toInt()
            val amt = "Total: ${Util.getNairaUnitFormat((amount * amountReal).toString(), false)}"
            totalQuantity.text =  amt
        }

    }


    private fun updateView(it: CarDetails?, view: View) {
        main_parent.visibility = View.VISIBLE
        carDetails = it!!
        detailsProgress.visibility = View.INVISIBLE
        car_title.text = it!!.model?.make?.name
        amountReal = it.price!!
        newPrice.text = Util.getNairaUnitFormat( it!!.price.toString(), false)
        old_price.apply {
            paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            text = Util.getNairaUnitFormat(it!!.marketplaceOldPrice.toString(), false)
        }

        btnBuy.text = Util.getNairaUnitFormat( it!!.price.toString(), false)
        layoutDetails.setOnClickListener(View.OnClickListener {
            val bundle : Bundle = Bundle()
            bundle.putParcelable(getString(R.string.card_key),carDetails)
            NavHostFragment.findNavController(this).navigate(R.id.carDetailsFragment,bundle)
        })
        QuantityValue.text = "0"

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_car_detail, container, false)
    }

    private fun setupViewModel() {
        viewModel1 =
                ViewModelProvider(
                        this,
                        MainViewModelFactory(ApiInterface.getApiService(),requireActivity().application)
                )[MainViewModel::class.java]
    }


    override fun onPause() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            mpwVideoPlayer.pause();
        }
        super.onPause()
    }

    override fun passVideoLink(video: CarMedia?, position: Int) {
        updateRecord(video)
    }

    private fun updateRecord(video: CarMedia?) {
        carMedia = video!!
            if(video != null){
                if(video.type!!.contains("video")){
                    try {
                        mpwVideoPlayer.visibility = View.VISIBLE
                        videoProgress.visibility = View.VISIBLE
                        carlogoicon.visibility = View.GONE
                        mpwVideoPlayer.setVideoURI(Uri.parse(video.url))
                        mpwVideoPlayer.start()
                        videoProgress.visibility = View.GONE
                    } catch(ex: Exception) {
                        Log.i ("aa",ex.localizedMessage)
                    }
                }else{
                    mpwVideoPlayer.visibility = View.GONE
                    videoProgress.visibility = View.GONE
                    carlogoicon.visibility = View.VISIBLE
                    currentImage = video.url
                    carlogoicon.load(video.url) {
                        placeholder(R.drawable.carlogoicon)
                    }
                }
            }
    }


}