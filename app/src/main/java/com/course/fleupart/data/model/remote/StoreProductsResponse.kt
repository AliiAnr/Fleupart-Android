package com.course.fleupart.data.model.remote

import com.google.gson.annotations.SerializedName

data class StoreProductsResponse(

	@field:SerializedName("data")
	val data: List<StoreProductDataItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null,

	@field:SerializedName("timestamp")
	val timestamp: String? = null
)

data class StoreProductDataItem(

	@field:SerializedName("rating")
	val rating: Int? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("review_count")
	val reviewCount: Int? = null,

	@field:SerializedName("pre_order")
	val preOrder: Boolean? = null,

	@field:SerializedName("store")
	val store: StoreProductStore? = null,

	@field:SerializedName("point")
	val point: Int? = null,

	@field:SerializedName("admin_verified_at")
	val adminVerifiedAt: Any? = null,

	@field:SerializedName("picture")
	val picture: List<PictureItem?>? = null,

	@field:SerializedName("price")
	val price: String? = null,

	@field:SerializedName("arrange_time")
	val arrangeTime: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("stock")
	val stock: Int? = null,

	@field:SerializedName("category")
	val category: StoreProductCategory? = null
)

data class StoreProductPictureItem(

	@field:SerializedName("path")
	val path: String? = null,

	@field:SerializedName("id")
	val id: String? = null
)

data class StoreProductCategory(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: String? = null
)

data class StoreProductStore(

	@field:SerializedName("operational_day")
	val operationalDay: String? = null,

	@field:SerializedName("sellerId")
	val sellerId: String? = null,

	@field:SerializedName("balance")
	val balance: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("logo")
	val logo: String? = null,

	@field:SerializedName("operational_hour")
	val operationalHour: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("picture")
	val picture: String? = null,

	@field:SerializedName("admin_verified_at")
	val adminVerifiedAt: Any? = null
)
