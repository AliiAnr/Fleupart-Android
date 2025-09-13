package com.course.fleupart.di

import android.content.Context
import com.course.fleupart.data.repository.LoginRepository
import com.course.fleupart.data.repository.NotificationRepository
import com.course.fleupart.data.repository.OnBoardingRepository
import com.course.fleupart.data.repository.OnDataBoardingRepository
import com.course.fleupart.data.repository.OtpRepository
import com.course.fleupart.data.repository.ProductRepository
import com.course.fleupart.data.repository.RegisterRepository

object Injection {

    fun provideNotificationRepository(context: Context): NotificationRepository {
        return NotificationRepository.getInstance(context)
    }

    fun provideOnBoardingRepository(context: Context): OnBoardingRepository {
        return OnBoardingRepository.getInstance(context)
    }

    fun provideOnDataBoardingRepository(context: Context): OnDataBoardingRepository {
        return OnDataBoardingRepository.getInstance(context)
    }

    fun provideRegisterRepository(context: Context): RegisterRepository {
        return RegisterRepository.getInstance(context)
    }

    fun provideOtpRepository(context: Context): OtpRepository {
        return OtpRepository.getInstance(context)
    }

    fun provideLoginRepository(context: Context): LoginRepository {
        return LoginRepository.getInstance(context)
    }

    fun provideProductRepository(context: Context): ProductRepository {
        return ProductRepository.getInstance(context)
    }

    fun provideProfileRepository(context: Context) = com.course.fleupart.data.repository.ProfileRepository.getInstance(context)

}