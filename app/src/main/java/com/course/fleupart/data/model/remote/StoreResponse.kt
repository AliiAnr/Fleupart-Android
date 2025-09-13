package com.course.fleupart.data.model.remote

import com.google.gson.annotations.SerializedName

data class ApiUpdateResponse(

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("statusCode")
	val statusCode: Int,

	@field:SerializedName("timestamp")
	val timestamp: String
)

data class StoreDetailResponse(

	@field:SerializedName("data")
	val data: StoreDetailData? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null,

	@field:SerializedName("timestamp")
	val timestamp: String? = null
)

data class StoreDetailData(

	@field:SerializedName("operational_day")
	val operationalDay: String? = null,

	@field:SerializedName("address")
	val address: StoreAddress? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("picture")
	val picture: String? = null,

	@field:SerializedName("admin_verified_at")
	val adminVerifiedAt: Any? = null,

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

	@field:SerializedName("id")
	val id: String? = null
)

data class StoreAddress(

	@field:SerializedName("province")
	val province: String? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("road")
	val road: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("latitude")
	val latitude: Double? = null,

	@field:SerializedName("district")
	val district: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("postcode")
	val postcode: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("detail")
	val detail: String? = null,

	@field:SerializedName("storeId")
	val storeId: String? = null,

	@field:SerializedName("longitude")
	val longitude: Double? = null
)


///////////////////////
//////////////////////



data class StoreAddressResponse(

	@field:SerializedName("data")
	val data: StoreAddressData? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null,

	@field:SerializedName("timestamp")
	val timestamp: String? = null
)

data class StoreAddressData(

	@field:SerializedName("province")
	val province: String? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("road")
	val road: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("latitude")
	val latitude: Double? = null,

	@field:SerializedName("district")
	val district: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("postcode")
	val postcode: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("detail")
	val detail: String? = null,

	@field:SerializedName("storeId")
	val storeId: String? = null,

	@field:SerializedName("longitude")
	val longitude: Double? = null
)


///////////////
//////////////


data class UpdateStoreAddressRequest(

	@field:SerializedName("province")
	val province: String? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("road")
	val road: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("latitude")
	val latitude: Any? = null,

	@field:SerializedName("district")
	val district: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("postcode")
	val postcode: String? = null,

	@field:SerializedName("detail")
	val detail: String? = null,

	@field:SerializedName("longitude")
	val longitude: Any? = null
)
