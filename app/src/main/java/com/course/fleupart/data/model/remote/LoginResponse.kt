package com.course.fleupart.data.model.remote


import com.google.gson.annotations.SerializedName

data class LoginRequest(

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("password")
    val password: String
)

data class UpdateStoreRequest(
    @field:SerializedName("name")
    val name: String,
)

data class LoginResponse(

    @field:SerializedName("data")
    val data: Data,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("statusCode")
    val statusCode: Int,

    @field:SerializedName("timestamp")
    val timestamp: String
)

data class Data(

    @field:SerializedName("access_token")
    val accessToken: String
)
