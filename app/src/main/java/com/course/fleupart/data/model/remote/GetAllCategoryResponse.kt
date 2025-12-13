package com.course.fleupart.data.model.remote

import com.google.gson.annotations.SerializedName

data class GetAllCategoryResponse(

	@field:SerializedName("data")
	val data: List<CategoryDataItem>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("statusCode")
	val statusCode: Int,

	@field:SerializedName("timestamp")
	val timestamp: String
)

data class CategoryDataItem(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: String
)
