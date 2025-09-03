package com.course.fleupart.ui.screen.authentication.login

import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.course.fleupart.data.model.remote.GetUserResponse
import com.course.fleupart.data.model.remote.LoginResponse
import com.course.fleupart.data.model.remote.PersonalizeResponse
import com.course.fleupart.data.repository.LoginRepository
import com.course.fleupart.data.repository.NotificationRepository
import com.course.fleupart.ui.common.ResultResponse
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginScreenViewModel(
    private val loginRepository: LoginRepository,
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    private val _loginState =
        MutableStateFlow<ResultResponse<LoginResponse>>(ResultResponse.None)
    val loginState: StateFlow<ResultResponse<LoginResponse>> = _loginState

    private val _userState =
        MutableStateFlow<ResultResponse<GetUserResponse>>(ResultResponse.None)
    val userState: StateFlow<ResultResponse<GetUserResponse>> = _userState

    private val _personalizeState =
        MutableStateFlow<ResultResponse<PersonalizeResponse>>(ResultResponse.None)
    val personalizeState: StateFlow<ResultResponse<PersonalizeResponse>> = _personalizeState

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _isValid = MutableStateFlow(false)
    val isValid: StateFlow<Boolean> = _isValid

    var emailValue by mutableStateOf("")
        private set
    var emailError by mutableStateOf("")
        private set

    var passwordValue by mutableStateOf("")
        private set
    var passwordError by mutableStateOf("")
        private set

    var usernameValue by mutableStateOf("")
        private set
    var usernameError by mutableStateOf("")
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

    fun loginUser() {
        if (validateEmail() && validatePassword()) {
            viewModelScope.launch {
                try {
                    _loginState.value = ResultResponse.Loading
                    loginRepository.loginUser(emailValue, passwordValue)
                        .collect { result ->
                            if(result is ResultResponse.Success) {
                                // Sync FCM token after successful login
                                saveToken()
                            }
                            _loginState.value = result
                        }
//                    delay(2000)
//                    _loginState.value =
//                        ResultResponse.Success(LoginResponse(
//                            data = Data(
//                                "wakoak"
//                            ),
//                            message = "Success",
//                            statusCode = 200,
//                            timestamp = "owkeake"
//                        ))
                } catch (e: Exception) {
                    _loginState.value = ResultResponse.Error("Registration failed: ${e.message}")
                }
            }
        } else {
            _loginState.value = ResultResponse.Error("Please correct the errors above.")
        }
    }

//    fun inputUsername() {
//        if (usernameValue.isNotEmpty() && validateUsername()) {
//            viewModelScope.launch {
//                try {
//                    _personalizeState.value = ResultResponse.Loading
//                    loginRepository.setUsername(usernameValue)
//                        .collect { result ->
//                            _personalizeState.value = result
//                        }
//                } catch (e: Exception) {
//                    _personalizeState.value =
//                        ResultResponse.Error("Registration failed: ${e.message}")
//                }
//            }
//        } else {
//            _personalizeState.value = ResultResponse.Error("Please correct the errors above.")
//        }
//    }

    fun getUser() {
        viewModelScope.launch {
            try {
                _userState.value = ResultResponse.Loading
                loginRepository.getUser()
                    .collect { result ->
                        _userState.value = result
                    }
            } catch (e: Exception) {
                _userState.value = ResultResponse.Error("Failed to get user: ${e.message}")
            }
        }
    }

    fun setPersonalizeCompleted() {
        viewModelScope.launch {
            loginRepository.setPersonalizeFilled()
        }
    }

    fun isUserLoggedIn() = loginRepository.isUserLoggedIn()

    fun isPersonalizeFilled() = loginRepository.isPersonalizeFilled()


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


    // remove validatePassword if user login, validate only for register
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

    fun login(onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (validateEmail() && validatePassword()) {
            _loading.value = true
            viewModelScope.launch {
                delay(2000)
                _loading.value = false
                onSuccess()
            }
        } else {
            onError("Invalid email or password")
        }
    }

    fun saveToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result
                    Log.e("TOKENN: ", token)
                    viewModelScope.launch {
                        notificationRepository.saveToken(token)
                            .collect { result ->
                                if(result is ResultResponse.Success) {
                                    Log.e("Notification", "Token saved successfully: ${result.data.message}")
                                }
                            }
                    }
                }
            }
    }

    fun resetState() {
        _loginState.value = ResultResponse.None
        _userState.value = ResultResponse.None
        _personalizeState.value = ResultResponse.None
        emailValue = ""
        emailError = ""
        passwordValue = ""
        passwordError = ""
        usernameValue = ""
        usernameError = ""
        _loading.value = false
    }
}
