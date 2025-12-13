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