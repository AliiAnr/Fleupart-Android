package com.course.fleupart.ui.screen.authentication.register

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.course.fleupart.data.model.remote.OtpResponse
import com.course.fleupart.data.model.remote.RegisterResponse
import com.course.fleupart.data.repository.RegisterRepository
import com.course.fleupart.ui.common.ResultResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterScreenViewModel(
    private val registerRepository: RegisterRepository
) : ViewModel() {

    private val _registerState =
        MutableStateFlow<ResultResponse<RegisterResponse>>(ResultResponse.None)
    val registerState: StateFlow<ResultResponse<RegisterResponse>> = _registerState

    private val _otpState = MutableStateFlow<ResultResponse<OtpResponse>>(ResultResponse.None)
    val otpState: StateFlow<ResultResponse<OtpResponse>> = _otpState

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isValid = MutableStateFlow(false)
    val isValid: StateFlow<Boolean> = _isValid

    var isButtonEnabled by mutableStateOf(true)
        private set

    var tempLog by mutableStateOf(false)
        private set

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

    fun registerSeller() {
        if (!isButtonEnabled) return

        if (validateEmail() && validatePassword() && validateConfirmPassword()) {
            viewModelScope.launch {
                try {
                    isButtonEnabled = false
                    _registerState.value = ResultResponse.Loading
                    registerRepository.registerSeller(emailValue, passwordValue)
                        .collect { result ->
                            _registerState.value = result
                        }
                    if (_registerState.value is ResultResponse.Success) {
                        requestOtp()
                    }
                } catch (e: Exception) {
                    _registerState.value = ResultResponse.Error("Registration failed: ${e.message}")
                } finally {
                    isButtonEnabled = true
                }
            }
        } else {
            _registerState.value = ResultResponse.Error("Please correct the errors above.")
        }
    }

    private fun requestOtp() {
        if (validateEmail()) {
            viewModelScope.launch {
                try {
                    registerRepository.generateOtp(emailValue)
                        .collect { result ->
                            _otpState.value = result
                        }
                } catch (e: Exception) {
                    _otpState.value = ResultResponse.Error("Failed to generate OTP: ${e.message}")
                }
            }
        } else {
            _otpState.value = ResultResponse.Error("Please enter a valid email address.")
        }
    }

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

    fun resetState() {
        usernameValue = ""
        usernameError = ""
        emailValue = ""
        emailError = ""
        passwordValue = ""
        passwordError = ""
        confirmPasswordValue = ""
        confirmPasswordError = ""
        isButtonEnabled = true
        tempLog = false
        _loading.value = false
        _errorMessage.value = null
        _registerState.value = ResultResponse.None
        _otpState.value = ResultResponse.None
        _isValid.value = false
    }


}
