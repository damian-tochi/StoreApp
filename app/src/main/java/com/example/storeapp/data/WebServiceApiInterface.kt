package com.example.storeapp.data

import com.example.storeapp.config.AppConstants
import com.example.storeapp.data.models.Product
import retrofit2.Call
import retrofit2.http.*

/** API call interface **/
interface WebServiceApiInterface {

    @GET(AppConstants.PRODUCTS)
    fun getProducts(): Call<List<Product>>

}
