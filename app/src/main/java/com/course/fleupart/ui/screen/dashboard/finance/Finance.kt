package com.course.fleupart.ui.screen.dashboard.finance

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.course.fleupart.R
import com.course.fleupart.data.model.remote.OrderDataItem
import com.course.fleupart.ui.common.ResultResponse
import com.course.fleupart.ui.common.formatCurrency
import com.course.fleupart.ui.components.CustomTopAppBar
import com.course.fleupart.ui.components.FakeCategory
import com.course.fleupart.ui.components.RecentSales
import com.course.fleupart.ui.components.WithdrawData
import com.course.fleupart.ui.screen.dashboard.order.OrderViewModel
import com.course.fleupart.ui.screen.navigation.FleupartSurface
import com.course.fleupart.ui.screen.navigation.MainDestinations
import com.course.fleupart.ui.theme.base20
import com.course.fleupart.ui.theme.base40
import com.course.fleupart.ui.theme.primaryLight
import network.chaintech.kmp_date_time_picker.ui.date_range_picker.formatToString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Finance(
    modifier: Modifier = Modifier,
    onCompletedOrderDetail: () -> Unit,
    orderViewModel: OrderViewModel
) {

    val filteredOrdersState by orderViewModel.filteredOrdersState.collectAsStateWithLifecycle(
        initialValue = ResultResponse.None
    )

    val isRefreshing by orderViewModel.isRefreshing.collectAsStateWithLifecycle()

    val completedPaidOrders by orderViewModel.completedPaidOrders.collectAsStateWithLifecycle(
        initialValue = emptyList()
    )

    val storeOrdersCount by orderViewModel.allOrdersCount.collectAsStateWithLifecycle()

    val storeBalance by orderViewModel.storeBalance.collectAsStateWithLifecycle()

    val pullToRefreshState = rememberPullToRefreshState()

    var showCircularProgress by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
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
                Log.e(
                    "ORDER_SCREEN",
                    "Error: ${(filteredOrdersState as ResultResponse.Error).error}"
                )
            }

            else -> {}
        }
    }

    Finance(
        modifier = modifier,
        completedPaidOrders = completedPaidOrders,
        showCircularProgress = showCircularProgress,
        pullToRefreshState = pullToRefreshState,
        storeBalance = storeBalance,
        storeOrdersCount = storeOrdersCount,
        isRefreshing = isRefreshing,
        onWithdrawClick = {},
        onRecentSalesClick = {},
        onCompletedOrderDetail = {
            onCompletedOrderDetail()
            orderViewModel.setSelectedCompletedOrderItem(it)
        },
        onRefresh = {
            orderViewModel.refreshOrders()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Finance(
    modifier: Modifier = Modifier,
    completedPaidOrders: List<OrderDataItem>,
    showCircularProgress: Boolean,
    pullToRefreshState: PullToRefreshState,
    storeBalance: Int,
    storeOrdersCount: Int,
    isRefreshing: Boolean,
    onWithdrawClick: () -> Unit,
    onRecentSalesClick: () -> Unit,
    onCompletedOrderDetail: (OrderDataItem) -> Unit,
    onRefresh: () -> Unit,
) {
    val tabItems: List<String> = listOf(
        "Sales Data", "Withdraw Data"
    )

    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val pagerState = rememberPagerState {
        tabItems.size
    }

    LaunchedEffect(selectedTabIndex) {
        pagerState.animateScrollToPage(selectedTabIndex)
    }

    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress) {
            selectedTabIndex = pagerState.currentPage
        }
    }

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
                    title = "Finance",
                )
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
                    if (showCircularProgress) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            CircularProgressIndicator(color = primaryLight)
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Spacer(modifier = Modifier.height(8.dp))
                            BalanceSection(
                                balance = storeBalance.toLong(),
                                onWithdrawClick = {
                                }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            TabRow(
                                selectedTabIndex = selectedTabIndex,
                                containerColor = Color.White
                            ) {
                                tabItems.forEachIndexed { index, item ->
                                    Tab(
                                        text = {
                                            Text(
                                                text = item,
                                                color = Color.Black,
                                                style = LocalTextStyle.current.copy(
                                                    color = Color.Black,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 14.sp,
                                                    textAlign = TextAlign.Start
                                                )
                                            )
                                        },
                                        selected = selectedTabIndex == index,
                                        onClick = {
                                            selectedTabIndex = index
                                        },
                                        selectedContentColor = Color.White
                                    )
                                }
                            }

                            HorizontalPager(
                                state = pagerState,
                                modifier = Modifier
                                    .fillMaxWidth()
                                .weight(1f)
                            ) { page ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    when (page) {
                                        0 -> {
                                            SalesDataSection(
                                                recentSalesList = completedPaidOrders,
                                                allOrdersCount = storeOrdersCount,
                                                onCompletedOrderDetail = onCompletedOrderDetail
                                            )
                                        }

                                        1 -> {
                                            WithdrawDataSection(
                                                recentWithdrawData = FakeCategory.withdrawData
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WithdrawDataSection(
    modifier: Modifier = Modifier,
    recentWithdrawData: List<WithdrawData>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 20.dp),
        ) {

            item {
                Spacer(modifier = Modifier.height(12.dp))
            }

            items(
                items = recentWithdrawData
            ) {
                RecentWithdrawItem(
                    item = it
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun SalesDataSection(
    modifier: Modifier = Modifier,
    recentSalesList: List<OrderDataItem>,
    onCompletedOrderDetail: (OrderDataItem) -> Unit,
    allOrdersCount: Int
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            item {
                Spacer(modifier = Modifier.height(12.dp))
                SalesReportSection(
                    onViewSalesClick = {

                    },
                    allOrdersCount = allOrdersCount
                )
            }

            item {
                Text(
                    text = "Recent Sales",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                )
            }

            items(items = recentSalesList) { item ->
                RecentSalesItem(
                    item = item,
                    onCompletedOrderDetail = onCompletedOrderDetail
                )
                Spacer(modifier = Modifier.height(8.dp))
            }


        }
    }
}


@Composable
fun BalanceSection(
    balance: Long,
    onWithdrawClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Total Balance",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = formatCurrency(balance),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = primaryLight
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Button(
                onClick = onWithdrawClick,
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryLight),
                modifier = Modifier.height(40.dp)
            ) {
                Text(
                    text = "Withdraw Balance",
                    fontSize = 14.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }

        }
    }
}

@Composable
private fun RecentSalesItem(
    modifier: Modifier = Modifier,
    item: OrderDataItem,
    onCompletedOrderDetail: (OrderDataItem) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(border = BorderStroke(1.dp, base40), shape = RoundedCornerShape(14.dp))
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .height(76.dp)
            .clickable(
                onClick = {
                    onCompletedOrderDetail(item)
                },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(
                text = item.orderItems?.get(0)?.let { "${it.quantity ?: 999}x ${it.product?.name ?: "Product Name"}" } ?: "Product Name",
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = formatCurrency(item.total?.toLong() ?: 0),
                color = primaryLight,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        Column(
            modifier = Modifier
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .background(Color(0xFFFFD700), RoundedCornerShape(14.dp))
                    .padding(horizontal = 28.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = item.payment?.methode ?: "Payment",
                    fontSize = 12.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun RecentWithdrawItem(
    modifier: Modifier = Modifier,
    item: WithdrawData
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(border = BorderStroke(1.dp, base40), shape = RoundedCornerShape(14.dp))
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .height(76.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(
                text = item.date.formatToString("MMMM dd yyyy"),
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = formatCurrency(item.amount),
                color = primaryLight,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        Column(
            modifier = Modifier
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .background(Color(0xFFFFD700), RoundedCornerShape(14.dp))
                    .padding(horizontal = 28.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = item.paymentMethod,
                    fontSize = 12.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun SalesReportSection(
    modifier: Modifier = Modifier,
    onViewSalesClick: (Int) -> Unit,
    allOrdersCount: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .border(border = BorderStroke(1.dp, base40), shape = RoundedCornerShape(14.dp))
                .padding(22.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.chart_square),
                    contentDescription = "Sales Report",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "View Sales Report",
                    fontSize = 14.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Box(
            modifier = Modifier
                .border(border = BorderStroke(1.dp, base40), shape = RoundedCornerShape(10.dp))
                .padding(22.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "All-time Order",
                    fontSize = 14.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$allOrdersCount Products",
                    fontSize = 16.sp,
                    color = primaryLight,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

