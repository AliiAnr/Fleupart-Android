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

data class GetUserAddressResponse(

	@field:SerializedName("data")
	val data: List<AddressData>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("statusCode")
	val statusCode: Int,

	@field:SerializedName("timestamp")
	val timestamp: String
)

data class AddressData(

	@field:SerializedName("sellerId")
	val sellerId: String,

	@field:SerializedName("province")
	val province: String?,

	@field:SerializedName("phone")
	val phone: String?,

	@field:SerializedName("road")
	val road: String?,

	@field:SerializedName("city")
	val city: String?,

	@field:SerializedName("latitude")
	val latitude: Any,

	@field:SerializedName("district")
	val district: String?,

	@field:SerializedName("name")
	val name: String?,

	@field:SerializedName("postcode")
	val postcode: String?,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("detail")
	val detail: String?,

	@field:SerializedName("longitude")
	val longitude: Any
) {
	fun isAddressCompleted(): Boolean {
		return !name.isNullOrBlank() &&
				!phone.isNullOrBlank() &&
				!province.isNullOrBlank() &&
				!road.isNullOrBlank() &&
				!city.isNullOrBlank() &&
				!district.isNullOrBlank() &&
				!postcode.isNullOrBlank() &&
				!detail.isNullOrBlank()
	}
}
