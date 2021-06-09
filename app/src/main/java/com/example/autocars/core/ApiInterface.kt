package com.example.autocars.core

import com.example.autocars.db.entities.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiInterface {
//https://api-prod.autochek.africa/v1/inventory/car/pgHpI6GIH
    //https://api-prod.autochek.africa/v1/inventory/car/pgHpI6GIH
    @GET("make?popular=true")
    suspend fun getListData(@Query("pageNumber") pageNumber: Int): Response<PopularCarsResponse>

    @GET("car/search")
    suspend fun getListDataSearch(@Query("pageNumber") pageNumber: Int, @Query("state") state: String ): Response<PopularListSearch>


    @GET("car/{carId}")
    suspend fun fetchCarDetails(@Path("carId")  carId : String?): Response<CarDetails>

    @GET("car_media")
    suspend fun CarMedias(@Query("pageNumber") pageNumber: Int, @Query("carId") carID: String?): Response<CarMediaList>

    companion object {
        private val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
        fun getApiService() = Retrofit.Builder()
                .baseUrl("https://api-prod.autochek.africa/v1/inventory/")
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(ApiInterface::class.java)
    }



}