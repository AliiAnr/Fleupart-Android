package com.course.fleupart.ui.screen.dashboard.order

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.course.fleupart.R
import com.course.fleupart.data.model.remote.OrderDataItem
import com.course.fleupart.ui.common.OrderDummyData
import com.course.fleupart.ui.common.ResultResponse
import com.course.fleupart.ui.components.CreatedOrderSummary
import com.course.fleupart.ui.components.CustomTopAppBar
import com.course.fleupart.ui.screen.dashboard.product.EmptyProduct
import com.course.fleupart.ui.screen.navigation.FleupartSurface
import com.course.fleupart.ui.theme.base20
import com.course.fleupart.ui.theme.primaryLight

@Composable
fun Order(
    modifier: Modifier = Modifier,
    orderViewModel: OrderViewModel
) {

    val filteredOrdersState by orderViewModel.filteredOrdersState.collectAsStateWithLifecycle(
        initialValue = ResultResponse.None
    )
    val newOrders by orderViewModel.newOrders.collectAsStateWithLifecycle(
        initialValue = emptyList()
    )
    val processOrders by orderViewModel.processOrders.collectAsStateWithLifecycle(
        initialValue = emptyList()
    )
    val pickupOrders by orderViewModel.pickupOrders.collectAsStateWithLifecycle(
        initialValue = emptyList()
    )
    val deliveryOrders by orderViewModel.deliveryOrders.collectAsStateWithLifecycle(
        initialValue = emptyList()
    )
    val completedOrders by orderViewModel.completedOrders.collectAsStateWithLifecycle(
        initialValue = emptyList()
    )

    var showCircularProgress by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
//        orderViewModel.getStoreOrders()
        orderViewModel.loadInitialData()
    }

    LaunchedEffect(filteredOrdersState) {
        when (filteredOrdersState) {
            is ResultResponse.Success -> {
                showCircularProgress = false
                Log.d("ORDER_SCREEN", "Orders loaded successfully")
            }
            is ResultResponse.Loading -> {
                showCircularProgress = true
            }
            is ResultResponse.Error -> {
                showCircularProgress = false
                Log.e("ORDER_SCREEN", "Error: ${(filteredOrdersState as ResultResponse.Error).error}")
            }
            else -> {}
        }
    }

    OrderContent(
        modifier = modifier,
        newOrders = newOrders,
        processOrders = processOrders,
        pickupOrders = pickupOrders,
        deliveryOrders = deliveryOrders,
        completedOrders = completedOrders,
        showLoading = showCircularProgress
    )
}

@Composable
private fun OrderContent(
    modifier: Modifier = Modifier,
    newOrders: List<OrderDataItem>,
    processOrders: List<OrderDataItem>,
    pickupOrders: List<OrderDataItem>,
    deliveryOrders: List<OrderDataItem>,
    completedOrders: List<OrderDataItem>,
    showLoading: Boolean
) {

    FleupartSurface(
        modifier = modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(base20),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CustomTopAppBar(
                    title = "Order",
                )

                var selectedTab by remember { mutableIntStateOf(0) }
                val tabs = listOf("New Order", "Process", "Pickup", "Delivery", "Completed")

                Column(
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    OrderStatusTabRow(
                        tabs = tabs,
                        selectedTabIndex = selectedTab,
                        onTabSelected = { selectedTab = it }
                    )

                    when (selectedTab) {
                        0 ->
                            NewOrderSection(newOrders = newOrders, isLoading = showLoading)
                        1 ->
                            OnProcessSection(processOrders = processOrders, isLoading = showLoading)
                        2 ->
                            OnPickupSection(pickupOrders = pickupOrders, isLoading = showLoading)
                        3 ->
                            OnDeliverySection(deliveryOrders = deliveryOrders, isLoading = showLoading)
                        4 ->
                            CompletedSection(completedOrders = completedOrders, isLoading = showLoading)
                    }
                }
            }
        }
    }
}

@Composable
fun OrderStatusTabRow(
    tabs: List<String>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    ScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        containerColor = Color.White,
        edgePadding = 0.dp,
        indicator = { tabPositions ->
            SecondaryIndicator(
                modifier = Modifier
                    .tabIndicatorOffset(tabPositions[selectedTabIndex])
                    .height(3.dp),
                color = primaryLight
            )
        },
        divider = {}
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                modifier = Modifier.width(160.dp),
                selected = selectedTabIndex == index,
                onClick = { onTabSelected(index) },
                text = {
                    Text(
                        text = title,
                        fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                        color = Color.Black
                    )
                },
                selectedContentColor = Color.White
            )
        }
    }
}


@Composable
private fun NewOrderSection(
    modifier: Modifier = Modifier,
    newOrders: List<OrderDataItem>,
    isLoading: Boolean,
    useDummyData: Boolean = true
) {
// detailnya belum di sesuaikan
    val ordersToShow = if (useDummyData) OrderDummyData.newOrdersDummy else newOrders
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(base20)
    ) {
        if (isLoading) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                CircularProgressIndicator(color = primaryLight)
            }
        } else if (ordersToShow.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(ordersToShow) { orderItem ->
                    CreatedOrderSummary(orderItem, {})
                }
            }
        } else {
            EmptyProduct(
                icon = R.drawable.empty_new_order,
                title = "You don’t have any new order yet",
                description = "Start to promote your product!"
            )
        }
    }
}

@Composable
private fun OnProcessSection(
    modifier: Modifier = Modifier,
    processOrders: List<OrderDataItem>,
    isLoading: Boolean
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        if (isLoading) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                CircularProgressIndicator(color = primaryLight)
            }
        } else if (processOrders.isNotEmpty()) {
            // Tampilkan daftar newOrders di sini
        } else {
        EmptyProduct(
            icon = R.drawable.empty_process_order,
            title = "You don’t have any processed order yet",
            description = "Start to processing your order!"
        )
        }
    }
}

@Composable
private fun OnDeliverySection(
    modifier: Modifier = Modifier,
    deliveryOrders: List<OrderDataItem>,
    isLoading: Boolean
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        if (isLoading) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                CircularProgressIndicator(color = primaryLight)
            }
        } else if (deliveryOrders.isNotEmpty()) {
            // Tampilkan daftar newOrders di sini
        } else {
            EmptyProduct(
                icon = R.drawable.empty_on_order,
                title = "You currently don’t have any product on delivery",
                description = "Start to promote your product!"
            )
        }
    }
}

@Composable
private fun OnPickupSection(
    modifier: Modifier = Modifier,
    pickupOrders: List<OrderDataItem>,
    isLoading: Boolean
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        if (isLoading) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                CircularProgressIndicator(color = primaryLight)
            }
        } else if (pickupOrders.isNotEmpty()) {
            // Tampilkan daftar newOrders di sini
        } else {
            EmptyProduct(
                icon = R.drawable.empty_product,
                title = "You don't have any orders that can be picked up yet",
                description = "Start to processing your order!"
            )
        }
    }
}

@Composable
private fun CompletedSection(
    modifier: Modifier = Modifier,
    completedOrders: List<OrderDataItem>,
    isLoading: Boolean
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        if (isLoading) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                CircularProgressIndicator(color = primaryLight)
            }
        } else if (completedOrders.isNotEmpty()) {
            // Tampilkan daftar newOrders di sini
        } else {
            EmptyProduct(
                icon = R.drawable.empty_completed_order,
                title = "You don’t have any completed order yet",
                description = "Start to promote your product!"
            )
        }
    }
}
