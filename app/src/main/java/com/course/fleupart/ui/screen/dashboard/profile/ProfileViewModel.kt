package com.course.fleupart.ui.screen.dashboard.profile

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.course.fleupart.data.model.remote.ApiUpdateResponse
import com.course.fleupart.data.model.remote.StoreAddressData
import com.course.fleupart.data.model.remote.StoreAddressResponse
import com.course.fleupart.data.model.remote.StoreDetailResponse
import com.course.fleupart.data.model.remote.UpdateStoreAddressRequest
import com.course.fleupart.data.repository.ProfileRepository
import com.course.fleupart.ui.common.ResultResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val profileRepository: ProfileRepository,
) : ViewModel() {

    private val _storeDetailState: MutableStateFlow<ResultResponse<StoreDetailResponse>> =
        MutableStateFlow(ResultResponse.None)
    val storeDetailState: StateFlow<ResultResponse<StoreDetailResponse>> =
        _storeDetailState.asStateFlow()

    private val _storeAddressState: MutableStateFlow<ResultResponse<StoreAddressResponse>> =
        MutableStateFlow(ResultResponse.None)
    val storeAddressState: StateFlow<ResultResponse<StoreAddressResponse>> =
        _storeAddressState.asStateFlow()

    private val _updateStoreAddressState: MutableStateFlow<ResultResponse<ApiUpdateResponse>> =
        MutableStateFlow(ResultResponse.None)
    val updateStoreAddressState: StateFlow<ResultResponse<ApiUpdateResponse>> =
        _updateStoreAddressState.asStateFlow()

    private val _dataInitialized = MutableStateFlow(false)
    val dataInitialized: StateFlow<Boolean> = _dataInitialized

    var storeAddressValue: MutableState<StoreAddressData?> = mutableStateOf(null)

    var nameValue by mutableStateOf("")
        private set

    var phoneNumberValue by mutableStateOf("")
        private set

    var phoneNumberErrorValue by mutableStateOf("")
        private set

    var streetNameValue by mutableStateOf("")
        private set

    var districtValue by mutableStateOf("")
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
    }

    fun setPhoneNumber(value: String) {
        phoneNumberValue = value
        validatePhoneNumber()
    }

    fun streetName(value: String) {
        streetNameValue = value
    }

    fun setDistrict(value: String) {
        districtValue = value
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

    fun getStoreDetail() {
        viewModelScope.launch {
            try {
                _storeDetailState.value = ResultResponse.Loading
                profileRepository.getStoreDetail().collect { result ->
                    _storeDetailState.value = result
                }
            } catch (e: Exception) {
                _storeDetailState.value = ResultResponse.Error("Exception: ${e.message}")
            }
        }
    }

    fun getStoreAddress() {
        viewModelScope.launch {
            try {
                _storeAddressState.value = ResultResponse.Loading
                profileRepository.getStoreAddress().collect { result ->
                    _storeAddressState.value = result
                    if (result is ResultResponse.Success) {
                        storeAddressValue.value = result.data.data
                    }
                }
            } catch (e: Exception) {
                _storeAddressState.value = ResultResponse.Error("Exception: ${e.message}")
            }
        }
    }

    fun updateStoreAddress(
        name: String,
        phone: String,
        province: String,
        road: String,
        city: String,
        district: String,
        postcode: String,
        detail: String,
        latitude: Double,
        longitude: Double
    ) {
        viewModelScope.launch {
            try {
                _updateStoreAddressState.value = ResultResponse.Loading
                profileRepository.updateStoreAddress(
                    requestBody = UpdateStoreAddressRequest (
                        name = name,
                        phone = phone,
                        province = province,
                        road = road,
                        city = city,
                        district = district,
                        postcode = postcode,
                        detail = detail,
                        latitude = latitude,
                        longitude = longitude
                    )
                ).collect { result ->
                    _updateStoreAddressState.value = result
                    if (result is ResultResponse.Success) {
                        getStoreAddress()
                    }
                }
            } catch (e: Exception) {
                _storeAddressState.value =
                    ResultResponse.Error("Failed to update address: ${e.message}")
            }
        }
    }


    fun loadInitialData() {
//        if (!_dataInitialized.value) {
        getStoreDetail()
        getStoreAddress()
        _dataInitialized.value = true
//        }
    }

    fun resetUpdateState() {
        _updateStoreAddressState.value = ResultResponse.None
    }

    private fun String.isDigitsOnly(): Boolean = this.all(Char::isDigit)

    private val LocalMobileRegex = Regex("^08\\d{8,11}$")

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


}
