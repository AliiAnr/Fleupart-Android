package com.course.fleupart.ui.screen.dashboard.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.course.fleupart.data.model.remote.StoreDetailData
import com.course.fleupart.data.model.remote.StoreProductResponse
import com.course.fleupart.data.repository.HomeRepository
import com.course.fleupart.ui.common.ResultResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val homeRepository: HomeRepository
) : ViewModel() {

    private val _storeProductState =
        MutableStateFlow<ResultResponse<StoreProductResponse>>(ResultResponse.None)
    val storeProductState: StateFlow<ResultResponse<StoreProductResponse>> = _storeProductState

    val storeDetail: MutableState<StoreDetailData?> = mutableStateOf(null)

    private val _dataInitialized = MutableStateFlow(false)
    val dataInitialized: StateFlow<Boolean> = _dataInitialized

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    // make function to testing store product state

    init {
        Log.i("HomeViewModel", "Initializing HomeViewModel")
        loadStoreDetail()
    }

    fun loadInitialData() {
        if (!_dataInitialized.value) {
//            getStoreOrders()
//            getFilteredStoreOrders()
            getAllStoreProduct()
            _dataInitialized.value = true
        }
    }

    fun testProductState() {
        viewModelScope.launch {
            _storeProductState.value = ResultResponse.Loading
            delay(4000)
            _storeProductState.value = ResultResponse.Success(
                StoreProductResponse(
                    data = emptyList(),
                    message = "dasd",
                    statusCode = 200,
                    timestamp = "asdasd"
                )
            )
        }
    }

    fun refreshData() {
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                val storeProductsDeferred = async {
                    storeDetail.value?.id?.let { storeId ->
                        homeRepository.getAllStoreProduct(storeId = storeId)
                            .collect { result ->
                                _storeProductState.value = result
                            }
                    }
                }

                storeProductsDeferred.await()
            } catch (e: Exception) {
                _storeProductState.value = ResultResponse.Error("Failed to refresh data: ${e.message}")
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    private fun loadStoreDetail() {
        viewModelScope.launch {
            val cachedData = homeRepository.getStoredStoreDetail()
            Log.i("HomeViewModel", "Cached Data: $cachedData")
            if (cachedData != null) {
                storeDetail.value = cachedData
            } else {
                Log.i("HomeViewModel", "Fetching from remote")
                fetchStoreDetailFromRemote()
            }
        }
    }

    private fun fetchStoreDetailFromRemote() {
        viewModelScope.launch {
            homeRepository.getStoreDetail().collect { result ->
                when (result) {
                    is ResultResponse.Success -> {
                        result.data.let { storeDetailData ->
                            storeDetail.value = storeDetailData.data
                            storeDetailData.data?.let {
                                homeRepository.saveStoreDetail(it)
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

    fun getAllStoreProduct() {
        viewModelScope.launch {
            try {
                _storeProductState.value = ResultResponse.Loading
                storeDetail.value?.id?.let { storeId ->
                    homeRepository.getAllStoreProduct(storeId = storeId)
                        .collect { result ->
                            _storeProductState.value = result
                        }

                } ?: run {
                    _storeProductState.value = ResultResponse.Error("Store ID is null")
                }
            } catch (e: Exception) {
                _storeProductState.value = ResultResponse.Error("Failed to get user: ${e.message}")
            }
        }
    }

}