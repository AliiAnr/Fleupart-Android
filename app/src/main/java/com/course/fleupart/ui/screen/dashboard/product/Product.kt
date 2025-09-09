package com.course.fleupart.ui.screen.dashboard.product

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.course.fleupart.R
import com.course.fleupart.ui.common.NumberPicker
import com.course.fleupart.ui.components.CustomTopAppBar
import com.course.fleupart.ui.components.FakeCategory
import com.course.fleupart.ui.components.MerchantCategory
import com.course.fleupart.ui.components.MerchantProduct
import com.course.fleupart.ui.components.OrderItem
import com.course.fleupart.ui.components.OrderItemCard
import com.course.fleupart.ui.components.OrderItemStatus
import com.course.fleupart.ui.components.OrderItemStatusCard
import com.course.fleupart.ui.components.SearchBar
import com.course.fleupart.ui.screen.navigation.FleupartSurface
import com.course.fleupart.ui.theme.base100
import com.course.fleupart.ui.theme.base20
import com.course.fleupart.ui.theme.primaryLight

@Composable
fun Product(
    modifier: Modifier = Modifier,
    onProductDetail: (String) -> Unit
) {

    Product(
        modifier = modifier,
        onProductDetail = onProductDetail,
        id = 0
    )
}

@Composable
private fun Product(
    modifier: Modifier = Modifier,
    onProductDetail: (String) -> Unit,
    id: Int = 0
) {
    val tabItems: List<String> = listOf(
        "All Products", "Categories"
    )

    val context = LocalContext.current

    var selectedTabIndex by remember { mutableIntStateOf(0) }

    var showProcessingProducts by remember { mutableStateOf(false) }

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
                    title = "Product",
                )

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
                        .padding(top = 8.dp)
                ) { page ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        when (page) {
                            0 -> {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {

                                    Column(
                                        modifier = Modifier
                                            .background(Color.White)
                                            .padding(vertical = 12.dp)
                                    ) {
                                        SearchBar(
                                            placeholder = "Search your product",
                                            query = "",
                                            onQueryChange = {},
                                            onSearch = {}
                                        )
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 20.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .width(160.dp)
                                                    .clip(RoundedCornerShape(50.dp))
                                                    .background(primaryLight)
                                                    .padding(horizontal = 18.dp, vertical = 8.dp)
                                                    .clickable(
                                                        onClick = {
                                                            onProductDetail("addProduct")
                                                        },
                                                        indication = null,
                                                        interactionSource = remember { MutableInteractionSource() }
                                                    ),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Row(
                                                    horizontalArrangement = Arrangement.Center,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Icon(
                                                        painter = painterResource(id = R.drawable.add_icon),
                                                        contentDescription = "Add",
                                                        tint = Color.Unspecified
                                                    )
                                                    Spacer(modifier = Modifier.width(8.dp))
                                                    Text(
                                                        text = "Add Product",
                                                        color = Color.White,
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 14.sp
                                                    )
                                                }

                                            }

                                            Box(
                                                modifier = Modifier
                                                    .width(160.dp)
                                                    .clip(RoundedCornerShape(50.dp))
                                                    .background(
                                                        if (!showProcessingProducts) primaryLight else Color(
                                                            0xFFF59DC6
                                                        )
                                                    )
                                                    .padding(horizontal = 18.dp, vertical = 8.dp)
                                                    .clickable(
                                                        onClick = {
                                                            showProcessingProducts =
                                                                !showProcessingProducts
                                                        },
                                                        indication = null,
                                                        interactionSource = remember { MutableInteractionSource() }
                                                    ),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Row(
                                                    horizontalArrangement = Arrangement.Center,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Icon(
                                                        painter = painterResource(id = R.drawable.more_icon),
                                                        contentDescription = "Add",
                                                        tint = Color.Unspecified
                                                    )
                                                    Spacer(modifier = Modifier.width(8.dp))
                                                    Text(
                                                        text = "Show Detail",
                                                        color = Color.White,
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 14.sp
                                                    )
                                                }
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    ListProduct(
                                        listEditProduct = FakeCategory.editProduct,
                                        listStatusProduct = FakeCategory.productStatus,
                                        showProcessingProducts = showProcessingProducts
                                    )
                                }
                            }

                            1 -> {
                                ListCategory(
                                    item = FakeCategory.merchantCategory
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ListProduct(
    modifier: Modifier = Modifier,
    listEditProduct: List<OrderItem>,
    listStatusProduct: List<OrderItemStatus>,
    showProcessingProducts: Boolean
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(start = 20.dp, end = 20.dp, top = 20.dp)
    ) {
        if ((showProcessingProducts && listStatusProduct.isEmpty()) || (!showProcessingProducts && listEditProduct.isEmpty())) {
            EmptyProduct()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (showProcessingProducts) {
                    items(listStatusProduct) {
                        OrderItemStatusCard(it)
                    }
                } else {
                    items(listEditProduct) {
                        OrderItemCard(it)
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyProduct(
    modifier: Modifier = Modifier,
    title: String = "You have not added any product yet",
    icon: Int = R.drawable.empty_product,
    description: String = "Start adding your product now!"
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "Empty Product",
                tint = Color.Unspecified,
                modifier = Modifier.width(250.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = title,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = description,
                color = base100,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun ListCategory(
    modifier: Modifier = Modifier,
    item: List<MerchantCategory>
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        if (item.isEmpty()) {
            EmptyProduct()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item.forEach { category ->
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 20.dp, top = 10.dp, end = 20.dp, bottom = 8.dp)
                        ) {
                            Text(
                                text = category.title,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                            )
                            Icon(
                                painter = painterResource(id = R.drawable.back_arrow),
                                contentDescription = "More",
                                tint = Color.Black,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    itemsIndexed(category.items) { index, item ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                                .padding(horizontal = 20.dp)
                        ) {
                            OrderItemCard(
                                item = item,
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

