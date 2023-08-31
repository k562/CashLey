package com.rewardpay.app.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestClient {

    private val BASE_URL = "http://165.232.185.199:4000/api/v1/"

    fun getRestService(): Retrofit {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttpClient: OkHttpClient =
            OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
        return retrofit
    }

    fun getApi() : RestService{
        return getRestService().create(RestService::class.java)
    }

}