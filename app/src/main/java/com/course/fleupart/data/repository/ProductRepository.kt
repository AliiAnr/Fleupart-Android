package com.course.fleupart.data.repository

import android.content.Context
import com.course.fleupart.data.model.remote.CreateProductPayload
import com.course.fleupart.data.model.remote.GetAllCategoryResponse
import com.course.fleupart.data.model.remote.PersonalizeResponse
import com.course.fleupart.retrofit.api.ApiConfig
import com.course.fleupart.retrofit.services.ProductService
import com.course.fleupart.ui.common.ResultResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


class ProductRepository private constructor(
    context: Context
) {

    private val productService: ProductService = ApiConfig.getProductService(context)

    fun createProductWithCategory(
        payload: CreateProductPayload,
        imageFiles: List<File>
    ): Flow<ResultResponse<PersonalizeResponse>> = flow {
        emit(ResultResponse.Loading)

        val response = productService.createProductWithCategory(
            name = payload.name.toPlainRequestBody(),
            stock = payload.stock.toString().toPlainRequestBody(),
            description = payload.description.toPlainRequestBody(),
            arrangeTime = payload.arrangeTime.toPlainRequestBody(),
            point = payload.point.toString().toPlainRequestBody(),
            price = payload.price.toString().toPlainRequestBody(),
            isPreOrder = payload.isPreOrder.toString().toPlainRequestBody(),
            categoryId = payload.categoryId.toPlainRequestBody(),
            files = imageFiles.map { file ->
                MultipartBody.Part.createFormData(
                    name = "files",
                    filename = file.name,
                    body = file.asRequestBody("image/jpeg".toMediaType())
                )
            }
        )

        if (response.isSuccessful) {
            response.body()?.let { emit(ResultResponse.Success(it)) }
                ?: emit(ResultResponse.Error("Empty response body"))
        } else {
            emit(ResultResponse.Error(response.errorBody()?.string() ?: "Unknown error"))
        }
    }.flowOn(Dispatchers.IO)

    fun getAllCategory(): Flow<ResultResponse<GetAllCategoryResponse>> = flow {
        emit(ResultResponse.Loading)
        try {
            val response = productService.getAllCategory()
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

    private fun String.toPlainRequestBody(): RequestBody =
        this.toRequestBody("text/plain".toMediaType())

    companion object {
        @Volatile
        private var INSTANCE: ProductRepository? = null

        fun getInstance(context: Context): ProductRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ProductRepository(context).also { INSTANCE = it }
            }
    }
}