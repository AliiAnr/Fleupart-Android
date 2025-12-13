package com.course.fleupart.retrofit.services

import com.course.fleupart.data.model.remote.AddressDataRequest
import com.course.fleupart.data.model.remote.GetAllCategoryResponse
import com.course.fleupart.data.model.remote.PersonalizeResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

interface ProductService {

    @POST("api/product")
    suspend fun createProduct(@Body addressData: AddressDataRequest): Response<PersonalizeResponse>

    @Multipart
    @POST("api/product/with-category")
    suspend fun createProductWithCategory(
        @Part("name") name: RequestBody,
        @Part("stock") stock: RequestBody,
        @Part("description") description: RequestBody,
        @Part("arrange_time") arrangeTime: RequestBody,
        @Part("point") point: RequestBody,
        @Part("price") price: RequestBody,
        @Part("pre_order") isPreOrder: RequestBody,
        @Part("category_id") categoryId: RequestBody,
        @Part files: List<MultipartBody.Part>
    ): Response<PersonalizeResponse>

    @GET("api/category")
    suspend fun getAllCategory(): Response<GetAllCategoryResponse>
}