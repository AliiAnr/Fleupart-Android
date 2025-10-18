package com.course.fleupart.ui.screen.dashboard.home

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.course.fleupart.data.model.remote.StoreProduct
import com.course.fleupart.data.model.remote.StoreProductResponse
import com.course.fleupart.ui.common.ResultResponse
import com.course.fleupart.ui.common.formatCurrency
import com.course.fleupart.ui.components.FakeCategory
import com.course.fleupart.ui.components.Flower
import com.course.fleupart.ui.components.FlowerItem
import com.course.fleupart.ui.components.ProductListLoading
import com.course.fleupart.ui.components.TipsItem
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
    homeViewModel: HomeViewModel
) {
    // call API in this section

    val isRefreshing by homeViewModel.isRefreshing.collectAsStateWithLifecycle()
    val pullToRefreshState = rememberPullToRefreshState()

    val storeProductState by homeViewModel.storeProductState.collectAsStateWithLifecycle(initialValue = ResultResponse.None)
    var isStoreProductLoading by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        homeViewModel.loadInitialData()
    }

    LaunchedEffect(storeProductState) {
        when (storeProductState) {
            is ResultResponse.Success -> {
               isStoreProductLoading = false
            }

            is ResultResponse.Loading -> {
                isStoreProductLoading = true
            }

            is ResultResponse.Error -> {
                isStoreProductLoading = false
            }

            else -> {}
        }
    }

    val productData: List<StoreProduct> = when (storeProductState) {
        is ResultResponse.Success -> {
            (storeProductState as ResultResponse.Success<StoreProductResponse>).data.data
        }
        else -> {
            emptyList()
        }
    }

    Home(
        modifier = modifier,
        isStoreProductLoading = isStoreProductLoading,
        storeProductList = productData,
        onSnackClick = onSnackClick,
        isRefreshing = isRefreshing,
        pullToRefreshState = pullToRefreshState,
        onRefresh = {
            homeViewModel.refreshData()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Home(
    modifier: Modifier = Modifier,
    isStoreProductLoading: Boolean,
    isRefreshing: Boolean,
    pullToRefreshState: PullToRefreshState,
    storeProductList: List<StoreProduct>,
    onSnackClick: (Long, String) -> Unit,
    onRefresh: () -> Unit,
) {
    var textState by remember { mutableStateOf("") }

    val temp = listOf(
        1,
        2,
        5,
        10
    )

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
                            Header()
                        }

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Income()
                        }

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            OrderStatusSection(
                                count = temp,
                                onNavigate = {

                                }
                            )
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
                                    onItemClick = { id, name ->

                                    }
                                )
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            TipsSection(
                                tipsList = FakeCategory.tipsList
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
            Text(
                text = "Lily’s Flower House!",
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
        IconButton(onClick = { /* Handle notification click */ }) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.notification),
                contentDescription = "Notification",
                tint = Color.Black
            )
        }
    }
}

@Composable
private fun Income(
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
            modifier = Modifier.padding(vertical = 18.dp)
        ) {
            Text(
                text = "Today's Income",
                fontSize = 14.sp,
                color = Color.Black,
                fontWeight = FontWeight.Normal
            )

            Text(
                text = formatCurrency(1000000),
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun OrderStatusSection(
    count: List<Int>,
    onNavigate: () -> Unit
) {

    val orderStatusList = listOf(
        "New Order",
        "On Delivery",
        "Completed",
        "Product Rate"
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
private fun OrderStatusItem(
    title: String,
    count: Int
) {
    Column(
        modifier = Modifier
            .width(80.dp)
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
private fun PopularProduct(
    modifier: Modifier = Modifier,
    flowerList: List<StoreProduct>,
    onNavigate: () -> Unit,
    onItemClick: (Long, String) -> Unit
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
                    onFlowerClick = { _, _ ->
                    },
                    item = flower,
                    setSelectedProduct = {}
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
                    .clickable {  }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier.fillMaxSize().height(160.dp),
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
    tipsList: List<TipsItem>
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
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Column(     ) {
            tipsList.forEach { item ->
                TipCard(item)
            }
        }
    }
}

@Composable
fun TipCard(
    tip: TipsItem
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, base100),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
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

