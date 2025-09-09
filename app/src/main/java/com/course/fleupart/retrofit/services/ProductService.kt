package com.course.fleupart.retrofit.services

import com.course.fleupart.data.model.remote.AddressDataRequest
import com.course.fleupart.data.model.remote.PersonalizeResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface ProductService {

    @POST("api/product")
    suspend fun createProduct(@Body addressData: AddressDataRequest): Response<PersonalizeResponse>
}