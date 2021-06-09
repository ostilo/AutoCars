package com.example.autocars.db.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.autocars.core.ApiInterface
import com.example.autocars.core.AppDatabase
import com.example.autocars.core.FavoriteDao
import com.example.autocars.db.entities.*

class PostDataSource(private val apiService: ApiInterface) : PagingSource<Int, PopularMakes>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PopularMakes> {
        try {
            val currentLoadingPageKey = params.key ?: 1
            val response = apiService.getListData(currentLoadingPageKey)
            val responseData = mutableListOf<PopularMakes>()
            val data = response.body()?.makeList ?: emptyList()
            responseData.addAll(data)

            val prevKey = if (currentLoadingPageKey == 1) null else currentLoadingPageKey - 1

            return LoadResult.Page(
                    data = responseData,
                    prevKey = prevKey,
                    nextKey = currentLoadingPageKey.plus(1)
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PopularMakes>): Int? {
        return  state?.anchorPosition
    }
}

class PostDataSourceSearch(private val apiService: ApiInterface, private val stateValue: String, private val counter: MutableLiveData<String>) : PagingSource<Int, PopularList>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PopularList> {
        try {
            val currentLoadingPageKey = params.key ?: 0
            val response = apiService.getListDataSearch(currentLoadingPageKey,stateValue)
            val responseData = mutableListOf<PopularList>()
            val data = response.body()?.result ?: emptyList()
            counter.postValue(response.body()?.pagination!!.total.toString())
            responseData.addAll(data)
            val prevKey = if (currentLoadingPageKey == 0) null else currentLoadingPageKey - 1
            return LoadResult.Page(
                    data = responseData,
                    prevKey = prevKey,
                    nextKey = currentLoadingPageKey.plus(1)
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }

    }


    override fun getRefreshKey(state: PagingState<Int, PopularList>): Int? {
        return  state?.anchorPosition
    }
}


class PostDataSourceMedia(private val apiService: ApiInterface, private val valye: String?, private val carMedia : MutableLiveData<CarMedia>) : PagingSource<Int, CarMedia>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CarMedia> {
        try {
            val currentLoadingPageKey = params.key ?: 0
            val response = apiService.CarMedias(currentLoadingPageKey,valye)
            val responseData = mutableListOf<CarMedia>()
            val data = response.body()?.carMediaList ?: emptyList()
            carMedia.postValue(data[0])
            responseData.addAll(data)
            val prevKey = if (currentLoadingPageKey == 0) null else currentLoadingPageKey - 1

            return LoadResult.Page(
                    data = responseData,
                    prevKey = prevKey,
                    nextKey = currentLoadingPageKey.plus(1)
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }

    }

    override fun getRefreshKey(state: PagingState<Int, CarMedia>): Int? {
        return  state?.anchorPosition
    }
}

class MainRepository(private var context:Context){
    var db : FavoriteDao? = AppDatabase.getInstance(context)?.userDao()


    fun getAllFavorites(): LiveData<List<Favorites>> {
       return db!!.getAllFavorites()
    }

      fun deleteUser(users: Favorites) {
       db!!.delete(users.id!!)
    }

    fun composeView(carDetails: CarDetails):String{
        var hasWarranty = if(carDetails.hasWarranty!!)    "YES" else   "NO"
        var hasFinancing  = if(carDetails.hasFinancing!!)    "YES" else   "NO"
        var isFirstOwner  = if(carDetails.isFirstOwner!!)   "YES"  else "NO"
        var sold  =  if(carDetails.sold!!)  "YES"  else "NO"
        var msg = "Name : ${carDetails.model?.make?.name}\n" +
                " \n " +
                "Price : ${carDetails.price}\n" +
                " \n " +
                "Model : ${carDetails.model?.name}\n" +
                " \n " +
                "State : ${carDetails.state}\n" +
                " \n " +
                "Address : ${carDetails.address} \n" +
                " \n " +
                "OwnerType Name :  ${carDetails.ownerType}\n" +
                " \n " +
                "Transmission Name : ${carDetails.transmission}\n" +
                " \n " +
                "LicensePlate Name : ${carDetails.licensePlate}\n" +
                " \n " +
                "EngineNumber : ${carDetails.engineNumber}\n" +
                " \n " +
                "MarketplacePrice: ${carDetails.marketplacePrice}\n" +
                " \n " +
                "MarketplaceVisible: ${carDetails.marketplaceVisible}\n" +
                " \n " +
                "HasWarranty : ${hasWarranty}\n" +
                " \n " +
                "HasFinancing : ${hasFinancing}\n" +
                " \n " +
                "ExteriorColor: ${carDetails.exteriorColor}\n" +
                " \n " +
                "InteriorColor: ${carDetails.interiorColor}\n" +
                " \n " +
                "IsFirstOwner : ${isFirstOwner}\n" +
                " \n " +
                "InteriorColor: ${carDetails.interiorColor} \n" +
                " \n " +
                "Sold : ${sold}\n" +
                " \n " +
                "EngineType: ${carDetails.engineType}\n" +
                " \n " +
                "Model\n" +
                "----------------------\n " +
                "Name : ${carDetails.model?.name}\n" +
                " \n " +
                "WheelType: ${carDetails.model?.wheelType}\n" +
                " \n "
            return msg
    }

    suspend fun fetchCardetails(s: String?, carDetails: MutableLiveData<CarDetails>, apiService: ApiInterface) {
          try {
             val response  = apiService.fetchCarDetails(s)
              val data = response.body()
               carDetails.postValue(data)
          }catch (ex:java.lang.Exception){
         var ee = ex.localizedMessage;
              var tt = ee
          }
    }

    fun addVideoToDB(video: PopularList?) {
        var msg = Favorites(video?.id!!, video?.title!!,video?.imageUrl!!,video?.year!!,video?.city!!,video?.state!!,video.marketplacePrice)
        db!!.insert(msg)
    }



}









