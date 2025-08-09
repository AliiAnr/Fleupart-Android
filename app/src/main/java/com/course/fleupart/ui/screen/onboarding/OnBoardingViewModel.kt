package com.course.fleupart.ui.screen.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.course.fleupart.data.repository.OnBoardingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OnBoardingViewModel(
    private val onBoardingRepository: OnBoardingRepository
) : ViewModel(){

    private val _onBoardingStatus = MutableStateFlow(false)
    val onBoardingStatus: StateFlow<Boolean> = _onBoardingStatus.asStateFlow()

    init {
        getOnBoardingStatus()
    }

    private fun getOnBoardingStatus() {
        viewModelScope.launch {
            onBoardingRepository.getOnBoardingStatus().collect { isCompleted ->
                _onBoardingStatus.value = isCompleted
            }
        }
    }

    fun setOnBoardingCompleted(completed: Boolean) {
        viewModelScope.launch {
            onBoardingRepository.setOnboardingCompleted(completed)
        }
    }

}