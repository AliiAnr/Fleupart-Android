package com.course.fleupart.ui.screen.dashboard.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.course.fleupart.data.model.remote.ProductReviewResponse
import com.course.fleupart.data.model.remote.StoreDetailData
import com.course.fleupart.data.model.remote.StoreDetailResponse
import com.course.fleupart.data.model.remote.StoreProduct
import com.course.fleupart.data.model.remote.StoreProductResponse
import com.course.fleupart.data.repository.HomeRepository
import com.course.fleupart.ui.common.ResultResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val homeRepository: HomeRepository
) : ViewModel() {

    private val _storeProductState =
        MutableStateFlow<ResultResponse<StoreProductResponse>>(ResultResponse.None)
    val storeProductState: StateFlow<ResultResponse<StoreProductResponse>> = _storeProductState

    val storeDetail: MutableState<StoreDetailData?> = mutableStateOf(null)

    private val _productReviewState =
        MutableStateFlow<ResultResponse<ProductReviewResponse>>(ResultResponse.None)
    val productReviewState: StateFlow<ResultResponse<ProductReviewResponse>> = _productReviewState

    private val _storeDetailState: MutableStateFlow<ResultResponse<StoreDetailResponse>> =
        MutableStateFlow(ResultResponse.None)
    val storeDetailState: StateFlow<ResultResponse<StoreDetailResponse>> =
        _storeDetailState.asStateFlow()

    private val _selectedProduct = MutableStateFlow<StoreProduct?>(null)
    val selectedProduct: StateFlow<StoreProduct?> = _selectedProduct.asStateFlow()

    private val _selectedEditProduct = MutableStateFlow<StoreProduct?>(null)
    val selectedEditProduct: StateFlow<StoreProduct?> = _selectedEditProduct.asStateFlow()


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
            if (storeDetail.value  != null) {
                getAllStoreProduct()
            } else {
                fetchStoreDetailFromRemote()
            }
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

    fun setSelectedProduct(storeProduct: StoreProduct) {
        _selectedProduct.value = storeProduct
    }

    fun setSelectedEditProduct(storeProduct: StoreProduct) {
        _selectedEditProduct.value = storeProduct
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

                val storeDetailDeferred = async {
                    fetchStoreDetailFromRemote()
                }

                storeDetailDeferred.await()
                storeProductsDeferred.await()
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error refreshing data: ${e.message}")
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    fun refreshDataAfterUpdate() {
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                // Update store detail first
                loadStoreDetail()

                // Now use the latest storeId to fetch products once
                storeDetail.value?.id?.let { storeId ->
                    homeRepository.getAllStoreProduct(storeId = storeId)
                        .collect { result -> _storeProductState.value = result }
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error refreshing data: ${e.message}")
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    private fun loadStoreDetail() {
        viewModelScope.launch {
            _storeDetailState.value = ResultResponse.Loading
            val cachedData = homeRepository.getStoredStoreDetail()
            Log.i("HomeViewModel", "Cached Data: $cachedData")
            if (cachedData != null) {
                storeDetail.value = cachedData
                _storeDetailState.value = ResultResponse.Success(
                    StoreDetailResponse(
                        data = cachedData,
                        message = "Loaded from cache",
                        statusCode = 200,
                        timestamp = ""
                    )
                )
            } else {
                Log.i("HomeViewModel", "Fetching from remote")
                fetchStoreDetailFromRemote()
            }
        }
    }

    private fun fetchStoreDetailFromRemote() {
        viewModelScope.launch {
            _storeDetailState.value = ResultResponse.Loading
            homeRepository.getStoreDetail().collect { result ->
                when (result) {
                    is ResultResponse.Success -> {
                        _storeDetailState.value = result
                        result.data.let { storeDetailData ->
                            storeDetail.value = storeDetailData.data
                            storeDetailData.data?.let {
                                homeRepository.saveStoreDetail(it)
                            }
                        }
                        getAllStoreProduct()
                    }

                    is ResultResponse.Error -> {
                        _storeDetailState.value = result
                    }

                    is ResultResponse.Loading -> {
                        _storeDetailState.value = result
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

    fun getProductReview(productId: String) {
        viewModelScope.launch {
            try {
                _productReviewState.value = ResultResponse.Loading
                homeRepository.getProductReview(productId = productId)
                    .collect { result ->
                        _productReviewState.value = result
                    }
            } catch (e: Exception) {
                _productReviewState.value = ResultResponse.Error("Failed to get user: ${e.message}")
            }
        }
    }

}