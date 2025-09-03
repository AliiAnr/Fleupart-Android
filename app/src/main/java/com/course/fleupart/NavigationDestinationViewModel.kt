package com.course.fleupart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.course.fleupart.ui.screen.authentication.login.LoginScreenViewModel
import com.course.fleupart.ui.screen.navigation.MainDestinations
import com.course.fleupart.ui.screen.onboarding.OnBoardingViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class NavigationDestinationViewModel (
    private val onBoardingViewModel: OnBoardingViewModel,
    private val loginViewModel: LoginScreenViewModel
) : ViewModel() {
    private val _startDestination = MutableStateFlow(MainDestinations.ONBOARDING_ROUTE)
    val startDestination: StateFlow<String> = _startDestination

    init {
        determineStartDestination()
    }

    private fun determineStartDestination() {
        viewModelScope.launch {
            combine(
                onBoardingViewModel.onBoardingStatus,
                loginViewModel.isUserLoggedIn(),
                loginViewModel.isPersonalizeFilled()
            ) { onBoardingComplete, isLoggedIn, isPersonalizeComplete ->
                when {
                    !onBoardingComplete -> MainDestinations.ONBOARDING_ROUTE
                    !isLoggedIn -> MainDestinations.WELCOME_ROUTE
                    !isPersonalizeComplete -> MainDestinations.CITIZEN_ROUTE
                    else -> MainDestinations.DASHBOARD_ROUTE
                }
            }.first().let {
                _startDestination.value = it
            }
        }
    }
}

class StartupNavigationViewModelFactory(
    private val onBoardingViewModel: OnBoardingViewModel,
    private val loginViewModel: LoginScreenViewModel
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NavigationDestinationViewModel::class.java)) {
            return NavigationDestinationViewModel(
                onBoardingViewModel,
                loginViewModel
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}