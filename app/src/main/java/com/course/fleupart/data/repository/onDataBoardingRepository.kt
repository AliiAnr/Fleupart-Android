package com.course.fleupart.data.repository

import android.content.Context
import com.course.fleupart.data.model.remote.CitizenDataRequest
import com.course.fleupart.data.model.remote.PersonalizeResponse
import com.course.fleupart.retrofit.api.ApiConfig
import com.course.fleupart.retrofit.services.PersonalizeService
import com.course.fleupart.ui.common.ResultResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class OnDataBoardingRepository private constructor(
    context: Context
) {

    private val personalizeService: PersonalizeService = ApiConfig.getPersonalizeService(context)

    fun inputCitizenData(citizenData: CitizenDataRequest): Flow<ResultResponse<PersonalizeResponse>> = flow {
        emit(ResultResponse.Loading)

        try {
            val response = personalizeService.inputCitizenData(
                citizenData = citizenData
            )
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(ResultResponse.Success(it))
                } ?: emit(ResultResponse.Error("Failed to update citizen data, response body empty"))
            } else {
                emit(ResultResponse.Error("Error: ${response.errorBody()?.string() ?: "Unknown error"}"))
            }
        } catch (e: Exception) {
            emit(ResultResponse.Error(e.localizedMessage ?: "Network error"))
        }
    }.flowOn(Dispatchers.IO)

    companion object {
        @Volatile
        private var INSTANCE: OnDataBoardingRepository? = null

        @JvmStatic
        fun getInstance(
            context: Context
        ): OnDataBoardingRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: OnDataBoardingRepository(context).also {
                    INSTANCE = it
                }
            }
    }
}