package com.course.fleupart.di

import android.content.Context
import com.course.fleupart.data.repository.OnBoardingRepository
import com.course.fleupart.data.repository.OtpRepository
import com.course.fleupart.data.repository.RegisterRepository

object Injection {

    fun provideOnBoardingRepository(context: Context): OnBoardingRepository {
        return OnBoardingRepository.getInstance(context)
    }

    fun provideRegisterRepository(context: Context): RegisterRepository {
        return RegisterRepository.getInstance(context)
    }

    fun provideOtpRepository(context: Context): OtpRepository {
        return OtpRepository.getInstance(context)
    }

}