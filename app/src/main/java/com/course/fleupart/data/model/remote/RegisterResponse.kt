package com.course.fleupart.data.model.remote

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @field:SerializedName("email")
    val email: String,
    @field:SerializedName("password")
    val password: String,
)

data class RegisterResponse(

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("statusCode")
    val statusCode: Int,

    @field:SerializedName("timestamp")
    val timestamp: String
)