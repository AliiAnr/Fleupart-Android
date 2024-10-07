package com.course.fleura.ui.components

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TextFieldViewModel() : ViewModel() {
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _isValid = MutableStateFlow(false)
    val isValid: StateFlow<Boolean> = _isValid

    var emailValue by mutableStateOf("")
        private set
    var emailError by mutableStateOf("")
        private set

    fun setEmail(value: String) {
        emailValue = value
    }

    var passwordValue by mutableStateOf("")
        private set
    var passwordError by mutableStateOf("")
        private set

    fun setPassword(value: String) {
        passwordValue = value
    }

    private fun validateEmail(): Boolean {
        val email = emailValue.trim()
        var isValid = true
        if (email.isEmpty() || email.isBlank()) {
            emailError = "Email is required"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Invalid email address"
            isValid = false
        }
        return isValid
    }

    private fun validatePassword(): Boolean {
        val password = passwordValue.trim()
        var isValid = true
        if (password.isBlank() || password.isEmpty()) {
            passwordError = "Please fill password field"
            isValid = false
        } else if (password.length < 6) {
            passwordError = "Password must be at least 6 characters"
            isValid = false
        }
        return isValid
    }

    fun login(onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (validateEmail() && validatePassword()) {
            _loading.value = true
            viewModelScope.launch {
                delay(2000)
                _loading.value = false
            }
        } else {
            _isValid.value = false
            onError("Invalid email or password")
        }
    }
}