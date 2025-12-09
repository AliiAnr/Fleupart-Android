package com.course.fleupart.ui.screen.dashboard.home

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.course.fleupart.R
import com.course.fleupart.data.model.remote.StoreDetailData
import com.course.fleupart.data.model.remote.StoreProduct
import com.course.fleupart.data.model.remote.StoreProductResponse
import com.course.fleupart.ui.common.ResultResponse
import com.course.fleupart.ui.common.formatCurrency
import com.course.fleupart.ui.common.loadingFx
import com.course.fleupart.ui.components.FakeCategory
import com.course.fleupart.ui.components.Flower
import com.course.fleupart.ui.components.FlowerItem
import com.course.fleupart.ui.components.ProductListLoading
import com.course.fleupart.ui.components.TipsItem
import com.course.fleupart.ui.screen.dashboard.order.OrderViewModel
import com.course.fleupart.ui.screen.navigation.FleupartSurface
import com.course.fleupart.ui.theme.base100
import com.course.fleupart.ui.theme.base20
import com.course.fleupart.ui.theme.base300
import com.course.fleupart.ui.theme.primaryLight
import com.google.android.datatransport.ProductData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    modifier: Modifier,
    onSnackClick: (Long, String) -> Unit,
    onTipsDetail: (Long) -> Unit,
    onFlowerDetail: (String) -> Unit,
    homeViewModel: HomeViewModel,
    orderViewModel: OrderViewModel
) {
    // call API in this section

    val isRefreshing by homeViewModel.isRefreshing.collectAsStateWithLifecycle()
    val pullToRefreshState = rememberPullToRefreshState()

    val storeProductState by homeViewModel.storeProductState.collectAsStateWithLifecycle(
        initialValue = ResultResponse.None
    )
    val isStoreProductLoading = storeProductState is ResultResponse.Loading ||
            storeProductState is ResultResponse.None

    val filteredOrdersState by orderViewModel.filteredOrdersState.collectAsStateWithLifecycle(
        initialValue = ResultResponse.None
    )
    val isFilteredOrdersLoading = filteredOrdersState is ResultResponse.Loading ||
            filteredOrdersState is ResultResponse.None

    val newOrders by orderViewModel.newOrders.collectAsStateWithLifecycle(
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

    val storeBalance by orderViewModel.storeBalance.collectAsStateWithLifecycle()

    val storeDetailState by homeViewModel.storeDetailState.collectAsStateWithLifecycle(initialValue = ResultResponse.None)

    val isStoreDetailLoading = storeDetailState is ResultResponse.Loading ||
            storeDetailState is ResultResponse.None

    LaunchedEffect(Unit) {
        Log.e("homeScreen", "Home Screen HIABID LaunchedEffect called")
        orderViewModel.loadInitialData()
        homeViewModel.loadInitialData()
    }
//
//    LaunchedEffect(storeProductState) {
//        when (storeProductState) {
//            is ResultResponse.Success -> {
//                isStoreProductLoading = false
//            }
//
//            is ResultResponse.Loading -> {
//                isStoreProductLoading = true
//            }
//
//            is ResultResponse.Error -> {
//                isStoreProductLoading = false
//            }
//
//            else -> {}
//        }
//    }
//
//    LaunchedEffect(storeDetailState) {
//        when (storeDetailState) {
//            is ResultResponse.Success -> {
//                isStoreDetailLoading = false
//            }
//
//            is ResultResponse.Loading -> {
//                isStoreDetailLoading = true
//            }
//
//            is ResultResponse.Error -> {
//                isStoreDetailLoading = false
//            }
//
//            else -> {}
//        }
//    }
//
//    LaunchedEffect(filteredOrdersState) {
//        when (filteredOrdersState) {
//            is ResultResponse.Success -> {
//                isFilteredOrdersLoading = false
//            }
//
//            is ResultResponse.Loading -> {
//                isFilteredOrdersLoading = true
//            }
//
//            is ResultResponse.Error -> {
//                isFilteredOrdersLoading = false
//            }
//
//            else -> {}
//        }
//    }


    val productData: List<StoreProduct> = when (storeProductState) {
        is ResultResponse.Success -> {
            (storeProductState as ResultResponse.Success<StoreProductResponse>).data.data
        }

        else -> {
            emptyList()
        }
    }

    val storeDetailName: String = homeViewModel.storeDetail.value?.name ?: ""

    val orderCounts = listOf(
        newOrders.size,
        pickupOrders.size,
        deliveryOrders.size,
        completedOrders.size
    )

    Home(
        modifier = modifier,
        isStoreProductLoading = isStoreProductLoading,
        storeProductList = productData,
        storeBalance = storeBalance,
        orderCounts = orderCounts,
        storeDetailName = storeDetailName,
        isFilteredOrdersLoading = isFilteredOrdersLoading,
        isStoreDetailLoading = isStoreDetailLoading,
        onSnackClick = onSnackClick,
        isRefreshing = isRefreshing,
        pullToRefreshState = pullToRefreshState,
        onRefresh = {
            homeViewModel.refreshData()
            orderViewModel.refreshOrders()
        },
        onTipsDetail = onTipsDetail,
        onFlowerDetail = onFlowerDetail,
        onSelectedProduct = { storeProduct ->
            homeViewModel.setSelectedProduct(storeProduct)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Home(
    modifier: Modifier = Modifier,
    isStoreProductLoading: Boolean,
    storeBalance: Int,
    isRefreshing: Boolean,
    orderCounts: List<Int>,
    isFilteredOrdersLoading: Boolean,
    isStoreDetailLoading: Boolean,
    storeDetailName: String,
    pullToRefreshState: PullToRefreshState,
    storeProductList: List<StoreProduct>,
    onSnackClick: (Long, String) -> Unit,
    onRefresh: () -> Unit,
    onTipsDetail: (Long) -> Unit,
    onFlowerDetail: (String) -> Unit,
    onSelectedProduct: (StoreProduct) -> Unit
) {

    val tempOrderCounts = listOf(0, 0, 0, 0)

    FleupartSurface(
        modifier = modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(base20)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                PullToRefreshBox(
                    isRefreshing = isRefreshing,
                    onRefresh = onRefresh,
                    state = pullToRefreshState,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(base20),
                    indicator = {
                        PullToRefreshDefaults.Indicator(
                            modifier = Modifier.align(Alignment.TopCenter),
                            isRefreshing = isRefreshing,
                            state = pullToRefreshState,
                            threshold = 100.dp,
                            color = primaryLight,
                            containerColor = Color.White
                        )
                    }
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .statusBarsPadding(),
                    ) {
                        item {
                            if (isStoreDetailLoading) {
                                HeaderLoading()
                            } else {
                                Header(
                                    storeName = storeDetailName
                                )
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            if (isFilteredOrdersLoading) {
                                IncomeLoading()
                            } else {
                                Income(
                                    storeBalance = storeBalance
                                )
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            if (isFilteredOrdersLoading) {
                                OrderStatusSectionLoading(count = tempOrderCounts)
                            } else {
                                OrderStatusSection(
                                    count = orderCounts,
                                )
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            if (isStoreProductLoading) {
                                ProductListLoading()
                            } else if (storeProductList.isEmpty()) {
                                EmptyPopularProduct()
                            } else {
                                PopularProduct(
                                    flowerList = storeProductList,
                                    onNavigate = {

                                    },
                                    onFlowerClick = onFlowerDetail,
                                    onSelectedProduct = onSelectedProduct
                                )
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            TipsSection(
                                tipsList = FakeCategory.tipsList,
                                onTipClick = onTipsDetail
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Header(
    modifier: Modifier = Modifier,
    storeName: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("Welcome,")
                    }
                },
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = storeName,
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.weight(1f))
//        IconButton(onClick = { /* Handle notification click */ }) {
//            Icon(
//                imageVector = ImageVector.vectorResource(id = R.drawable.notification),
//                contentDescription = "Notification",
//                tint = Color.Black
//            )
//        }
    }
}


@Composable
private fun HeaderLoading(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("Welcome,")
                    }
                },
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .height(24.dp)
                    .width(250.dp)
                    .loadingFx()
            )

//            Text(
//                text = "Lily’s Flower House!",
//                fontSize = 24.sp,
//                color = MaterialTheme.colorScheme.primary,
//                fontWeight = FontWeight.Bold
//            )
        }
        Spacer(modifier = Modifier.weight(1f))
//        IconButton(onClick = { /* Handle notification click */ }) {
//            Icon(
//                imageVector = ImageVector.vectorResource(id = R.drawable.notification),
//                contentDescription = "Notification",
//                tint = Color.Black
//            )
//        }
    }
}


@Composable
private fun Income(
    modifier: Modifier = Modifier,
    storeBalance: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.padding(vertical = 18.dp)
        ) {
            Text(
                text = "Total Income",
                fontSize = 14.sp,
                color = Color.Black,
                fontWeight = FontWeight.Normal
            )

            Text(
                text = formatCurrency(amount = storeBalance.toLong()),
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Composable
private fun IncomeLoading(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.padding(vertical = 18.dp)
        ) {
            Text(
                text = "Total Income",
                fontSize = 14.sp,
                color = Color.Black,
                fontWeight = FontWeight.Normal
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .height(24.dp)
                    .width(200.dp)
                    .loadingFx()
            ) {
            }

        }
    }
}

@Composable
private fun OrderStatusSection(
    count: List<Int>,
) {

    val orderStatusList = listOf(
        "New Order",
        "Pickup",
        "Delivery",
        "Completed"
    )

    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .padding(vertical = 16.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Order Status",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.weight(1f))
//            Icon(
//                painter = painterResource(R.drawable.back_arrow),
//                contentDescription = "Navigate",
//                tint = Color.Black,
//                modifier = Modifier
//                    .size(18.dp)
//                    .clickable { onNavigate() }
//            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            items(orderStatusList.size) { index ->
                val item = orderStatusList[index]
                OrderStatusItem(
                    title = item,
                    count = count[index]
                )
            }
        }
    }
}


@Composable
private fun OrderStatusSectionLoading(
    count: List<Int>,
) {

    val orderStatusList = listOf(
        "New Order",
        "Pickup",
        "Delivery",
        "Completed"
    )

    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .padding(vertical = 16.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Order Status",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.weight(1f))
//            Icon(
//                painter = painterResource(R.drawable.back_arrow),
//                contentDescription = "Navigate",
//                tint = Color.Black,
//                modifier = Modifier
//                    .size(18.dp)
//                    .clickable { onNavigate() }
//            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            items(orderStatusList.size) { index ->
                OrderStatusItemLoading(
                )
            }
        }
    }
}

@Composable
private fun OrderStatusItem(
    title: String,
    count: Int
) {
    Column(
        modifier = Modifier
            .width(80.dp)
            .height(80.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(base20)
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = count.toString(),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFFD700)
        )
        Text(
            text = title,
            fontSize = 10.sp,
            color = base300
        )
    }
}


@Composable
private fun OrderStatusItemLoading(
) {
    Column(
        modifier = Modifier
            .width(80.dp)
            .height(80.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(base20)
            .loadingFx()
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
    }
}

@Composable
private fun PopularProduct(
    modifier: Modifier = Modifier,
    flowerList: List<StoreProduct>,
    onNavigate: () -> Unit,
    onSelectedProduct: (StoreProduct) -> Unit,
    onFlowerClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Best Ratings!",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Icon(
                painter = painterResource(R.drawable.back_arrow),
                contentDescription = "Navigate",
                tint = Color.Black,
                modifier = Modifier
                    .size(16.dp)
                    .clickable { onNavigate() }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(flowerList, key = { it.id }) { flower ->
                FlowerItem(
                    onFlowerClick = onFlowerClick,
                    item = flower,
                    onSelectedProduct = onSelectedProduct
                )
            }

        }
    }
}


@Composable
private fun EmptyPopularProduct(
) {
    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Best Ratings!",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Icon(
                painter = painterResource(R.drawable.back_arrow),
                contentDescription = "Navigate",
                tint = Color.Black,
                modifier = Modifier
                    .size(16.dp)
                    .clickable { }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .height(160.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "No popular products available",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Try to refresh or check back later.",
                color = base100,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}


@Composable
fun TipsSection(
    tipsList: List<TipsItem>,
    onTipClick: (Long) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
        // 🔹 Judul Section
        Text(
            text = "Tips For You",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Column {
            tipsList.forEach { item ->
                TipCard(
                    tip = item,
                    onTipClick = onTipClick
                )
            }
        }
    }
}

@Composable
fun TipCard(
    tip: TipsItem,
    onTipClick: (Long) -> Unit
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, base100),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(
                onClick = { onTipClick(tip.id) },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                painter = painterResource(tip.imageRes),
                contentDescription = "Tip Image",
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(60.dp)
                    .padding(end = 16.dp)
            )

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = tip.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = tip.description,
                    fontSize = 10.sp,
                    lineHeight = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}

