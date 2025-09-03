package com.course.fleupart.retrofit.services

import com.course.fleupart.data.model.remote.NotificationRequest
import com.course.fleupart.data.model.remote.NotificationResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface NotificationService {

    @POST("api/notification/seller/save-token")
    suspend fun saveToken(
        @Body token: NotificationRequest,
    ): Response<NotificationResponse>

    @POST("api/notification/seller/send")
    suspend fun sendNotif(
    ): Response<NotificationResponse>

}