package com.course.fleupart.data.model.remote

import com.google.gson.annotations.SerializedName

data class CreateProductResponse(

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("statusCode")
	val statusCode: Int,

	@field:SerializedName("timestamp")
	val timestamp: String
)


