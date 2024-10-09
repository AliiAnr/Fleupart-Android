package com.course.fleura.ui.screen.authentication.register

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

class RegisterScreenViewModel : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isValid = MutableStateFlow(false)
    val isValid: StateFlow<Boolean> = _isValid

    var usernameValue by mutableStateOf("")
        private set
    var usernameError by mutableStateOf("")
        private set

    var emailValue by mutableStateOf("")
        private set
    var emailError by mutableStateOf("")
        private set

    var passwordValue by mutableStateOf("")
        private set
    var passwordError by mutableStateOf("")
        private set

    var confirmPasswordValue by mutableStateOf("")
        private set
    var confirmPasswordError by mutableStateOf("")
        private set

    fun setUsername(value: String) {
        usernameValue = value
        validateUsername()
    }

    fun setEmail(value: String) {
        emailValue = value
        validateEmail()
    }

    fun setPassword(value: String) {
        passwordValue = value
        validatePassword()
    }

    fun setConfirmPassword(value: String) {
        confirmPasswordValue = value
        validateConfirmPassword()
    }

    private fun validateUsername(): Boolean {
        val username = usernameValue.trim()
        return if (username.isBlank()) {
            usernameError = "Please fill username field"
            false
        } else if (username.length < 5) {
            usernameError = "Username must be at least 5 characters long"
            false
        } else if (username.contains(" ")) {
            usernameError = "Username should not contain spaces"
            false
        } else {
            usernameError = ""
            true
        }
    }

    private fun validateEmail(): Boolean {
        val email = emailValue.trim()
        return if (email.isBlank()) {
            emailError = "Email is required"
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Invalid email address"
            false
        } else {
            emailError = ""
            true
        }
    }

    private fun validatePassword(): Boolean {
        val password = passwordValue.trim()
        val containsLetter = password.any { it.isLetter() }
        val containsDigit = password.any { it.isDigit() }

        return when {
            password.isBlank() -> {
                passwordError = "Please fill password field"
                false
            }
            password.length < 6 -> {
                passwordError = "Password must be at least 6 characters long"
                false
            }
            !containsLetter -> {
                passwordError = "Password must contain at least one letter"
                false
            }
            !containsDigit -> {
                passwordError = "Password must contain at least one digit"
                false
            }
            else -> {
                passwordError = ""
                true
            }
        }
    }

    private fun validateConfirmPassword(): Boolean {
        return if (confirmPasswordValue != passwordValue) {
            confirmPasswordError = "Passwords do not match"
            false
        } else {
            confirmPasswordError = ""
            true
        }
    }

    fun register(onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (validateEmail() && validatePassword() && validateConfirmPassword() && validateUsername()) {
            _loading.value = true
            viewModelScope.launch {
                delay(2000)
                _loading.value = false
                onSuccess()
            }
        } else {
            onError("Please correct the errors above.")
        }
    }
}
