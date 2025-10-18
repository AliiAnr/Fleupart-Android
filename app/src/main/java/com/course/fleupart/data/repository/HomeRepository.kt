package com.course.fleupart.data.repository

import android.content.Context
import com.course.fleupart.data.model.remote.StoreDetailData
import com.course.fleupart.data.model.remote.StoreDetailResponse
import com.course.fleupart.data.model.remote.StoreProductResponse
import com.course.fleupart.data.store.DataStoreManager
import com.course.fleupart.retrofit.api.ApiConfig
import com.course.fleupart.ui.common.ResultResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class HomeRepository private constructor(
    context: Context
) {

    private val homeService = ApiConfig.getHomeService(context)

    private val dataStoreManager = DataStoreManager(context)

    suspend fun getStoredStoreDetail(): StoreDetailData? = dataStoreManager.getStoreDetail()
    fun getAllStoreProduct(storeId: String): Flow<ResultResponse<StoreProductResponse>> = flow {
        emit(ResultResponse.Loading)

        try {
            val response = homeService.getAllStoreProduct(storeId = storeId)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(ResultResponse.Success(it))
                } ?: emit(ResultResponse.Error("Empty response body"))
            } else {
                emit(ResultResponse.Error("Error: ${response.errorBody()?.string() ?: "Unknown error"}"))
            }
        } catch (e: Exception) {
            emit(ResultResponse.Error(e.localizedMessage ?: "Error retrieving user details"))
        }
    }.flowOn(Dispatchers.IO)

    fun getStoreDetail(): Flow<ResultResponse<StoreDetailResponse>> = flow {
        emit(ResultResponse.Loading)
        try {
            val response = homeService.getStoreDetail()
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
        private var INSTANCE: HomeRepository? = null

        @JvmStatic
        fun getInstance(
            context: Context
        ): HomeRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: HomeRepository(context).also {
                    INSTANCE = it
                }
            }
    }
}