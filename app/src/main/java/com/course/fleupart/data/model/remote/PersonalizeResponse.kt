package com.course.fleupart.data.model.remote

import com.google.gson.annotations.SerializedName
import java.io.File

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

data class UserSelfPictureRequest(

	@field:SerializedName("file")
	val picture: File
)

data class UserCitizenPictureRequest(

	@field:SerializedName("file")
	val picture: File
)

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

