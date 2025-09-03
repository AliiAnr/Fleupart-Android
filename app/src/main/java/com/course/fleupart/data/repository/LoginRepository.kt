package com.course.fleupart.data.repository

import android.content.Context
import com.course.fleupart.data.model.remote.GetUserResponse
import com.course.fleupart.data.model.remote.LoginRequest
import com.course.fleupart.data.model.remote.LoginResponse
import com.course.fleupart.data.model.remote.PersonalizeResponse
import com.course.fleupart.data.store.DataStoreManager
import com.course.fleupart.retrofit.api.ApiConfig
import com.course.fleupart.ui.common.ResultResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class LoginRepository private constructor(
    context: Context
) {

    private val dataStoreManager = DataStoreManager(context)
    private val loginService = ApiConfig.getLoginService(context)

    fun loginUser(email: String, password: String): Flow<ResultResponse<LoginResponse>> = flow {
        emit(ResultResponse.Loading)

        try {
            val response = loginService.login(LoginRequest(email, password))
            if (response.isSuccessful) {
                response.body()?.let {
                    dataStoreManager.saveUserToken(it.data.accessToken)
                    dataStoreManager.setUserLoggedIn(true)
                    emit(ResultResponse.Success(it))
                } ?: emit(ResultResponse.Error("Empty response body"))
            } else {
                emit(ResultResponse.Error("Error: ${response.errorBody()?.string() ?: "Unknown error"}"))
            }
        } catch (e: Exception) {
            emit(ResultResponse.Error(e.localizedMessage ?: "Network error"))
        }

    }.flowOn(Dispatchers.IO)

    fun getUser(): Flow<ResultResponse<GetUserResponse>> = flow {
        emit(ResultResponse.Loading)

        try {
            val response = loginService.getUser()
            if (response.isSuccessful) {
                response.body()?.let {
                    dataStoreManager.saveUserDetail(it.data)
                    emit(ResultResponse.Success(it))
                } ?: emit(ResultResponse.Error("Empty response body"))
            } else {
                emit(ResultResponse.Error("Error: ${response.errorBody()?.string() ?: "Unknown error"}"))
            }
        } catch (e: Exception) {
            emit(ResultResponse.Error(e.localizedMessage ?: "Network error"))
        }
    }.flowOn(Dispatchers.IO)

//    fun setUsername(username: String): Flow<ResultResponse<PersonalizeResponse>> = flow {
//        emit(ResultResponse.Loading)
//
//        try {
//            val response = loginService.setUsername(NameRequest(username))
//            if (response.isSuccessful) {
//                response.body()?.let {
//                    emit(ResultResponse.Success(it))
//                } ?: emit(ResultResponse.Error("Empty response body"))
//            } else {
//                emit(ResultResponse.Error("Error: ${response.errorBody()?.string() ?: "Unknown error"}"))
//            }
//        } catch (e: Exception) {
//            emit(ResultResponse.Error(e.localizedMessage ?: "Network error"))
//        }
//    }.flowOn(Dispatchers.IO)

    fun isUserLoggedIn(): Flow<Boolean> = dataStoreManager.isUserLoggedIn

    fun getUserToken(): Flow<String?> = dataStoreManager.userToken

    suspend fun setPersonalizeFilled() {
        dataStoreManager.setPersonalizedFilled(dataStoreManager.isPersonalizeCompleted())
    }

    fun isPersonalizeFilled() : Flow<Boolean> = dataStoreManager.personalizedFilled

    suspend fun logout() {
        dataStoreManager.clearUserData()
    }


    companion object {
        @Volatile
        private var INSTANCE: LoginRepository? = null

        @JvmStatic
        fun getInstance(
            context: Context
        ): LoginRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: LoginRepository(context).also {
                    INSTANCE = it
                }
            }
    }
}