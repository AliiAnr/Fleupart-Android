package com.course.fleupart.data.model.remote

import com.google.gson.annotations.SerializedName


data class ProductReviewResponse(

    @field:SerializedName("data")
    val data: List<ReviewItem>,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("statusCode")
    val statusCode: Int,

    @field:SerializedName("timestamp")
    val timestamp: String
)

data class ReviewItem(

    @field:SerializedName("rate")
    val rate: Int,

    @field:SerializedName("created_at")
    val createdAt: String,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("buyerId")
    val buyerId: String
)
