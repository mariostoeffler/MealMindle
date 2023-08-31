package com.example.mealmindle.data.api

import com.example.mealmindle.data.ProductResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductApi {
    @GET("product/{barcode}.json")
    suspend fun getProductDetails(@Path("barcode") barcode: String): Response<ProductResponse>
}
