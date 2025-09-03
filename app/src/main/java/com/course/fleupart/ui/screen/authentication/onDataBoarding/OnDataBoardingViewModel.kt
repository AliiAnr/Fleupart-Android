package com.course.fleupart.ui.screen.authentication.onDataBoarding

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.course.fleupart.data.model.remote.AddressDataRequest
import com.course.fleupart.data.model.remote.CitizenDataRequest
import com.course.fleupart.data.model.remote.PersonalizeResponse
import com.course.fleupart.data.repository.OnDataBoardingRepository
import com.course.fleupart.ui.common.ResultResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OnDataBoardingViewModel(
    private val onDataBoardingRepository: OnDataBoardingRepository
) : ViewModel() {


    private val _personalizeState =
        MutableStateFlow<ResultResponse<PersonalizeResponse>>(ResultResponse.None)
    val personalizeState: StateFlow<ResultResponse<PersonalizeResponse>> = _personalizeState

    var nameValue by mutableStateOf("")
        private set
    var nameErrorValue by mutableStateOf("")
        private set

    var citizenIdValue by mutableStateOf("")
        private set
    var citizenIdErrorValue by mutableStateOf("")
        private set

    var phoneNumberValue by mutableStateOf("")
        private set
    var phoneNumberErrorValue by mutableStateOf("")
        private set

    var bankAccountNumberValue by mutableStateOf("")
        private set
    var bankAccountNumberErrorValue by mutableStateOf("")
        private set

    var streetNameValue by mutableStateOf("")
        private set

    var subDistrictValue by mutableStateOf("")
        private set


    var cityValue by mutableStateOf("")
        private set

    var provinceValue by mutableStateOf("")
        private set

    var postalCodeValue by mutableStateOf("")
        private set

    var additionalDetailValue by mutableStateOf("")
        private set

    fun setName(value: String) {
        nameValue = value
        validateName()
    }

    fun setCitizenId(value: String) {
        citizenIdValue = value
        validateCitizenId()
    }

    fun setPhoneNumber(value: String) {
        phoneNumberValue = value
        validatePhoneNumber()
    }

    fun setBankAccountNumber(value: String) {
        bankAccountNumberValue = value
        validateBankAccountNumber()
    }

    fun setStreetName(value: String) {
        streetNameValue = value
    }

    fun setSubDistrict(value: String) {
        subDistrictValue = value
    }

    fun setCity(value: String) {
        cityValue = value
    }

    fun setProvince(value: String) {
        provinceValue = value
    }

    fun setPostalCode(value: String) {
        postalCodeValue = value
    }

    fun setAdditionalDetail(value: String) {
        additionalDetailValue = value
    }

    private fun validateName(): Boolean {
        val name = nameValue.trim()
        return if (name.isEmpty()) {
            nameErrorValue = "Name cannot be empty"
            false
        } else {
            nameErrorValue = ""
            true
        }
    }

    fun setPersonalizeState(value: ResultResponse<PersonalizeResponse>) {
        _personalizeState.value = value
    }

    private fun String.isDigitsOnly(): Boolean = this.all(Char::isDigit)

    private val LocalMobileRegex = Regex("^08\\d{8,11}$")

    private fun validateCitizenId(): Boolean {
        val id = citizenIdValue.trim()
        return when {
            id.isEmpty() -> {
                citizenIdErrorValue = "Citizen ID cannot be empty"
                false
            }

            !id.isDigitsOnly() -> {
                citizenIdErrorValue = "Citizen ID must contain digits only (0-9)"
                false
            }

            id.length != 16 -> {
                citizenIdErrorValue = "Citizen ID must be exactly 16 digits"
                false
            }

            else -> {
                citizenIdErrorValue = ""
                true
            }
        }
    }

    private fun validatePhoneNumber(): Boolean {
        val phone = phoneNumberValue.trim()
        return when {
            phone.isEmpty() -> {
                phoneNumberErrorValue = "Phone number cannot be empty"
                false
            }

            !phone.isDigitsOnly() -> {
                phoneNumberErrorValue = "Phone number must contain digits only (0-9)"
                false
            }

            !LocalMobileRegex.matches(phone) -> {
                phoneNumberErrorValue = "Use local format: starts with 08 and 10–13 digits"
                false
            }

            else -> {
                phoneNumberErrorValue = ""
                true
            }
        }
    }

    private fun validateBankAccountNumber(): Boolean {
        val account = bankAccountNumberValue.trim()
        return when {
            account.isEmpty() -> {
                bankAccountNumberErrorValue = "Bank account number cannot be empty"
                false
            }

            !account.isDigitsOnly() -> {
                bankAccountNumberErrorValue = "Bank account number must contain digits only (0-9)"
                false
            }

            account.length !in 10..16 -> {
                bankAccountNumberErrorValue = "Bank account number must be 10–16 digits"
                false
            }

            else -> {
                bankAccountNumberErrorValue = ""
                true
            }
        }
    }


    fun inputCitizenData() {
        if (validateName() && validateCitizenId() && validatePhoneNumber() && validateBankAccountNumber()) {
//           Log.d("OnDataBoardingViewModel", "Input data is valid")
//        } else {
//            Log.d("OnDataBoardingViewModel", "Input data is invalid")
//        }
            viewModelScope.launch {
                try {
                    _personalizeState.value = ResultResponse.Loading
                    onDataBoardingRepository.inputCitizenData(
                        citizenData = CitizenDataRequest(
                            name = nameValue,
                            identityNumber = citizenIdValue,
                            phone = phoneNumberValue,
                            account = bankAccountNumberValue
                        )
                    ).collect { result ->
                            _personalizeState.value = result
                        }
                } catch (e: Exception) {
                    _personalizeState.value =
                        ResultResponse.Error("Add citizen data failed: ${e.message}")
                }
            }
        } else {
            _personalizeState.value = ResultResponse.Error("Please correct the errors above.")
        }
    }

    fun inputAddressData() {
        viewModelScope.launch {
            try {
                _personalizeState.value = ResultResponse.Loading
                onDataBoardingRepository.inputAddressData(
                    AddressDataRequest(
                        road = streetNameValue,
                        subDistrict = subDistrictValue,
                        city = cityValue,
                        province = provinceValue,
                        postCode = postalCodeValue,
                        additionalDetail = additionalDetailValue
                    )
                ).collect { result ->
                        _personalizeState.value = result
                    }
            } catch (e: Exception) {
                _personalizeState.value =
                    ResultResponse.Error("Add address data failed: ${e.message}")
            }
        }
    }
}