package com.course.fleupart.ui.screen.dashboard.product

import android.util.Log
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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.course.fleupart.R
import com.course.fleupart.data.model.remote.StoreProduct
import com.course.fleupart.data.model.remote.StoreProductResponse
import com.course.fleupart.ui.common.NumberPicker
import com.course.fleupart.ui.common.ResultResponse
import com.course.fleupart.ui.components.CustomTopAppBar
import com.course.fleupart.ui.components.FakeCategory
import com.course.fleupart.ui.components.MerchantCategory
import com.course.fleupart.ui.components.MerchantProduct
import com.course.fleupart.ui.components.OrderItem
import com.course.fleupart.ui.components.OrderItemCard
import com.course.fleupart.ui.components.OrderItemStatus
import com.course.fleupart.ui.components.OrderItemStatusCard
import com.course.fleupart.ui.components.SearchBar
import com.course.fleupart.ui.components.SearchBarForNavigation
import com.course.fleupart.ui.screen.dashboard.home.HomeViewModel
import com.course.fleupart.ui.screen.navigation.DetailDestinations
import com.course.fleupart.ui.screen.navigation.FleupartSurface
import com.course.fleupart.ui.theme.base100
import com.course.fleupart.ui.theme.base20
import com.course.fleupart.ui.theme.primaryLight
import kotlin.text.category

@Composable
fun Product(
    modifier: Modifier = Modifier,
    onProductDetail: (String) -> Unit,
    onEditProductDetail: (String) -> Unit,
    onFlowerStatus: (String) -> Unit,
    onSearchDetail: (String) -> Unit,
    homeViewModel: HomeViewModel
) {

    val storeProductState by homeViewModel.storeProductState.collectAsStateWithLifecycle()

    var showCircularProgress by remember { mutableStateOf(false) }

    LaunchedEffect(storeProductState) {
        when (storeProductState) {
            is ResultResponse.Success -> {
                showCircularProgress = false
            }
            is ResultResponse.Loading -> {
                showCircularProgress = true
            }
            is ResultResponse.Error -> {
                showCircularProgress = false
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

    Product(
        modifier = modifier,
        onProductDetail = onProductDetail,
        onEditProductDetail = onEditProductDetail,
        showCircularProgress = showCircularProgress,
        homeViewModel = homeViewModel,
        onSearchDetail = onSearchDetail,
        onFlowerStatus = onFlowerStatus,
        productList = productData
    )
}

@Composable
private fun Product(
    modifier: Modifier = Modifier,
    onProductDetail: (String) -> Unit,
    onEditProductDetail: (String) -> Unit,
    onFlowerStatus: (String) -> Unit,
    onSearchDetail: (String) -> Unit,
    homeViewModel: HomeViewModel,
    productList: List<StoreProduct>,
    showCircularProgress: Boolean
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

    val focusManager = LocalFocusManager.current

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
                    .clickable(
                        onClick = {
                            focusManager.clearFocus()
                        },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    )
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

                if (showCircularProgress) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(base20)
                    ) {
                        CircularProgressIndicator(color = primaryLight)
                    }
                } else {

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
                                            SearchBarForNavigation(
                                                onNavigate = {
                                                    onSearchDetail(DetailDestinations.SEARCH_PRODUCT)
                                                }
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
                                                        .padding(
                                                            horizontal = 18.dp,
                                                            vertical = 8.dp
                                                        )
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
                                                        .padding(
                                                            horizontal = 18.dp,
                                                            vertical = 8.dp
                                                        )
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
                                            onFlowerStatus = onFlowerStatus,
                                            onSelectedProduct = { storeProduct ->
                                                homeViewModel.setSelectedProduct(storeProduct)
                                            },
                                            productList = productList,
                                            homeViewModel = homeViewModel,
                                            onEditProductDetail = onEditProductDetail,
                                            showProcessingProducts = showProcessingProducts
                                        )
                                    }
                                }

                                1 -> {
                                    ListCategory(
                                        item = productList,
                                        onEditProductDetail = onEditProductDetail,
                                        homeViewModel = homeViewModel
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

@Composable
private fun ListProduct(
    modifier: Modifier = Modifier,
    listEditProduct: List<OrderItem>,
    listStatusProduct: List<OrderItemStatus>,
    onSelectedProduct : (StoreProduct) -> Unit,
    productList: List<StoreProduct>,
    onFlowerStatus: (String) -> Unit,
    homeViewModel: HomeViewModel,
    onEditProductDetail: (String) -> Unit,
    showProcessingProducts: Boolean
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(start = 20.dp, end = 20.dp, top = 20.dp)
    ) {
//        if ((showProcessingProducts && listStatusProduct.isEmpty()) || (!showProcessingProducts && listEditProduct.isEmpty())) {
//            EmptyProduct()
//        } else {
        if ((showProcessingProducts && productList.isEmpty()) || (!showProcessingProducts && productList.isEmpty())) {
            EmptyProduct()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
//                if (showProcessingProducts) {
//                    items(listStatusProduct) {
//                        OrderItemStatusCard(it)
//                    }
//                } else {
//                    items(listEditProduct) {
////                        OrderItemCard(it)
//                    }
//                }

                if (showProcessingProducts) {
                    items(productList, key = { it.id ?: it.name }) {
                        OrderItemStatusCard(item = it, onFlowerStatus = onFlowerStatus, onSelectedProduct = onSelectedProduct)
                    }
                } else {
                    items(productList, key = { it.id ?: it.name }) {
                        OrderItemCard(
                            item = it,
                            onEditItemClick = {
                                homeViewModel.setSelectedEditProduct(it)
                                onEditProductDetail(it.id)
                            }
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
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
            .fillMaxSize(),
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
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

//
//@Composable
//private fun ListCategory(
//    modifier: Modifier = Modifier,
//    item: List<StoreProduct>
//) {
//    Box(
//        modifier = modifier
//            .fillMaxSize()
//            .background(Color.White)
//    ) {
//        if (item.isEmpty()) {
//            EmptyProduct()
//        } else {
//            LazyColumn(
//                modifier = Modifier.fillMaxSize()
//            ) {
//                item.forEach { category ->
//                    item {
//                        Spacer(modifier = Modifier.height(8.dp))
//                        Row(
//                            horizontalArrangement = Arrangement.SpaceBetween,
//                            verticalAlignment = Alignment.CenterVertically,
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(start = 20.dp, top = 10.dp, end = 20.dp, bottom = 8.dp)
//                        ) {
//                            Text(
//                                text = category.title,
//                                fontSize = 18.sp,
//                                fontWeight = FontWeight.Bold,
//                                color = Color.Black,
//                            )
//                            Icon(
//                                painter = painterResource(id = R.drawable.back_arrow),
//                                contentDescription = "More",
//                                tint = Color.Black,
//                                modifier = Modifier.size(20.dp)
//                            )
//                        }
//                    }
//
//                    itemsIndexed(category.items) { index, item ->
//                        Column(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .background(Color.White)
//                                .padding(horizontal = 20.dp)
//                        ) {
//                            OrderItemCard(
//                                item = item,
//                            )
//                            Spacer(modifier = Modifier.height(8.dp))
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//


@Composable
private fun ListCategory(
    modifier: Modifier = Modifier,
    item: List<StoreProduct>,
    homeViewModel: HomeViewModel,
    onEditProductDetail: (String) -> Unit
) {
    val comparator = remember { Comparator<String> { a, b -> a.lowercase().compareTo(b.lowercase()) } }
    val groupedProducts = remember(item) {
        item
            .groupBy { product ->
                product.category?.name?.takeIf { it.isNotBlank() } ?: "Uncategorized"
            }
            .toSortedMap(comparator)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        if (groupedProducts.isEmpty()) {
            EmptyProduct()
        } else {
            val groupedEntries = remember(groupedProducts) { groupedProducts.entries.toList() }

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                groupedEntries.forEachIndexed { index, (categoryName, productsInCategory) ->
                    item(key = "header-$categoryName") {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = categoryName,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                                .padding(start = 20.dp, top = 10.dp, end = 20.dp, bottom = 8.dp)
                        )
                    }

                    itemsIndexed(
                        items = productsInCategory,
                        key = { _, product -> product.id ?: "$categoryName-${product.name}" }
                    ) { index , product ->
                        OrderItemCard(
                            modifier = Modifier.padding(horizontal = 20.dp),
                            item = product,
                            onEditItemClick = {
                                homeViewModel.setSelectedEditProduct(storeProduct = product)
                                onEditProductDetail(product.id)
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        if(index == productsInCategory.lastIndex) {
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }

                    if (index != groupedEntries.lastIndex) {
                        item {
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .background(base20)
                            )
                        }
                    }
                }

            }
        }
    }
}
