package com.example.autocars.ui.fragments

import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.autocars.R
import com.example.autocars.core.ApiInterface
import com.example.autocars.db.entities.CarDetails
import com.example.autocars.db.viewmodel.MainViewModel
import com.example.autocars.db.viewmodel.MainViewModelFactory

class CarDetailsFragment : Fragment() {
    private  lateinit var textDetails : TextView
    private lateinit var  btnBackArrow : ImageView
    private lateinit var txtToolbarTitle : TextView
    private lateinit var badge2 : RelativeLayout
    private lateinit var convertHtml : Spanned
    private  lateinit var viewModel1 : MainViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initiateView(view)
    }

    private fun initiateView(view: View) {
        btnBackArrow = view.findViewById(R.id.btnBackArrow)
        txtToolbarTitle = view.findViewById(R.id.txtToolbarTitle)
        badge2 = view.findViewById(R.id.badge2)
        txtToolbarTitle.text = getString(R.string.about)
        badge2.visibility = View.GONE
        btnBackArrow.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_arrow_back_ios_24))
        btnBackArrow.setOnClickListener {
            Navigation.findNavController(view).navigateUp()
        }
    }


    fun updateDetails(carDetails: CarDetails){
    var message = viewModel1.composeView(carDetails)
        textDetails.text = message
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun setupViewModel() {
        viewModel1 =
            ViewModelProvider(
                this,
                MainViewModelFactory(ApiInterface.getApiService(),requireActivity().application)
            )[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view  : View= inflater.inflate(R.layout.fragment_car_details, container, false)
        textDetails = view.findViewById(R.id.textDetails)
        setupViewModel()
        arguments?.let {
         var customer : CarDetails? = it.getParcelable(getString(R.string.card_key))
            if (customer != null) {
                updateDetails(customer)
            }
        }
        return view
    }
}