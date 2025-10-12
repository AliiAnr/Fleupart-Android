package com.course.fleupart.ui.screen.dashboard.order

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.course.fleupart.data.model.remote.FilteredOrdersData
import com.course.fleupart.data.model.remote.OrderDataItem
import com.course.fleupart.data.model.remote.OrderListResponse
import com.course.fleupart.data.model.remote.StoreDetailData
import com.course.fleupart.data.repository.OrderRepository
import com.course.fleupart.ui.common.ResultResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrderViewModel(
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _dataInitialized = MutableStateFlow(false)
    val dataInitialized: StateFlow<Boolean> = _dataInitialized

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    private val _storeOrderState: MutableStateFlow<ResultResponse<OrderListResponse>> =
        MutableStateFlow(ResultResponse.None)
    val storeOrderState: StateFlow<ResultResponse<OrderListResponse>> = _storeOrderState


    private val _filteredOrdersState: MutableStateFlow<ResultResponse<FilteredOrdersData>> =
        MutableStateFlow(ResultResponse.None)
    val filteredOrdersState: StateFlow<ResultResponse<FilteredOrdersData>> = _filteredOrdersState

    private val _newOrders = MutableStateFlow<List<OrderDataItem>>(emptyList())
    val newOrders: StateFlow<List<OrderDataItem>> = _newOrders

    private val _processOrders = MutableStateFlow<List<OrderDataItem>>(emptyList())
    val processOrders: StateFlow<List<OrderDataItem>> = _processOrders

    private val _pickupOrders = MutableStateFlow<List<OrderDataItem>>(emptyList())
    val pickupOrders: StateFlow<List<OrderDataItem>> = _pickupOrders

    private val _deliveryOrders = MutableStateFlow<List<OrderDataItem>>(emptyList())
    val deliveryOrders: StateFlow<List<OrderDataItem>> = _deliveryOrders

    private val _completedOrders = MutableStateFlow<List<OrderDataItem>>(emptyList())
    val completedOrders: StateFlow<List<OrderDataItem>> = _completedOrders


    val storeDetail: MutableState<StoreDetailData?> = mutableStateOf(null)

    init {
        loadStoreDetail()
    }

    fun loadInitialData() {
        if (!_dataInitialized.value) {
//            getStoreOrders()
            getFilteredStoreOrders()
            _dataInitialized.value = true
        }
    }

    fun refreshOrders() {
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                storeDetail.value?.id?.let { storeId ->
                    orderRepository.getFilteredStoreOrders(storeId)
                        .collect { result ->
                            _filteredOrdersState.value = result
                            if (result is ResultResponse.Success) {
                                updateOrderStates(result.data)
                            }
                        }
                }
            } catch (e: Exception) {
                _filteredOrdersState.value = ResultResponse.Error("Failed to refresh orders: ${e.message}")
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    fun getFilteredStoreOrders() {
        viewModelScope.launch {
            try {
                _filteredOrdersState.value = ResultResponse.Loading
                storeDetail.value?.id?.let { storeId ->
                    orderRepository.getFilteredStoreOrders(storeId)
                        .collect { result ->
                            _filteredOrdersState.value = result

                            if (result is ResultResponse.Success) {
                                updateOrderStates(result.data)
                            }
                        }
                } ?: run {
                    _filteredOrdersState.value = ResultResponse.Error("Store ID is null")
                }
            } catch (e: Exception) {
                _filteredOrdersState.value = ResultResponse.Error("Failed to get orders: ${e.message}")
            }
        }
    }

    private fun updateOrderStates(filteredData: FilteredOrdersData) {
        _newOrders.value = filteredData.newOrders
        _processOrders.value = filteredData.processOrders
        _pickupOrders.value = filteredData.pickupOrders
        _deliveryOrders.value = filteredData.deliveryOrders
        _completedOrders.value = filteredData.completedOrders
    }

    // Helper functions to get orders by type
    fun getOrdersByStatus(status: String): List<OrderDataItem> {
        return when (status.lowercase()) {
            "new", "created" -> _newOrders.value
            "process", "processing" -> _processOrders.value
            "pickup", "ready_pickup" -> _pickupOrders.value
            "delivery", "on_delivery" -> _deliveryOrders.value
            "completed" -> _completedOrders.value
            else -> emptyList()
        }
    }

    fun getStoreOrders() {
        viewModelScope.launch {
            try {
                _storeOrderState.value = ResultResponse.Loading
                storeDetail.value?.id?.let { storeId ->
                    orderRepository.getStoreOrders(storeId)
                        .collect { result ->
                            _storeOrderState.value = result
                        }
                } ?: run {
                    _storeOrderState.value = ResultResponse.Error("Store ID is null")
                }
            } catch (e: Exception) {
                _storeOrderState.value = ResultResponse.Error("Failed to get user: ${e.message}")
            }
        }
    }

    private fun loadStoreDetail() {
        viewModelScope.launch {
            // Pertama, cek cache local
            val cachedData = orderRepository.getStoredStoreDetail()
            Log.i("OrderViewModel", "Cached Data: $cachedData")
            if (cachedData != null) {
                storeDetail.value = cachedData
            } else {
                // Jika tidak ada cache, ambil dari remote
                fetchStoreDetailFromRemote()
            }
        }
    }

    private fun fetchStoreDetailFromRemote() {
        viewModelScope.launch {
            orderRepository.getStoreDetail().collect { result ->
                when (result) {
                    is ResultResponse.Success -> {
                        result.data.let { storeDetailData ->
                            storeDetail.value = storeDetailData.data
                            // Simpan ke local storage
                            storeDetailData.data?.let {
                                orderRepository.saveStoreDetail(it)
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
}