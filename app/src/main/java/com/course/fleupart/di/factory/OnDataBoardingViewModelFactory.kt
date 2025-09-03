package com.course.fleupart.di.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.course.fleupart.data.repository.OnDataBoardingRepository
import com.course.fleupart.di.Injection
import com.course.fleupart.ui.screen.authentication.onDataBoarding.OnDataBoardingViewModel


class OnDataBoardingViewModelFactory private constructor(
    private val onDataBoardingRepository: OnDataBoardingRepository
) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(OnDataBoardingViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                OnDataBoardingViewModel(
                    onDataBoardingRepository = onDataBoardingRepository
                ) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: OnDataBoardingViewModelFactory? = null

        @JvmStatic
        fun getInstance(
            context: Context
        ): OnDataBoardingViewModelFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: OnDataBoardingViewModelFactory(
                    onDataBoardingRepository = Injection.provideOnDataBoardingRepository(context)
                ).also {
                    INSTANCE = it
                }
            }
    }
}