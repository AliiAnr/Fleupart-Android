package com.course.fleupart.di

import android.content.Context
import com.course.fleupart.data.repository.OnBoardingRepository

object Injection {

    fun provideOnBoardingRepository(context: Context): OnBoardingRepository {
        return OnBoardingRepository.getInstance(context)
    }

}