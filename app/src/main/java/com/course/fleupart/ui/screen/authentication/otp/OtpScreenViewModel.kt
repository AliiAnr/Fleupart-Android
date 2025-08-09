package com.course.fleupart.ui.screen.authentication.otp

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.course.fleupart.data.model.remote.VerifyOtpRequest
import com.course.fleupart.data.model.remote.VerifyOtpResponse
import com.course.fleupart.data.repository.OtpRepository
import com.course.fleupart.ui.common.OtpAction
import com.course.fleupart.ui.common.OtpState
import com.course.fleupart.ui.common.ResultResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


private const val VALID_OTP_CODE = "14141"

class OtpScreenViewModel(
    private val otpRepository: OtpRepository
) : ViewModel() {

    private val _state = MutableStateFlow(OtpState())
    val state = _state.asStateFlow()

    var emailValue by mutableStateOf("")
        private set

    var otpData by mutableStateOf(
        VerifyOtpRequest(
            "", ""
        )
    )

    fun setEmail(value: String) {
        emailValue = value
    }

    private val _verifyOtpState =
        MutableStateFlow<ResultResponse<VerifyOtpResponse>>(ResultResponse.None)
    val verifyOtpState: StateFlow<ResultResponse<VerifyOtpResponse>> = _verifyOtpState

    private fun verifyOtp(email: String, otp: String) {
        if (otp.isNotBlank()) {
            Log.d("OtpScreenViewModel", "Verifying OTP: $otp for email: $email")
            viewModelScope.launch {
                try {
                    _verifyOtpState.value = ResultResponse.Loading
//                    delay(2000)
//                    _verifyOtpState.value =
//                        ResultResponse.Success(VerifyOtpResponse("Success", 2, "d"))
//                    _verifyOtpState.value = ResultResponse.Error("Failed to verify OTP")
                    otpRepository.verifyOtp(email = email, otp = otp)
                        .collect { result ->
                            _verifyOtpState.value = result
                        }
                    if (_verifyOtpState.value is ResultResponse.Success) {
                        otpData = VerifyOtpRequest(email = email, otpCode = otp)
                        _state.update {
                            it.copy(
                                isValid = true,
                                code = List(5) { null } // Mengatur ulang nilai OTP
                            )
                        }
                    }
                } catch (e: Exception) {
                    _verifyOtpState.value =
                        ResultResponse.Error(e.localizedMessage ?: "Network error")
                    _state.update {
                        it.copy(
                            isValid = false,
                            code = List(5) { null } // Mengatur ulang nilai OTP
                        )
                    }
                }
            }
        } else {
            _verifyOtpState.value = ResultResponse.Error("Please enter OTP")
            _state.update {
                it.copy(
                    isValid = false,
                    code = List(5) { null } // Mengatur ulang nilai OTP
                )
            }
        }
    }

    fun onAction(action: OtpAction) {
        when (action) {
            is OtpAction.OnChangeFieldFocused -> {
                _state.update {
                    it.copy(
                        focusedIndex = action.index
                    )
                }
            }

            is OtpAction.OnEnterNumber -> {
                enterNumber(action.number, action.index)
            }

            OtpAction.OnKeyboardBack -> {
                val previousIndex = getPreviousFocusedIndex(state.value.focusedIndex)
                _state.update {
                    it.copy(
                        code = it.code.mapIndexed { index, number ->
                            if (index == previousIndex) {
                                null
                            } else {
                                number
                            }
                        },
                        focusedIndex = previousIndex
                    )
                }
            }

            OtpAction.OnError -> {
                _state.value = _state.value.copy(code = List(5) { null })
            }
        }
    }

    private fun enterNumber(number: Int?, index: Int) {
        val newCode = state.value.code.mapIndexed { currentIndex, currentNumber ->
            if (currentIndex == index) {
                number
            } else {
                currentNumber
            }
        }
        val wasNumberRemoved = number == null
        _state.update {
            it.copy(
                code = newCode,
                focusedIndex = if (wasNumberRemoved || it.code.getOrNull(index) != null) {
                    it.focusedIndex
                } else {
                    getNextFocusedTextFieldIndex(
                        currentCode = it.code,
                        currentFocusedIndex = it.focusedIndex
                    )
                },
                isValid = if (newCode.none { it == null }) {
                    val otp = newCode.joinToString("") { it?.toString() ?: "" }
                    if (otp.length == 5 && emailValue.isNotBlank()) {
                        verifyOtp(email = emailValue, otp = otp)
                    }
                    _verifyOtpState.value is ResultResponse.Success
                } else null
            )
        }
    }

    private fun getPreviousFocusedIndex(currentIndex: Int?): Int? {
        return currentIndex?.minus(1)?.coerceAtLeast(0)
    }

    private fun getNextFocusedTextFieldIndex(
        currentCode: List<Int?>,
        currentFocusedIndex: Int?
    ): Int? {
        if (currentFocusedIndex == null) {
            return null
        }

        if (currentFocusedIndex == 4) {
            return currentFocusedIndex
        }

        return getFirstEmptyFieldIndexAfterFocusedIndex(
            code = currentCode,
            currentFocusedIndex = currentFocusedIndex
        )
    }

    private fun getFirstEmptyFieldIndexAfterFocusedIndex(
        code: List<Int?>,
        currentFocusedIndex: Int
    ): Int {
        code.forEachIndexed { index, number ->
            if (index <= currentFocusedIndex) {
                return@forEachIndexed
            }
            if (number == null) {
                return index
            }
        }
        return currentFocusedIndex
    }

    fun resetState() {
        _state.value = OtpState()
        _verifyOtpState.value = ResultResponse.None
        emailValue = ""
        otpData = VerifyOtpRequest("", "")
    }
}