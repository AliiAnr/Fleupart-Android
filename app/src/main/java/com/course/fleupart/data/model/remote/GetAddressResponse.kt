package com.course.fleupart.data.model.remote

import com.google.gson.annotations.SerializedName

data class AddressDataRequest(

	@field:SerializedName("province")
	val province: String,

	@field:SerializedName("road")
	val road: String,

	@field:SerializedName("city")
	val city: String,

	@field:SerializedName("district")
	val subDistrict: String,

	@field:SerializedName("postcode")
	val postCode: String,

	@field:SerializedName("detail")
	val additionalDetail: String
)



