package com.example.mealmindle

import com.example.mealmindle.data.ProductResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("api/food-database/v2/parser")
    fun getProductDetails(
        @Query("upc") upc: String,
        @Query("app_id") appId: String,
        @Query("app_key") appKey: String
    ): Call<ProductResponse>

    companion object {
        const val BASE_URL = "https://api.edamam.com/"

        val instance: ApiService by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}