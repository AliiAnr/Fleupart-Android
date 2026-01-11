package com.course.fleupart.data.model.remote

data class CreateProductPayload(
    val name: String,
    val stock: Int,
    val description: String,
    val arrangeTime: String,
    val point: Int,
    val price: Long,
    val categoryId: String,
)

data class UpdateProductPayload(
    val productId: String,
    val name: String? = null,
    val stock: Int? = null,
    val description: String? = null,
    val arrangeTime: String? = null,
    val point: Int? = null,
    val price: Long? = null,
    val preOrder: Boolean? = null,
    val categoryId: String? = null
)