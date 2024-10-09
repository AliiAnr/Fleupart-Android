package com.course.fleura.ui.screen.authentication.username

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UsernameScreenViewModel : ViewModel() {

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


    fun setUsername(value: String) {
        usernameValue = value
        validateUsername()
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

}
