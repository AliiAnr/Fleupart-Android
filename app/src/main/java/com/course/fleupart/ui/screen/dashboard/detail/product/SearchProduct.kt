package com.course.fleupart.ui.screen.dashboard.detail.product

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.course.fleupart.data.model.remote.StoreProduct
import com.course.fleupart.data.model.remote.StoreProductResponse
import com.course.fleupart.ui.common.ResultResponse
import com.course.fleupart.ui.components.CustomTopAppBar
import com.course.fleupart.ui.components.OrderItemCard
import com.course.fleupart.ui.components.SearchBar
import com.course.fleupart.ui.components.SearchBarForNavigation
import com.course.fleupart.ui.screen.dashboard.home.HomeViewModel
import com.course.fleupart.ui.screen.dashboard.product.EmptyProduct
import com.course.fleupart.ui.screen.navigation.FleupartSurface
import com.course.fleupart.ui.theme.base20
import com.course.fleupart.ui.theme.primaryLight
import kotlinx.coroutines.delay

@Composable
fun SearchProduct (
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel,
    onEditProductDetail: (String) -> Unit,
    onBackClick: () -> Unit
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

    SearchProduct(
        modifier = modifier,
        showCircularProgress = showCircularProgress,
        productData = productData,
        homeViewModel = homeViewModel,
        onEditProductDetail = onEditProductDetail,
        onBackClick = onBackClick
    )

}

@Composable
private fun SearchProduct (
    modifier: Modifier = Modifier,
    showCircularProgress: Boolean,
    productData: List<StoreProduct>,
    homeViewModel: HomeViewModel,
    onEditProductDetail: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    var query by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var filtered by remember { mutableStateOf(productData) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    LaunchedEffect(query, productData) {
        isLoading = true
        delay(1500)
        filtered = if (query.isBlank()) {
            productData
        } else {
            productData.filter { product ->
                product.name?.contains(query, ignoreCase = true) == true
            }
        }
        isLoading = false
    }

    FleupartSurface(
        modifier = modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    onClick = {
                        focusManager.clearFocus()
                    },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                )
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CustomTopAppBar(
                    title = "Search",
                    showNavigationIcon = true,
                    onBackClick = {
                         onBackClick()
                        focusManager.clearFocus()
                    }
                )


                Column(
                    modifier = Modifier
                        .background(Color.White)
                        .padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    SearchBar(
                        query = query,
                        onSearch = {},
                        onQueryChange = {
                            query = it
                        },
                        focusRequester = focusRequester
                    )
                }

                if (showCircularProgress || isLoading) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(if (isLoading) Color.White else base20)
                    ) {
                        CircularProgressIndicator(color = primaryLight)
                    }
                } else if (filtered.isEmpty()) {
                    EmptyProduct(
                        title = "No product found",
                        description = "Try another keyword"
                    )
                }
                else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                            .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filtered, key = { it.id ?: it.name }) { product ->
                            OrderItemCard(
                                item = product,
                                onEditItemClick = {
                                    homeViewModel.setSelectedEditProduct(product)
                                    onEditProductDetail(product.id)
                                }
                            )
                        }
                    }
                }
            }

        }
    }
}