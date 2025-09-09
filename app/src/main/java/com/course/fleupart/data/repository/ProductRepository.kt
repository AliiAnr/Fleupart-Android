package com.course.fleupart.data.repository

import android.content.Context

class ProductRepository private constructor(
    context: Context
) {


    companion object {
        @Volatile
        private var INSTANCE: ProductRepository? = null

        fun getInstance(context: Context): ProductRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ProductRepository(context).also { INSTANCE = it }
            }
    }
}