package com.course.fleupart.data.model.remote

import com.google.gson.annotations.SerializedName

data class DeleteProductRequest(
    @SerializedName("product_id")
    val productId: String
)
