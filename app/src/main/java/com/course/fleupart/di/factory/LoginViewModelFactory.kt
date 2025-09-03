package com.course.fleupart.di.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.course.fleupart.data.repository.LoginRepository
import com.course.fleupart.data.repository.NotificationRepository
import com.course.fleupart.di.Injection
import com.course.fleupart.ui.screen.authentication.login.LoginScreenViewModel


class LoginViewModelFactory private constructor(
    private val loginRepository: LoginRepository,
    private val notificationRepository: NotificationRepository
) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginScreenViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                LoginScreenViewModel(
                    loginRepository = loginRepository,
                    notificationRepository = notificationRepository
                ) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: LoginViewModelFactory? = null

        @JvmStatic
        fun getInstance(
            context: Context
        ): LoginViewModelFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: LoginViewModelFactory(
                    loginRepository = Injection.provideLoginRepository(context),
                    notificationRepository = Injection.provideNotificationRepository(context)
                ).also {
                    INSTANCE = it
                }
            }
    }
}