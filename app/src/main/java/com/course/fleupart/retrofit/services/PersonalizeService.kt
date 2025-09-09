package com.course.fleupart.retrofit.services

import com.course.fleupart.data.model.remote.AddressDataRequest
import com.course.fleupart.data.model.remote.CitizenDataRequest
import com.course.fleupart.data.model.remote.GetUserAddressResponse
import com.course.fleupart.data.model.remote.PersonalizeResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part

interface PersonalizeService {

    @GET("api/seller/address")
    suspend fun getAddressData(): Response<GetUserAddressResponse>

    @PUT("api/seller/update")
    suspend fun inputCitizenData(@Body citizenData: CitizenDataRequest): Response<PersonalizeResponse>

    @PUT("api/seller/address")
    suspend fun inputAddressData(@Body addressData: AddressDataRequest): Response<PersonalizeResponse>

    @Multipart
    @PUT("api/seller/picture")
    suspend fun inputSelfPicture(
        @Part file: MultipartBody.Part
    ): Response<PersonalizeResponse>

    @Multipart
    @PUT("api/seller/identity")
    suspend fun inputCitizenPicture(
        @Part file: MultipartBody.Part
    ): Response<PersonalizeResponse>
}