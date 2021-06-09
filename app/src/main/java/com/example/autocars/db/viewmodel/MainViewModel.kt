package com.example.autocars.db.viewmodel

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.*
import com.example.autocars.core.ApiInterface
import com.example.autocars.db.entities.CarDetails
import com.example.autocars.db.entities.CarMedia
import com.example.autocars.db.entities.Favorites
import com.example.autocars.db.entities.PopularList
import com.example.autocars.db.repository.*
import kotlinx.coroutines.launch


class MainViewModel(private val apiService: ApiInterface, application: Application) : AndroidViewModel(application) {

    val counter =  MutableLiveData<String>()
    val carDetails = MutableLiveData<CarDetails>()
    private val mProxy = MainRepository(application.applicationContext)
    val carMedia = MutableLiveData<CarMedia>()



    fun queryCars(s: String) : kotlinx.coroutines.flow.Flow<PagingData<PopularList>> {
     return  Pager(PagingConfig(pageSize = 12)) {
            PostDataSourceSearch(apiService, s, counter)
        }.flow.cachedIn(viewModelScope)
    }

    fun fetchCardetails(s: String?) {
        viewModelScope.launch {
            mProxy.fetchCardetails(s,carDetails,apiService)
        }

    }

    fun composeView(carDetails: CarDetails):String{
        return mProxy.composeView(carDetails)
    }


    fun fetchFavorites():LiveData<List<Favorites>>{
            return mProxy.getAllFavorites()
    }


    fun lisCarMedia(s: String?) : kotlinx.coroutines.flow.Flow<PagingData<CarMedia>> {
        return  Pager(PagingConfig(pageSize = 12)) {
            PostDataSourceMedia(apiService, s,carMedia)
        }.flow.cachedIn(viewModelScope)
    }

    fun deleteFromDB(video: Favorites?) {
          viewModelScope.launch {
              video?.let { mProxy.deleteUser(it) }
          }
    }

    fun addVideoToDB(video: PopularList?) {
  viewModelScope.launch {
      mProxy.addVideoToDB(video)
  }
    }


    val listDataSearch = Pager(PagingConfig(pageSize = 12)) {
        PostDataSourceSearch(apiService," ",counter)
    }.flow.cachedIn(viewModelScope)



    val listData = Pager(PagingConfig(pageSize = 12)) {
        PostDataSource(apiService)
    }.flow.cachedIn(viewModelScope)


}


class MainViewModelFactory(private val apiService: ApiInterface,private val application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(apiService,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}