package com.course.fleupart.retrofit.api

import android.content.Context
import com.course.fleupart.BuildConfig
import com.course.fleupart.retrofit.AuthInterceptor
import com.course.fleupart.retrofit.services.LoginService
import com.course.fleupart.retrofit.services.NotificationService
import com.course.fleupart.retrofit.services.OrderService
import com.course.fleupart.retrofit.services.OtpService
import com.course.fleupart.retrofit.services.PersonalizeService
import com.course.fleupart.retrofit.services.ProfileService
import com.course.fleupart.retrofit.services.RegisterService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {

    private fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        } else {
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    private fun provideOkHttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(provideLoggingInterceptor())
            .addInterceptor(AuthInterceptor(context))
            .build()
    }

    private fun provideRetrofit(context: Context): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(provideOkHttpClient(context))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getNotificationService(context: Context): NotificationService {
        return provideRetrofit(context).create(NotificationService::class.java)
    }

    fun getRegisterService(context: Context): RegisterService {
        return provideRetrofit(context).create(RegisterService::class.java)
    }

    fun getOtpService(context: Context): OtpService {
        return provideRetrofit(context).create(OtpService::class.java)
    }

    fun getPersonalizeService(context: Context): PersonalizeService {
        return provideRetrofit(context).create(PersonalizeService::class.java)
    }

    fun getLoginService(context: Context): LoginService {
        return provideRetrofit(context).create(LoginService::class.java)
    }

    fun getOrderService(context: Context) : OrderService {
        return provideRetrofit(context).create(OrderService::class.java)
    }

    fun getProfileService(context: Context) : ProfileService {
        return provideRetrofit(context).create(ProfileService::class.java)
    }
}