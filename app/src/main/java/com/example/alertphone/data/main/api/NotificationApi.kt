package com.example.alertphone.data.main.api


import com.example.alertphone.Constants.CONTENT_TYPE
import com.example.alertphone.Constants.SERVER_KEY
import com.example.alertphone.data.main.model.PushNotification
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


interface NotificationApi {

    @Headers("Authorization: key=$SERVER_KEY", "Content-Type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(@Body notification: PushNotification): Response<ResponseBody>
}