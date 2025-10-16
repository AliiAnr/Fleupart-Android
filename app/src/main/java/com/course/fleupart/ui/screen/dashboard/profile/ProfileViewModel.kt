package com.course.fleupart.ui.screen.dashboard.profile

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.course.fleupart.data.model.remote.ApiUpdateResponse
import com.course.fleupart.data.model.remote.StoreAddressData
import com.course.fleupart.data.model.remote.StoreAddressResponse
import com.course.fleupart.data.model.remote.StoreBannerRequest
import com.course.fleupart.data.model.remote.StoreDetailData
import com.course.fleupart.data.model.remote.StoreDetailResponse
import com.course.fleupart.data.model.remote.StoreLogoRequest
import com.course.fleupart.data.model.remote.StoreProductsResponse
import com.course.fleupart.data.model.remote.UpdateStoreAddressRequest
import com.course.fleupart.data.model.remote.UpdateStoreDetailRequest
import com.course.fleupart.data.repository.ProfileRepository
import com.course.fleupart.data.resource.Resource
import com.course.fleupart.ui.common.ImageCompressor
import com.course.fleupart.ui.common.ResultResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class ProfileViewModel(
    private val profileRepository: ProfileRepository,
) : ViewModel() {

    val storeDetail: MutableState<StoreDetailData?> = mutableStateOf(null)

    private val _isRefreshing = MutableStateFlow(false)
    private val _storeDetailState: MutableStateFlow<ResultResponse<StoreDetailResponse>> =
        MutableStateFlow(ResultResponse.None)
    val storeDetailState: StateFlow<ResultResponse<StoreDetailResponse>> =
        _storeDetailState.asStateFlow()

    private val _updateImageState: MutableStateFlow<ResultResponse<ApiUpdateResponse>> = MutableStateFlow(ResultResponse.None)

    val updateImageState: StateFlow<ResultResponse<ApiUpdateResponse>> = _updateImageState.asStateFlow()

    private val _storeAddressState: MutableStateFlow<ResultResponse<StoreAddressResponse>> =
        MutableStateFlow(ResultResponse.None)
    val storeAddressState: StateFlow<ResultResponse<StoreAddressResponse>> =
        _storeAddressState.asStateFlow()

    private val _storeProductsState: MutableStateFlow<ResultResponse<StoreProductsResponse>> =
        MutableStateFlow(ResultResponse.None)
    val storeProductsState: StateFlow<ResultResponse<StoreProductsResponse>> = _storeProductsState

    private val _updateStoreAddressState: MutableStateFlow<ResultResponse<ApiUpdateResponse>> =
        MutableStateFlow(ResultResponse.None)
    val updateStoreAddressState: StateFlow<ResultResponse<ApiUpdateResponse>> =
        _updateStoreAddressState.asStateFlow()

    private val _updateStoreDetailState: MutableStateFlow<ResultResponse<ApiUpdateResponse>> =
        MutableStateFlow(ResultResponse.None)
    val updateStoreDetailState: StateFlow<ResultResponse<ApiUpdateResponse>> = _updateStoreDetailState.asStateFlow()

    private val _dataInitialized = MutableStateFlow(false)
    val dataInitialized: StateFlow<Boolean> = _dataInitialized

    var storeAddressValue: MutableState<StoreAddressData?> = mutableStateOf(null)

    var storeInformationValue: MutableState<StoreDetailData?> = mutableStateOf(null)

    var storeProductsValue: MutableState<StoreProductsResponse?> = mutableStateOf(null)

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

    var storeNameValue by mutableStateOf("")
        private set

    var storeDescriptionValue by mutableStateOf("")
        private set

    var storePhoneNumberValue by mutableStateOf("")
        private set

    var storeOperationHourValue by mutableStateOf("")
        private set

    var storeOperationDayValue by mutableStateOf("")
        private set

    var storeLogoValue by mutableStateOf<Uri?>(null)
        private set

    var storeBannerValue by mutableStateOf<Uri?>(null)
        private set

    fun setStoreName(value: String) {
        storeNameValue = value
    }

    fun setStoreDescription(value: String) {
        storeDescriptionValue = value
    }

    fun setStorePhoneNumber(value: String) {
        storePhoneNumberValue = value
    }

    fun setStoreOperationHour(value: String) {
        storeOperationHourValue = value
    }

    fun setStoreOperationDay(value: String) {
        storeOperationDayValue = value
    }

    fun getStoreData() {

    }

    init {
        loadStoreDetail()
    }

    fun setStoreLogo(value: Uri): Uri {
        val tempFile = File.createTempFile(
            "temp_storelogo_${System.currentTimeMillis()}",
            ".jpg",
            Resource.appContext.cacheDir
        )
        val success = ImageCompressor.compressImage(Resource.appContext, value, tempFile)
        return if (success) {
            val compressedUri = Uri.fromFile(tempFile)
            val fileSizeKb = tempFile.length() / 1024
            val fileSizeMb = fileSizeKb / 1024.0
            Log.e(
                "ProfileViewModel",
                "Image Logo compressed successfully: $compressedUri, size: ${fileSizeKb}KB (${
                    String.format(
                        "%.2f",
                        fileSizeMb
                    )
                }MB)"
            )
            compressedUri
        } else {
            Log.e("ProfileViewModel", "Image Logo compression failed")
            Uri.EMPTY
        }
    }

    private fun loadStoreDetail() {
        viewModelScope.launch {
            val cachedData = profileRepository.getStoredStoreDetail()
            Log.i("ProfileViewModel", "Cached Data: $cachedData")
            if (cachedData != null) {
                storeDetail.value = cachedData
            } else {
                fetchStoreDetailFromRemote()
            }
        }
    }

    private fun fetchStoreDetailFromRemote() {
        viewModelScope.launch {
            profileRepository.getStoreDetail().collect { result ->
                when (result) {
                    is ResultResponse.Success -> {
                        result.data.let { storeDetailData ->
                            storeDetail.value = storeDetailData.data
                            storeDetailData.data?.let {
                                profileRepository.saveStoreDetail(it)
                            }
                        }
                    }

                    is ResultResponse.Error -> {
                        // Handle error sesuai kebutuhan
                    }

                    is ResultResponse.Loading -> {
                        // Handle loading state jika diperlukan
                    }

                    ResultResponse.None -> {

                    }
                }
            }
        }
    }

    fun setStoreBanner(value: Uri): Uri {
        val tempFile = File.createTempFile(
            "temp_storebanner_${System.currentTimeMillis()}",
            ".jpg",
            Resource.appContext.cacheDir
        )
        val success = ImageCompressor.compressImage(Resource.appContext, value, tempFile)
        return if (success) {
            val compressedUri = Uri.fromFile(tempFile)
            val fileSizeKb = tempFile.length() / 1024
            val fileSizeMb = fileSizeKb / 1024.0
            Log.e(
                "ProfileViewModel",
                "Image Banner compressed successfully: $compressedUri, size: ${fileSizeKb}KB (${
                    String.format(
                        "%.2f",
                        fileSizeMb
                    )
                }MB)"
            )
            compressedUri
        } else {
            Log.e("ProfileViewModel", "Image Banner compression failed")
            Uri.EMPTY
        }
    }

    fun uploadStoreLogo(logoUri: Uri) {
        viewModelScope.launch {
            var logoTempFile: File? = null

            try {
                _updateImageState.value = ResultResponse.Loading

                logoTempFile = File.createTempFile(
                    "temp_storelogo_${System.currentTimeMillis()}",
                    ".jpeg",
                    Resource.appContext.cacheDir
                )

                val compressionSuccess = ImageCompressor.compressImage(Resource.appContext, logoUri, logoTempFile)

                if(compressionSuccess) {
                    profileRepository.uploadStoreLogo(
                        storeLogo = StoreLogoRequest(
                            picture = logoTempFile
                        )
                    ).collect { result ->
                        _updateImageState.value = result
                    }
                } else {
                    _updateImageState.value = ResultResponse.Error("Image compression failed")
                }
            } catch (e: Exception) {
                _updateImageState.value = ResultResponse.Error("Failed to upload logo: ${e.message}")
            } finally {
                logoTempFile?.delete()
            }
        }
    }

    fun uploadStoreBanner(bannerUri: Uri) {
        viewModelScope.launch {
            var bannerTempFile: File? = null

            try {
                _updateImageState.value = ResultResponse.Loading

                bannerTempFile = File.createTempFile(
                    "temp_storebanner_${System.currentTimeMillis()}",
                    ".jpeg",
                    Resource.appContext.cacheDir
                )

                val compressionSuccess = ImageCompressor.compressImage(Resource.appContext, bannerUri, bannerTempFile)

                if(compressionSuccess) {
                    profileRepository.uploadStoreBanner(
                        storeBanner = StoreBannerRequest(
                            picture = bannerTempFile
                        )
                    ).collect { result ->
                        _updateImageState.value = result
                    }
                } else {
                    _updateImageState.value = ResultResponse.Error("Image compression failed")
                }
            } catch (e: Exception) {
                _updateImageState.value = ResultResponse.Error("Failed to upload logo: ${e.message}")
            } finally {
                bannerTempFile?.delete()
            }
        }
    }

    fun getStoreDetail() {
        viewModelScope.launch {
            try {
                _storeDetailState.value = ResultResponse.Loading
                profileRepository.getStoreDetail().collect { result ->
                    _storeDetailState.value = result
                    if (result is ResultResponse.Success) {
                        storeInformationValue.value = result.data.data
                    }
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

    fun getStoreProducts() {
        viewModelScope.launch {
            try {
                _storeProductsState.value = ResultResponse.Loading

                // Tunggu storeDetailState jika masih loading
                storeDetailState.collect { detailResult ->
                    if (detailResult is ResultResponse.Success) {
                        val storeId = detailResult.data.data?.id
                        Log.d("ProfileViewModel", "Fetching products for store ID: $storeId")

                        storeId?.let { id ->
                            profileRepository.getStoreProducts(id).collect { result ->
                                _storeProductsState.value = result
                                if (result is ResultResponse.Success) {
                                    storeProductsValue.value = result.data
                                }
                            }
                        } ?: run {
                            _storeProductsState.value = ResultResponse.Error("Store ID is null")
                        }
                        return@collect // Exit setelah berhasil
                    }
                }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error fetching store products: ${e.message}")
                _storeProductsState.value = ResultResponse.Error("Exception: ${e.message}")
            }
        }
    }


    fun updateStoreDetail(
        name: String,
        description: String,
        phone: String,
        operationalHour: String,
        operationalDay: String
    ) {
        viewModelScope.launch {
            try {
                _updateStoreDetailState.value = ResultResponse.Loading
                profileRepository.updateStoreDetail(
                    requestBody = UpdateStoreDetailRequest(
                        name = name,
                        description = description,
                        phone = phone,
                        operationalHour = operationalHour,
                        operationalDay = operationalDay
                    )
                ).collect { result ->
                    _updateStoreDetailState.value = result
                }
            } catch (e: Exception) {
                _updateStoreDetailState.value =
                    ResultResponse.Error("Failed to update store detail: ${e.message}")
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
                    requestBody = UpdateStoreAddressRequest(
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
        if (!_dataInitialized.value) {
        getStoreProducts()
        getStoreDetail()
        getStoreAddress()
        _dataInitialized.value = true
        }
    }

    fun refreshData() {
        viewModelScope.launch {
            _isRefreshing.value = true
            getStoreProducts()
            getStoreDetail()
            getStoreAddress()
            _isRefreshing.value = false
        }
    }

    fun resetUpdateState() {
        _updateStoreAddressState.value = ResultResponse.None
    }

    fun resetImageUpdateState() {
        _updateImageState.value = ResultResponse.None
    }

    fun resetUpdateStoreDetailState() {
        _updateStoreDetailState.value = ResultResponse.None
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
