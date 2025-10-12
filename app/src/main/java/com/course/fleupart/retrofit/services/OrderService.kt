package com.course.fleupart.retrofit.services

import com.course.fleupart.data.model.remote.ApiUpdateResponse
import com.course.fleupart.data.model.remote.OrderListResponse
import com.course.fleupart.data.model.remote.OrderStatusRequest
import com.course.fleupart.data.model.remote.StoreDetailResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface OrderService {
    @GET("api/store/detail")
    suspend fun getStoreDetail(): Response<StoreDetailResponse>

    @GET("api/order/store/{storeId}")
    suspend fun getStoreOrders(@Path("storeId") storeId: String): Response<OrderListResponse>

    @PUT("api/order/status/{orderId}")
    suspend fun updateOrderStatus(@Path("orderId") orderId: String, @Body orderStatusRequest: OrderStatusRequest): Response<ApiUpdateResponse>

}

