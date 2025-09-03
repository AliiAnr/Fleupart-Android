package com.course.fleupart.retrofit.services

import com.course.fleupart.data.model.remote.GetUserResponse
import com.course.fleupart.data.model.remote.LoginRequest
import com.course.fleupart.data.model.remote.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface LoginService {
    @POST("api/seller/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("api/seller/profile")
    suspend fun getUser(): Response<GetUserResponse>
//
//    @PUT("api/buyer/username")
//    suspend fun setUsername(@Body username: NameRequest): Response<PersonalizeResponse>
}
