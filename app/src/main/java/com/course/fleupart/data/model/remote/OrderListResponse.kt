package com.course.fleupart.data.model.remote

import com.google.gson.annotations.SerializedName


data class FilteredOrdersData(
	val newOrders: List<OrderDataItem> = emptyList(),
	val processOrders: List<OrderDataItem> = emptyList(),
	val pickupOrders: List<OrderDataItem> = emptyList(),
	val deliveryOrders: List<OrderDataItem> = emptyList(),
	val completedOrders: List<OrderDataItem> = emptyList()
)

data class OrderListResponse(

	@field:SerializedName("data")
	val data: List<OrderDataItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null,

	@field:SerializedName("timestamp")
	val timestamp: String? = null
)

data class OrderStore(

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
	val logo: Any? = null,

	@field:SerializedName("operational_hour")
	val operationalHour: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("picture")
	val picture: Any? = null,

	@field:SerializedName("admin_verified_at")
	val adminVerifiedAt: Any? = null
)

data class OrderPayment(

	@field:SerializedName("methode")
	val methode: String? = null,

	@field:SerializedName("qris_url")
	val qrisUrl: String? = null,

	@field:SerializedName("orderId")
	val orderId: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("success_at")
	val successAt: Any? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("qris_expired_at")
	val qrisExpiredAt: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class OrderCategory(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: String? = null
)

data class OrderProduct(

	@field:SerializedName("price")
	val price: String? = null,

	@field:SerializedName("arrange_time")
	val arrangeTime: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("pre_order")
	val preOrder: Boolean? = null,

	@field:SerializedName("store")
	val store: OrderStore? = null,

	@field:SerializedName("stock")
	val stock: Int? = null,

	@field:SerializedName("category")
	val category: OrderCategory? = null,

	@field:SerializedName("point")
	val point: Int? = null,

	@field:SerializedName("admin_verified_at")
	val adminVerifiedAt: Any? = null,

	@field:SerializedName("picture")
	val picture: List<PictureItem?>? = null
)

data class OrderItemsItem(

	@field:SerializedName("product")
	val product: OrderProduct? = null,

	@field:SerializedName("quantity")
	val quantity: Int? = null,

	@field:SerializedName("id")
	val id: String? = null
)

data class PictureItem(

	@field:SerializedName("path")
	val path: String? = null,

	@field:SerializedName("id")
	val id: String? = null
)

data class OrderDataItem(

	@field:SerializedName("note")
	val note: String? = null,

	@field:SerializedName("taken_date")
	val takenDate: String? = null,

	@field:SerializedName("total")
	val total: Int? = null,

	@field:SerializedName("taken_method")
	val takenMethod: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("payment")
	val payment: OrderPayment? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("orderItems")
	val orderItems: List<OrderItemsItem?>? = null,

	@field:SerializedName("point")
	val point: Int? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("addressId")
	val addressId: String? = null
)
