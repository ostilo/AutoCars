package com.example.autocars.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import com.example.autocars.R
import com.example.autocars.core.ApiInterface
import com.example.autocars.db.adapter.FavoritesMainAdapter
import com.example.autocars.db.entities.Favorites
import com.example.autocars.db.entities.Messanger
import com.example.autocars.db.entities.PopularList
import com.example.autocars.db.viewmodel.MainViewModel
import com.example.autocars.db.viewmodel.MainViewModelFactory
import com.example.autocars.ui.base.BaseFragment
import com.example.autocars.ui.base.IFavoriteCallback
import kotlinx.android.synthetic.main.custom_tollbar.*

class FavoriteFragment : BaseFragment(), IFavoriteCallback {
    private lateinit var  btnBackArrow : ImageView
    private lateinit var badge2 : RelativeLayout
    private lateinit var addImageScrollView : RecyclerView
    private lateinit var favAdapter : FavoritesMainAdapter
    private  lateinit var viewModel1 : MainViewModel
    lateinit var badgeNotification : TextView
    private lateinit var noLayoutRecord : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView(view)
        setupViewModel()
        initializeView()
    }

    private fun initializeView() {
        viewModel1.fetchFavorites().observe(viewLifecycleOwner, Observer{

            registerView(it)

        })

    }

    private fun registerView(it: List<Favorites>?) {
        if(it!!.isEmpty()){
            addImageScrollView.visibility = View.GONE
            noLayoutRecord.visibility = View.VISIBLE
        }else{
            noLayoutRecord.visibility = View.GONE
            addImageScrollView.visibility = View.VISIBLE
        }
        badgeNotification.text = it.size.toString()
        favAdapter = FavoritesMainAdapter(it,this)
        addImageScrollView.apply {
            adapter = favAdapter
        }
    }

    private fun setUpView(view: View) {
        badgeNotification = view.findViewById(R.id.badge_notification_2)
        btnBackArrow = view.findViewById(R.id.btnBackArrow)
        badge2 = view.findViewById(R.id.badge2)
        txtToolbarTitle.text = getString(R.string.favor)
        btnBackArrow.visibility = View.GONE
        addImageScrollView = view.findViewById(R.id.addImageScrollView_card)
        noLayoutRecord = view.findViewById(R.id.noLayoutRecord)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    private fun setupViewModel() {
        viewModel1 =
                ViewModelProvider(
                        this,
                        MainViewModelFactory(ApiInterface.getApiService(),requireActivity().application)
                )[MainViewModel::class.java]
    }


    private fun deleteFromDB(video: Favorites?) {
        viewModel1.deleteFromDB(video)
    }

    private fun showCarDetails(video: Favorites?) {
        var message  = Messanger(video!!.id,video.imageUrl)
        var bundle = Bundle()
        bundle.putParcelable(getString(R.string.messenger_key),message)
        NavHostFragment.findNavController(this).navigate(R.id.action_favoriteFragment_to_carsDetial,bundle)
    }

    override fun passSearchLink1(video: Favorites?, position: Int) {
        when(position){
            1 -> {
                showCarDetails(video)
            }
            2 -> {
                deleteFromDB(video)
            }
        }
    }

}