package com.course.fleupart.data.repository

import android.content.Context

class ProfileRepository private constructor(
    context: Context
) {

    companion object {
        @Volatile
        private var INSTANCE: ProfileRepository? = null

        fun getInstance(context: Context): ProfileRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ProfileRepository(context).also { INSTANCE = it }
            }
    }
}