package com.course.fleupart.ui.screen.dashboard.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.course.fleupart.R
import com.course.fleupart.ui.components.CustomTopAppBar
import com.course.fleupart.ui.screen.dashboard.product.EmptyProduct
import com.course.fleupart.ui.screen.navigation.FleupartSurface
import com.course.fleupart.ui.theme.base20
import com.course.fleupart.ui.theme.primaryLight

@Composable
fun Order(
    modifier: Modifier = Modifier
) {

    Order(
        modifier = modifier,
        id = 0
    )
}

@Composable
private fun Order(
    modifier: Modifier = Modifier,
    id: Int = 0
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
                            NewOrderSection()
                        1 ->
                            OnProcessSection()
                        2 ->
                            OnPickupSection()
                        3 ->
                            OnDeliverySection()
                        4 ->
                            CompletedSection()
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
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(start = 20.dp, end = 20.dp, top = 20.dp)
    ) {
        EmptyProduct(
            icon = R.drawable.empty_new_order,
            title = "You don’t have any new order yet",
            description = "Start to promote your product!"
        )
    }
}

@Composable
private fun OnProcessSection(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(start = 20.dp, end = 20.dp, top = 20.dp)
    ) {
        EmptyProduct(
            icon = R.drawable.empty_process_order,
            title = "You don’t have any processed order yet",
            description = "Start to processing your order!"
        )
    }
}

@Composable
private fun OnDeliverySection(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(start = 20.dp, end = 20.dp, top = 20.dp)
    ) {
        EmptyProduct(
            icon = R.drawable.empty_on_order,
            title = "You currently don’t have any product on delivery",
            description = "Start to promote your product!"
        )
    }
}

@Composable
private fun OnPickupSection(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(start = 20.dp, end = 20.dp, top = 20.dp)
    ) {
        EmptyProduct(
            icon = R.drawable.empty_product,
            title = "You don't have any orders that can be picked up yet",
            description = "Start to processing your order!"
        )
    }
}

@Composable
private fun CompletedSection(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(start = 20.dp, end = 20.dp, top = 20.dp)
    ) {
        EmptyProduct(
            icon = R.drawable.empty_completed_order,
            title = "You don’t have any completed order yet",
            description = "Start to promote your product!"
        )
    }
}
