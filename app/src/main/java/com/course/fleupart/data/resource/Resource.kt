package com.course.fleupart.data.resource

import android.app.Application
import android.content.Context

class Resource : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }

    companion object {
        lateinit var appContext: Context
            private set
    }
}