package com.course.fleupart.data.repository

import android.content.Context
import android.util.Log
import com.course.fleupart.data.model.remote.FilteredOrdersData
import com.course.fleupart.data.model.remote.OrderDataItem
import com.course.fleupart.data.model.remote.OrderListResponse
import com.course.fleupart.data.model.remote.StoreDetailData
import com.course.fleupart.data.model.remote.StoreDetailResponse
import com.course.fleupart.data.store.DataStoreManager
import com.course.fleupart.retrofit.api.ApiConfig
import com.course.fleupart.ui.common.ResultResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class OrderRepository private constructor(
    context: Context
) {

    private val orderService = ApiConfig.getOrderService(context)
    private val dataStoreManager = DataStoreManager(context)

    suspend fun getStoredStoreDetail(): StoreDetailData? = dataStoreManager.getStoreDetail()

    fun getCachedStoreDetail(): Flow<StoreDetailData?> = dataStoreManager.storeDetailFlow

    fun getStoreOrders(storeId: String) : Flow<ResultResponse<OrderListResponse>> = flow {
        emit(ResultResponse.Loading)
        try {
            val response = orderService.getStoreOrders(storeId = storeId)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(ResultResponse.Success(it))
                } ?: emit(ResultResponse.Error("Empty response body"))
            } else {
                emit(ResultResponse.Error("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(ResultResponse.Error("Exception: ${e.message}"))
        }
    }.flowOn(Dispatchers.IO)

    fun getFilteredStoreOrders(storeId: String): Flow<ResultResponse<FilteredOrdersData>> = flow {
        emit(ResultResponse.Loading)
        try {
            val response = orderService.getStoreOrders(storeId = storeId)
            if (response.isSuccessful) {
                response.body()?.let { orderListResponse ->
                    Log.d("OrderRepository", "Response body: $orderListResponse")
                    val filteredData = filterOrdersByStoreAndStatus(orderListResponse.data, storeId)
                    emit(ResultResponse.Success(filteredData))
                } ?: emit(ResultResponse.Error("Empty response body"))
            } else {
                emit(ResultResponse.Error("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(ResultResponse.Error("Exception: ${e.message}"))
        }
    }.flowOn(Dispatchers.IO)

    private fun filterOrdersByStoreAndStatus(
        orders: List<OrderDataItem?>?,
        targetStoreId: String
    ): FilteredOrdersData {
        val storeOrders = orders?.filterNotNull()?.filter { order ->
            order.orderItems?.any { item ->
                item?.product?.store?.id == targetStoreId
            } == true
        } ?: emptyList()

        Log.d("OrderRepository", "Filtered orders for store $targetStoreId: $storeOrders")
        Log.d("OrderRepository", "Filtered orders for status created $targetStoreId: ${storeOrders.filter { it.status == "created" }}")
        Log.d("OrderRepository", "Filtered orders for status process $targetStoreId: ${storeOrders.filter { it.status == "process" }}")
        Log.d("OrderRepository", "Filtered orders for status pickup $targetStoreId: ${storeOrders.filter { it.status == "pickup" }}")
        Log.d("OrderRepository", "Filtered orders for status delivery $targetStoreId: ${storeOrders.filter { it.status == "delivery" }}")
        Log.d("OrderRepository", "Filtered orders for status completed $targetStoreId: ${storeOrders.filter { it.status == "completed" }}")

        return FilteredOrdersData(
            newOrders = storeOrders.filter { it.status == "created" },
            processOrders = storeOrders.filter { it.status == "process" },
            pickupOrders = storeOrders.filter { it.status == "pickup" },
            deliveryOrders = storeOrders.filter { it.status == "delivery" },
            completedOrders = storeOrders.filter { it.status == "completed" }
        )
    }

    fun getStoreDetail(): Flow<ResultResponse<StoreDetailResponse>> = flow {
        emit(ResultResponse.Loading)
        try {
            val response = orderService.getStoreDetail()
            if (response.isSuccessful) {
                response.body()?.let {
                    it.data?.let { storeDetail ->
                        dataStoreManager.saveStoreDetail(storeDetail)
                    }
                    emit(ResultResponse.Success(it))
                } ?: emit(ResultResponse.Error("Empty response body"))
            } else {
                emit(ResultResponse.Error("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(ResultResponse.Error("Exception: ${e.message}"))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun saveStoreDetail(storeDetail: StoreDetailData) {
        dataStoreManager.saveStoreDetail(storeDetail)
    }

    companion object {
        @Volatile
        private var INSTANCE: OrderRepository? = null

        @JvmStatic
        fun getInstance(
            context: Context
        ): OrderRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: OrderRepository(context).also {
                    INSTANCE = it
                }
            }
    }
}