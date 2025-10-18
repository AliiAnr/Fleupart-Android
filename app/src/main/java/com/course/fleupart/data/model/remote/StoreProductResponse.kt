package com.course.fleupart.data.model.remote

import com.google.gson.annotations.SerializedName


data class StoreProductResponse(

    @field:SerializedName("data")
    val data: List<StoreProduct>,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("statusCode")
    val statusCode: Int,

    @field:SerializedName("timestamp")
    val timestamp: String
)

data class StoreProduct(

    @field:SerializedName("price")
    val price: String,

    @field:SerializedName("arrange_time")
    val arrangeTime: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("pre_order")
    val preOrder: Boolean,

    @field:SerializedName("store")
    val store: StoreFromProduct,

    @field:SerializedName("stock")
    val stock: Int,

    @field:SerializedName("category")
    val category: ProductCategory,

    @field:SerializedName("point")
    val point: Int,

    @field:SerializedName("picture")
    val picture: List<ProductPicture>,

    @field:SerializedName("rating")
    val rating: Double,
)

data class StoreFromProduct(

    @field:SerializedName("operational_day")
    val operationalDay: String,

    @field:SerializedName("sellerId")
    val sellerId: String,

    @field:SerializedName("balance")
    val balance: String,

    @field:SerializedName("updated_at")
    val updatedAt: String,

    @field:SerializedName("phone")
    val phone: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("logo")
    val logo: String,

    @field:SerializedName("operational_hour")
    val operationalHour: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("picture")
    val picture: String
)

data class ProductPicture(

    @field:SerializedName("path")
    val path: String,

    @field:SerializedName("id")
    val id: String
)

data class ProductCategory(

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("id")
    val id: String
)