package com.course.fleupart.ui.screen.dashboard.detail.Profile

import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.course.fleupart.R
import com.course.fleupart.data.model.remote.StoreDetailData
import com.course.fleupart.data.model.remote.StoreProductDataItem
import com.course.fleupart.data.model.remote.StoreProductsResponse
import com.course.fleupart.ui.common.toFormattedAddress
import com.course.fleupart.ui.components.MerchantFlowerItem
import com.course.fleupart.ui.components.MerchantProduct
import com.course.fleupart.ui.screen.dashboard.profile.ProfileViewModel
import com.course.fleupart.ui.screen.navigation.FleupartSurface
import com.course.fleupart.ui.theme.base20
import com.course.fleupart.ui.theme.base40
import com.course.fleupart.ui.theme.base500

@Composable
fun StoreView(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    profileViewModel: ProfileViewModel
) {

    val storeData = remember {
        profileViewModel.storeInformationValue.value?.let {
            StoreDetailData(
                id = it.id ?: "",
                name = it.name ?: "",
                description = it.description ?: "",
                phone = it.phone ?: "",
                logo = it.logo ?: "",
                picture = it.picture ?: "",
                operationalDay = it.operationalDay ?: "",
                operationalHour = it.operationalHour ?: "",
                address = it.address,
            )
        }
    }

    val storeProduct = remember {
        profileViewModel.storeProductsValue.value?.data
    }

    Merchant(
        modifier = modifier,
        storeData = storeData,
        storeProduct = storeProduct,
        onBackClick = onBackClick,
    )

}

@Composable
private fun Merchant(
    modifier: Modifier = Modifier,
    storeData: StoreDetailData?,
    storeProduct: List<StoreProductDataItem?>?,
    onBackClick: () -> Unit,
) {

    val groupedByCat = remember(storeProduct) {
        storeProduct
            ?.filterNotNull()
            ?.groupBy { it.category?.name ?: "Uncategorized" }
            ?.toSortedMap(compareBy { it })
            ?: sortedMapOf()
    }


    FleupartSurface(
        modifier = modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .background(base20),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {

                    item {
                        DescMerchant(
                            storeData = storeData,
                            onBackClick = onBackClick
                        )
                    }

                    if (storeProduct.isNullOrEmpty()){

                    } else {

                        groupedByCat.forEach { (categoryName, itemsInCat) ->
                            // Header kategori
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
                                        .padding(start = 20.dp, top = 20.dp, end = 20.dp)
                                )
                            }

                            items(
                                items = itemsInCat,
                                key = { it.id ?: "id-$categoryName-$it" }
                            ) { product ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.White)
                                        .padding(horizontal = 20.dp)
                                ) {
                                    MerchantFlowerItem(
                                        item = product,
                                        onFlowerClick = {
//                                        homeViewModel.setSelectedProduct(product)
//                                        onFlowerClick(
//                                            product.id,
//                                            DetailDestinations.DETAIL_MERCHANT
//                                        )
                                        }
                                    )
                                    // Divider kecuali item terakhir
                                    if (product != itemsInCat.last()) {
                                        HorizontalDivider(color = base40)
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .background(Color.White)
                        )
                    }

                }
            }
        }
    }
}

@Composable
private fun DescMerchant(
    modifier: Modifier = Modifier,
    storeData: StoreDetailData?,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        ) {

            if (storeData?.picture.isNullOrEmpty()) {
                Log.e("WOIII KOSONG", "${storeData?.picture}")
                Image(
                    painter = painterResource(id = R.drawable.placeholder),  // Use a placeholder image
                    contentDescription = "Store Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                )
            } else {
                Log.e("WOIII ADA", storeData.picture)
                AsyncImage(
//                    model = ImageRequest.Builder(LocalContext.current)
//                        .data(storeData?.picture)
//                        .crossfade(true)
//                        .build(),
                    model = storeData.picture,
                    contentDescription = "Store Image",
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.placeholder),
                    error = painterResource(id = R.drawable.placeholder),
                    modifier = Modifier
                        .fillMaxSize()
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            Color.White,
                            shape = RoundedCornerShape(50.dp)
                        )
                        .clickable(
                            onClick = { onBackClick() },
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.back_arrow),
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier
                            .size(20.dp)
                            .graphicsLayer(rotationZ = 180f)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            Color.White,
                            shape = RoundedCornerShape(50.dp)
                        )
                        .clickable(
                            onClick = { },
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.share),
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier
                            .size(20.dp)
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(20.dp)
        ) {
            Text(
                text = storeData?.name ?: "STORE NAME",
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = storeData?.description
                    ?: "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                color = base500,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.loc),
                    contentDescription = "Location",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = storeData?.address?.toFormattedAddress() ?: "Address not available",
                    color = base500,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W700
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.bubble_chat),
                    contentDescription = "Rating",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = storeData?.phone ?: "Phone not available",
                    color = base500,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W700
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(20.dp)
        ) {
            Text(
                text = "Opening Hours",
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.calendar),
                    contentDescription = "Rating",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = storeData?.operationalDay ?: "Not specified",
                    color = base500,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W700
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.clock),
                    contentDescription = "Rating",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = storeData?.operationalHour ?: "Not specified",
                    color = base500,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W700
                )
            }
        }
    }
}
