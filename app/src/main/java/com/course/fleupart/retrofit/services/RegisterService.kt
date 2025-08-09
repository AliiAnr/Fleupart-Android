package com.course.fleupart.retrofit.services

import com.course.fleupart.data.model.remote.OtpRequest
import com.course.fleupart.data.model.remote.OtpResponse
import com.course.fleupart.data.model.remote.RegisterRequest
import com.course.fleupart.data.model.remote.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterService {
    @POST("api/seller/register")
    suspend fun registerSeller(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("api/seller/auth/otp/generate")
    suspend fun generateOtp(@Body request: OtpRequest): Response<OtpResponse>
}

