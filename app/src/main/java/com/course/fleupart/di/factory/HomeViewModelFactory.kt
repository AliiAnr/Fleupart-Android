package com.course.fleupart.di.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.course.fleupart.data.repository.HomeRepository
import com.course.fleupart.di.Injection
import com.course.fleupart.ui.screen.dashboard.home.HomeViewModel


class HomeViewModelFactory private constructor(private val homeRepository: HomeRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                HomeViewModel(homeRepository = homeRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: HomeViewModelFactory? = null

        @JvmStatic
        fun getInstance(
            context: Context
        ): HomeViewModelFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: HomeViewModelFactory(Injection.provideHomeRepository(context)).also {
                    INSTANCE = it
                }
            }
    }
}