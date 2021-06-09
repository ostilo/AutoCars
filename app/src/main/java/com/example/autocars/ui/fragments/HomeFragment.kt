package com.example.autocars.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import com.example.autocars.R
import com.example.autocars.core.ApiInterface
import com.example.autocars.db.adapter.MainListAdapter
import com.example.autocars.db.adapter.MainListSearchAdapter
import com.example.autocars.db.entities.Favorites
import com.example.autocars.db.entities.Messanger
import com.example.autocars.db.entities.PopularList
import com.example.autocars.db.entities.PopularMakes
import com.example.autocars.db.viewmodel.MainViewModel
import com.example.autocars.db.viewmodel.MainViewModelFactory
import com.example.autocars.ui.base.BaseFragment
import com.example.autocars.ui.base.ICarSearchCallback
import com.example.autocars.ui.base.Util
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeFragment : BaseFragment(), ICarSearchCallback {

    lateinit var  paCarsLoader : RecyclerView;
    lateinit var  mainListAdapter : MainListAdapter
    lateinit var  mainListSearchAdapter : MainListSearchAdapter
    lateinit var paCarSearch : RecyclerView
    private  lateinit var viewModel1 : MainViewModel
    lateinit var searchBtn : ImageView
    lateinit var progressLoad : ProgressBar
    lateinit var badgeNotification : TextView
    lateinit var noLayoutRecord : LinearLayout
    lateinit var mainParentHome : LinearLayout

    //main_parent_home

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Util.hideKeyboard(requireActivity())
        setupViewModel()
        setupList(view)
        setupView()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    private fun setupView() {

        lifecycleScope.launch {
            viewModel1.listData.collect {
                if(it != PagingData.empty<PopularMakes>()){
                    mainParentHome.visibility = View.VISIBLE
                    noLayoutRecord.visibility = View.GONE
                }
                mainListAdapter.submitData(it)
           }
            }

        lifecycleScope.launch {
            viewModel1.listDataSearch.collect {
                if(it != PagingData.empty<PopularList>()){
                    mainParentHome.visibility = View.VISIBLE
                    noLayoutRecord.visibility = View.GONE
                }else{
                    mainParentHome.visibility = View.GONE
                    noLayoutRecord.visibility = View.VISIBLE
                   super.toastError(getString(R.string.internet))
                }
                mainListSearchAdapter.submitData(it)
            }
        }

        viewModel1.counter.observe(viewLifecycleOwner, Observer {
            badgeNotification.text = it
        })
        }

    private fun setupList(view: View) {
        badgeNotification = view.findViewById(R.id.badge_notification_2)
        val editText : EditText  = view.findViewById(R.id.searchRef)
        progressLoad = view.findViewById(R.id.progress_load)
        noLayoutRecord = view.findViewById(R.id.noLayoutRecord)
        mainParentHome = view.findViewById(R.id.main_parent_home)
        editText.isFocusableInTouchMode = false
        editText.isFocusable = false
        editText.setOnClickListener {
           editText.isFocusableInTouchMode = true
           editText.isFocusable = true
       }

        mainListAdapter = MainListAdapter()
        searchBtn = view.findViewById(R.id.search_img)
        paCarsLoader = view.findViewById(R.id.addImageScrollView)
        mainListSearchAdapter = MainListSearchAdapter(this)
        paCarSearch = view.findViewById(R.id.addImageScrollView_card)
        paCarsLoader.apply {
            adapter = mainListAdapter
        }
        paCarSearch.apply {
            adapter = mainListSearchAdapter
        }

        searchBtn.setOnClickListener {
            progressLoad.visibility = View.VISIBLE
             lifecycleScope.launch {
                viewModel1.queryCars(editText.text.toString().trim()).collect {
                   try {
                       if(it != PagingData.empty<PopularList>()){
                         super.toastSuccess(getString(R.string.welcome))
                       }else{
                         super.toastError(getString(R.string.internet))
                       }
                       progressLoad.visibility = View.GONE
                        mainListSearchAdapter.submitData(it)
                   }catch (Ex: Exception){
                      super.toastError(Ex.localizedMessage)
                   }
                }
            }

        }
    }

    private fun setupViewModel() {
        viewModel1 =
                ViewModelProvider(
                        this,
                        MainViewModelFactory(ApiInterface.getApiService(),requireActivity().application)
                )[MainViewModel::class.java]
    }

    override fun passSearchLink(video: PopularList?, position: Int) {
        var message  = Messanger(video?.id!!, video.imageUrl!!)
        when(position){
            1 -> gotoDetails(message)
            2 -> addToRoom(video)
            3 -> deleteFromDB(video)
        }
       }

    private fun addToRoom(video: PopularList?) {
        viewModel1.addVideoToDB(video)
    }

    private fun gotoDetails(video: Messanger) {
        var bundle = Bundle()
        bundle.putParcelable(getString(R.string.messenger_key),video)
        //bundle.putParcelable(getString(R.string.search_car),video)
        NavHostFragment.findNavController(this).navigate(R.id.action_homeFragment_to_carsDetial,bundle)
    }

    private fun deleteFromDB(video:PopularList?){
        var msg = Favorites(video?.id!!, video.title!!,video.imageUrl!!,video.year!!,video.city!!,video.state!!,video.marketplacePrice)
        viewModel1.deleteFromDB(msg)
    }
}