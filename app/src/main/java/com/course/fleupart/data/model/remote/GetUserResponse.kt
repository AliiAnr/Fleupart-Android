package com.course.fleupart.data.model.remote

import com.google.gson.annotations.SerializedName

data class GetUserResponse(

	@field:SerializedName("data")
	val data: Detail,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("statusCode")
	val statusCode: Int,

	@field:SerializedName("timestamp")
	val timestamp: String
)

data class Detail(

	@field:SerializedName("phone")
	val phone: String?,

	@field:SerializedName("verified_at")
	val verifiedAt: String,

	@field:SerializedName("name")
	val name: String?,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("identity_number")
	val identityNumber: String?,

	@field:SerializedName("picture")
	val picture: String?,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("account")
	val account: String?,

	@field:SerializedName("identity_picture")
	val identityPicture: String?
) {
	fun isProfileComplete(): Boolean {
		return !name.isNullOrBlank() &&
				!identityNumber.isNullOrBlank() &&
				!phone.isNullOrBlank() &&
				!picture.isNullOrBlank() &&
				!account.isNullOrBlank() &&
				!identityPicture.isNullOrBlank()
	}
}
