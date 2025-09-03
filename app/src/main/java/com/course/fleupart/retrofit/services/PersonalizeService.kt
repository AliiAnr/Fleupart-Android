package com.course.fleupart.retrofit.services

import com.course.fleupart.data.model.remote.AddressDataRequest
import com.course.fleupart.data.model.remote.CitizenDataRequest
import com.course.fleupart.data.model.remote.PersonalizeResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PUT

interface PersonalizeService {

    @PUT("api/seller/update")
    suspend fun inputCitizenData(@Body citizenData: CitizenDataRequest): Response<PersonalizeResponse>

    @PUT("api/seller/address")
    suspend fun inputAddressData(@Body addressData: AddressDataRequest): Response<PersonalizeResponse>
}