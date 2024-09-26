package com.example.storeapp.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.storeapp.config.AppConstants.Companion.BASE_URL
import com.example.storeapp.data.models.Product
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/** API controller leveraging Retrofit, OkHTTP interceptor **/
abstract class RetrofitController{

    companion object {
        private var mRetrofit: Retrofit? = null

        private fun createRetrofit(context: Context): Retrofit {
            mRetrofit = getInstance(context)
            return mRetrofit as Retrofit
        }

        private fun getApi(context: Context): WebServiceApiInterface {
            return createRetrofit(context).create(WebServiceApiInterface::class.java)
        }

        private fun getInstance(context: Context) : Retrofit {

            val gson = GsonBuilder().setLenient().create()

            if (mRetrofit == null) {
                return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(getOkHttpClientBuilder(context).build())
                    .build()
            }

            return mRetrofit as Retrofit
        }

        /** Get products repository **/
        fun getAllProducts(registrationCategoryCallBack: Callback<List<Product>>, context: Context) {
            val call = getApi(context).getProducts()
            call.enqueue(registrationCategoryCallBack)
        }

    }
}

/** Interceptor builder **/
fun getOkHttpClientBuilder(context: Context) : OkHttpClient.Builder {
    val cacheSize = (5 * 1024 * 1024).toLong()
    val myCache = Cache(context.cacheDir, cacheSize)

    val client = OkHttpClient.Builder()
    client.cache(myCache)
    client.addNetworkInterceptor(CacheIntercept())
    client.addInterceptor { chain ->
        var request = chain.request()
        request = if (hasNetwork(context))
            request
                .newBuilder()
                .cacheControl(
                    CacheControl.Builder()
                        .maxAge(30, TimeUnit.MINUTES)
                        .build()
                )
                .build()
        else
            request
                .newBuilder()
                .cacheControl(
                    CacheControl.FORCE_CACHE
                )
                .build()
        chain.proceed(request)
    }
    client.addInterceptor(RetryInterceptor(context))
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY
    client.addInterceptor(interceptor)
    client.addInterceptor(Interceptor { chain ->
        val original = chain.request()
        val requestBuilder = original.newBuilder()
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json")
        val request = requestBuilder.build()
        chain.proceed(request)
    })


    return client
}

/** Is network available **/
fun hasNetwork(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val mNetwork      = connectivityManager.activeNetwork ?: return false
    val actNetwork  = connectivityManager.getNetworkCapabilities(mNetwork) ?: return false
    return when {
        actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
        else -> false
    }
}