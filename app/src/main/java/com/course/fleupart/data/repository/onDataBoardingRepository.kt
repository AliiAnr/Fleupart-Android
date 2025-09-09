package com.course.fleupart.data.repository

import android.content.Context
import com.course.fleupart.data.model.remote.AddressDataRequest
import com.course.fleupart.data.model.remote.CitizenDataRequest
import com.course.fleupart.data.model.remote.GetUserAddressResponse
import com.course.fleupart.data.model.remote.PersonalizeResponse
import com.course.fleupart.data.model.remote.UserCitizenPictureRequest
import com.course.fleupart.data.model.remote.UserSelfPictureRequest
import com.course.fleupart.retrofit.api.ApiConfig
import com.course.fleupart.retrofit.services.PersonalizeService
import com.course.fleupart.ui.common.ResultResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.http.Multipart

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

    fun inputAddressData(addressDataRequest: AddressDataRequest): Flow<ResultResponse<PersonalizeResponse>> = flow {
        emit(ResultResponse.Loading)

        try {
            val response = personalizeService.inputAddressData(
                addressData = addressDataRequest
            )
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(ResultResponse.Success(it))
                } ?: emit(ResultResponse.Error("Failed to update address data, response body empty"))
            } else {
                emit(ResultResponse.Error("Error: ${response.errorBody()?.string() ?: "Unknown error"}"))
            }
        } catch (e: Exception) {
            emit(ResultResponse.Error(e.localizedMessage ?: "Network error"))
        }
    }.flowOn(Dispatchers.IO)

    fun inputSelfPicture(selfPictureData: UserSelfPictureRequest): Flow<ResultResponse<PersonalizeResponse>> = flow {
        emit(ResultResponse.Loading)
        try {
            val filePart = MultipartBody.Part.createFormData(
                "file",
                selfPictureData.picture.name,
                selfPictureData.picture.asRequestBody("image/jpeg".toMediaTypeOrNull()) // Changed from "image/*"
            )

            val response = personalizeService.inputSelfPicture(file = filePart)
            if (response.isSuccessful) {
                response.body()?.let { emit(ResultResponse.Success(it)) }
                    ?: emit(ResultResponse.Error("Empty response"))
            } else {
                emit(ResultResponse.Error("Error: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            emit(ResultResponse.Error(e.localizedMessage ?: "Network error"))
        }
    }.flowOn(Dispatchers.IO)

    fun inputCitizenPicture(citizenPictureData: UserCitizenPictureRequest): Flow<ResultResponse<PersonalizeResponse>> = flow {
        emit(ResultResponse.Loading)
        try {
            val filePart = MultipartBody.Part.createFormData(
                "file",
                citizenPictureData.picture.name,
                citizenPictureData.picture.asRequestBody("image/jpeg".toMediaTypeOrNull()) // Changed from "image/*"
            )

            val response = personalizeService.inputCitizenPicture(file = filePart)
            if (response.isSuccessful) {
                response.body()?.let { emit(ResultResponse.Success(it)) }
                    ?: emit(ResultResponse.Error("Empty response"))
            } else {
                emit(ResultResponse.Error("Error: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            emit(ResultResponse.Error(e.localizedMessage ?: "Network error"))
        }
    }.flowOn(Dispatchers.IO)

    fun getUserAddress(): Flow<ResultResponse<GetUserAddressResponse>> = flow {
        emit(ResultResponse.Loading)

        try {
            val response = personalizeService.getAddressData()
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(ResultResponse.Success(it))
                } ?: emit(ResultResponse.Error("Failed to fetch address data, response body empty"))
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