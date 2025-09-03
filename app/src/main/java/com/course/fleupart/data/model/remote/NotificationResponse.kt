package com.course.fleupart.data.model.remote

import com.google.gson.annotations.SerializedName

data class NotificationRequest(

    @field:SerializedName("token")
    val token: String,

    )

data class NotificationResponse(

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("statusCode")
    val statusCode: Int,

    @field:SerializedName("timestamp")
    val timestamp: String
)
