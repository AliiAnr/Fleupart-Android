package com.course.fleupart.data.repository

import android.content.Context
import android.util.Log
import com.course.fleupart.data.model.remote.NotificationRequest
import com.course.fleupart.data.model.remote.NotificationResponse
import com.course.fleupart.data.store.DataStoreManager
import com.course.fleupart.retrofit.api.ApiConfig
import com.course.fleupart.ui.common.ResultResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


class NotificationRepository private constructor(
    context: Context
) {

    private val notificationService = ApiConfig.getNotificationService(context)
    private val dataStoreManager = DataStoreManager(context)


//    fun saveToken(token: String): Flow<ResultResponse<NotificationResponse>> = flow {
//        emit(ResultResponse.Loading)
//
//        try {
//            if (!dataStoreManager.isUserLoggedIn.first()) {
//                emit(ResultResponse.Error("User not logged in"))
//                return@flow
//            }
//
//            val userToken = NotificationRequest(
//                token = token,
//            )
//
//            val response = notificationService.saveToken(token = userToken)
//            if (response.isSuccessful) {
//                response.body()?.let {
//                    emit(ResultResponse.Success(it))
//                } ?: emit(ResultResponse.Error("Empty response body"))
//            } else {
//                emit(ResultResponse.Error("Error: ${response.errorBody()?.string() ?: "Unknown error"}"))
//            }
//        } catch (e: Exception) {
//            emit(ResultResponse.Error(e.localizedMessage ?: "Error saving token"))
//        }
//    }.flowOn(Dispatchers.IO)

    fun saveToken(token: String): Flow<ResultResponse<NotificationResponse>> = flow {
        emit(ResultResponse.Loading)
        try {
            // Log the token that we're trying to save
            Log.e("NotificationRepository", "Attempting to save FCM token: $token")

            if (!dataStoreManager.isUserLoggedIn.first()) {
                Log.e("NotificationRepository", "User not logged in, cannot save token")
                emit(ResultResponse.Error("User not logged in"))
                return@flow
            }

            val userToken = NotificationRequest(
                token = token,
            )

            Log.e("NotificationRepository", "Sending FCM token to backend API: $token")
            val response = notificationService.saveToken(token = userToken)

            if (response.isSuccessful) {
                response.body()?.let {
                    Log.e("NotificationRepository", "Token saved successfully: ${it.message}")
//                    notificationService.sendNotif()
                    emit(ResultResponse.Success(it))
                } ?: run {
                    Log.e("NotificationRepository", "Empty response body from server")
                    emit(ResultResponse.Error("Empty response body"))
                }
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Unknown error"
                Log.e("NotificationRepository", "Error saving token: $errorMsg")
                emit(ResultResponse.Error("Error: $errorMsg"))
            }
        } catch (e: Exception) {
            Log.e("NotificationRepository", "Exception while saving token", e)
            Log.e("NotificationRepository", "Exception message: ${e.message}")
            emit(ResultResponse.Error(e.localizedMessage ?: "Error saving token"))
        }
    }.flowOn(Dispatchers.IO)


    companion object {
        @Volatile
        private var INSTANCE: NotificationRepository? = null
        fun getInstance(context: Context): NotificationRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: NotificationRepository(context).also { INSTANCE = it }
            }
    }
}
