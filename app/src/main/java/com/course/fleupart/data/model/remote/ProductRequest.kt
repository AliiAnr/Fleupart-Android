package com.course.fleupart.data.model.remote

import com.google.gson.annotations.SerializedName

data class ProductRequest(

	@field:SerializedName("price")
	val price: Int,

	@field:SerializedName("arrange_time")
	val arrangeTime: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("stock")
	val stock: Int,

	@field:SerializedName("point")
	val point: Int
)

data class DeleteProductRequest(
    @SerializedName("product_id")
    val productId: String
)
