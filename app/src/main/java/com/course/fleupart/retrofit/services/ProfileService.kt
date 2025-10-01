package com.course.fleupart.retrofit.services

import com.course.fleupart.data.model.remote.ApiUpdateResponse
import com.course.fleupart.data.model.remote.StoreAddressResponse
import com.course.fleupart.data.model.remote.StoreDetailResponse
import com.course.fleupart.data.model.remote.UpdateStoreAddressRequest
import com.course.fleupart.data.model.remote.UpdateStoreDetailRequest
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part

interface ProfileService {

    @GET("api/store/detail")
    suspend fun getStoreDetail(): Response<StoreDetailResponse>

    @GET("api/store/address")
    suspend fun getStoreAddress(): Response<StoreAddressResponse>

    @PUT("api/store/address")
    suspend fun updateStoreAddress(@Body request: UpdateStoreAddressRequest): Response<ApiUpdateResponse>

    @PUT("api/store")
    suspend fun updateStoreDetail(@Body request: UpdateStoreDetailRequest): Response<ApiUpdateResponse>

    @Multipart
    @PUT("api/store/logo")
    suspend fun updateStoreLogo(
        @Part file: MultipartBody.Part
    ): Response<ApiUpdateResponse>

    @Multipart
    @PUT("api/store/picture")
    suspend fun updateStoreBanner(
        @Part file: MultipartBody.Part
    ): Response<ApiUpdateResponse>

}