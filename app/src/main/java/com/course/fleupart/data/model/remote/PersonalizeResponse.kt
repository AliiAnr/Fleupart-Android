package com.course.fleupart.data.model.remote

import com.google.gson.annotations.SerializedName

data class PersonalizeResponse(

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("statusCode")
	val statusCode: Int,

	@field:SerializedName("timestamp")
	val timestamp: String
)

data class CitizenDataRequest(

	@field:SerializedName("phone")
	val phone: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("identity_number")
	val identityNumber: String,

	@field:SerializedName("account")
	val account: String
)

