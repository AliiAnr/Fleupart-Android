package com.course.fleupart.di.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.course.fleupart.data.repository.ProfileRepository
import com.course.fleupart.di.Injection
import com.course.fleupart.ui.screen.dashboard.profile.ProfileViewModel

class ProfileViewModelFactory private constructor(private val profileRepository: ProfileRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                ProfileViewModel(profileRepository = profileRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ProfileViewModelFactory? = null

        @JvmStatic
        fun getInstance(
            context: Context
        ): ProfileViewModelFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ProfileViewModelFactory(
                    Injection.provideProfileRepository(context)
                ).also {
                    INSTANCE = it
                }
            }
    }
}