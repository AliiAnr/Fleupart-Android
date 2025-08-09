package com.course.fleupart.retrofit.services

import com.course.fleupart.data.model.remote.VerifyOtpRequest
import com.course.fleupart.data.model.remote.VerifyOtpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface OtpService {

    @POST("api/seller/auth/otp/verify")
    suspend fun verifyOtp(@Body request: VerifyOtpRequest): Response<VerifyOtpResponse>

}

