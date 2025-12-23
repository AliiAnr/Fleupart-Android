package com.course.fleupart.data.repository

import android.content.Context
import android.util.Log
import co.yml.charts.common.extensions.isNotNull
import com.course.fleupart.data.model.remote.ApiUpdateResponse
import com.course.fleupart.data.model.remote.StoreAddressResponse
import com.course.fleupart.data.model.remote.StoreBannerRequest
import com.course.fleupart.data.model.remote.StoreDetailData
import com.course.fleupart.data.model.remote.StoreDetailResponse
import com.course.fleupart.data.model.remote.StoreLogoRequest
import com.course.fleupart.data.model.remote.StoreProductsResponse
import com.course.fleupart.data.model.remote.UpdateStoreAddressRequest
import com.course.fleupart.data.model.remote.UpdateStoreDetailRequest
import com.course.fleupart.data.store.DataStoreManager
import com.course.fleupart.retrofit.api.ApiConfig
import com.course.fleupart.ui.common.ResultResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

class ProfileRepository private constructor(
    context: Context
) {

    private val profileService = ApiConfig.getProfileService(context)

    private val dataStoreManager = DataStoreManager(context)

    fun getStoreDetail(): Flow<ResultResponse<StoreDetailResponse>> = flow {
        emit(ResultResponse.Loading)
        try {
            val response = profileService.getStoreDetail()
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

    suspend fun getStoredStoreDetail(): StoreDetailData? = dataStoreManager.getStoreDetail()

    suspend fun saveStoreDetail(storeDetail: StoreDetailData) {
        dataStoreManager.saveStoreDetail(storeDetail)
    }

    suspend fun logout() {
        dataStoreManager.clearUserDataExceptOnboarding()
    }

    fun getStoreProducts(storeId: String): Flow<ResultResponse<StoreProductsResponse>> = flow {
        emit(ResultResponse.Loading)
        try {
            val response = profileService.getStoreProducts(storeId = storeId)
            if (response.isSuccessful) {
                response.body()?.let { productResponse ->
                    Log.d("ProfileRepositoryOrder", "Response body: $productResponse")

                    emit(ResultResponse.Success(productResponse))
                } ?: emit(ResultResponse.Error("Empty response body"))
            } else {
                emit(ResultResponse.Error("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(ResultResponse.Error("Exception: ${e.message}"))
        }
    }.flowOn(Dispatchers.IO)

    fun getStoreAddress(): Flow<ResultResponse<StoreAddressResponse>> = flow {
        emit(ResultResponse.Loading)
        try {
            val response = profileService.getStoreAddress()
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

    fun updateStoreAddress(requestBody: UpdateStoreAddressRequest): Flow<ResultResponse<ApiUpdateResponse>> =
        flow {
            emit(ResultResponse.Loading)
            try {
                val response = profileService.updateStoreAddress(requestBody)
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


    fun updateStoreDetail(requestBody: UpdateStoreDetailRequest): Flow<ResultResponse<ApiUpdateResponse>> =
        flow {
            emit(ResultResponse.Loading)
            try {
                val response = profileService.updateStoreDetail(requestBody)
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

    fun uploadStoreLogo(storeLogo: StoreLogoRequest): Flow<ResultResponse<ApiUpdateResponse>> =
        flow {
            emit(ResultResponse.Loading)

            try {
                val filePart = MultipartBody.Part.createFormData(
                    "file",
                    storeLogo.picture.name,
                    storeLogo.picture.asRequestBody("image/jpeg".toMediaTypeOrNull())
                )

                val response = profileService.updateStoreLogo(file = filePart)
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

    fun uploadStoreBanner(storeBanner: StoreBannerRequest): Flow<ResultResponse<ApiUpdateResponse>> =
        flow {
            emit(ResultResponse.Loading)

            try {
                val filePart = MultipartBody.Part.createFormData(
                    "file",
                    storeBanner.picture.name,
                    storeBanner.picture.asRequestBody("image/jpeg".toMediaTypeOrNull())
                )

                val response = profileService.updateStoreBanner(file = filePart)
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


    companion object {
        @Volatile
        private var INSTANCE: ProfileRepository? = null

        fun getInstance(context: Context): ProfileRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ProfileRepository(context).also { INSTANCE = it }
            }
    }
}