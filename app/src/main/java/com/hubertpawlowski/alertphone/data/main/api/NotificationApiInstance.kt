package com.hubertpawlowski.alertphone.data.main.api

import com.hubertpawlowski.alertphone.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object NotificationApiInstance {

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL_FCM)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: NotificationApi by lazy {
        retrofit.create(NotificationApi::class.java)
    }
}
