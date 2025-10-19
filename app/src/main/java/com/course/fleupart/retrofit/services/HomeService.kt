package com.course.fleupart.retrofit.services

import com.course.fleupart.data.model.remote.ProductReviewResponse
import com.course.fleupart.data.model.remote.StoreDetailResponse
import com.course.fleupart.data.model.remote.StoreProductResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface HomeService {

    @GET("api/product/store/{storeId}")
    suspend fun getAllStoreProduct(@Path("storeId") storeId: String): Response<StoreProductResponse>

    @GET("api/store/detail")
    suspend fun getStoreDetail(): Response<StoreDetailResponse>

    @GET("api/product/review/product/{productId}")
    suspend fun getProductReview(@Path("productId") productId: String): Response<ProductReviewResponse>

}
