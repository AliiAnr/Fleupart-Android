package com.course.fleupart.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.course.fleupart.R
import com.course.fleupart.data.model.remote.StoreProduct
import com.course.fleupart.data.model.remote.StoreProductDataItem
import com.course.fleupart.ui.common.formatCurrency
import com.course.fleupart.ui.common.formatCurrencyFromString
import com.course.fleupart.ui.theme.base100
import com.course.fleupart.ui.theme.base500
import com.course.fleupart.ui.theme.secColor


@Composable
fun FlowerItem(
    modifier: Modifier = Modifier,
    onFlowerClick: (String) -> Unit,
    onSelectedProduct: (StoreProduct) -> Unit,
    item: StoreProduct
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary,
        ),
        modifier = modifier
            .width(140.dp)
            .clickable(
                onClick = {
                    onSelectedProduct(item)
                    onFlowerClick(item.id)
                },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            if (item.picture.isEmpty()) {
                Image(
                    painter = painterResource(id = R.drawable.placeholder),  // Use a placeholder image
                    contentDescription = "Store Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
            } else {
                AsyncImage(
                    model = item.picture[0].path,
                    contentDescription = "Store Image",
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.placeholder),
                    error = painterResource(id = R.drawable.placeholder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "${item.name} - ${item.store.name}",
                fontSize = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Medium,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.star),
                    contentDescription = "Rating",
                    tint = secColor,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${item.rating} | ${item.reviewCount}",
                    fontSize = 10.sp,
                    color = base100
                )
            }
            Text(
                text = formatCurrencyFromString(item.price),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )

        }
    }
}

@Composable
fun MerchantFlowerItem(
    onFlowerClick: () -> Unit,
    item: StoreProductDataItem
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .clickable(
                onClick = {
                    onFlowerClick()
                },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Gambar item

        if (item.picture.isNullOrEmpty()) {
//            Log.e("MerchantFlowerItem", "Image URL is null or empty")
            Image(
                painter = painterResource(id = R.drawable.placeholder),  // Use a placeholder image
                contentDescription = "Store Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(110.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
        } else {
//            Log.d("MerchantFlowerItem", "Image URL: ${item.picture}")
//            AsyncImage(
//                model = ImageRequest.Builder(LocalContext.current)
//                    .data(item.picture.firstOrNull()?.path)
//                    .crossfade(true)
//                    .memoryCachePolicy(CachePolicy.ENABLED)
//                    .build(),
//                contentDescription = "Store Image",
////                contentScale = ContentScale.Crop,
//                placeholder = painterResource(id = R.drawable.placeholder),
//                error = painterResource(id = R.drawable.placeholder),
//                modifier = Modifier
//                    .size(110.dp)
//                    .clip(RoundedCornerShape(10.dp))
//            )
            AsyncImage(
                model = item.picture.firstOrNull()?.path,
                contentDescription = null,
                placeholder = painterResource(R.drawable.placeholder),
                error       = painterResource(R.drawable.placeholder),
                contentScale = ContentScale.Crop,
                modifier    = Modifier.size(110.dp).clip(RoundedCornerShape(10.dp)),
            )
        }

        Spacer(modifier = Modifier.width(18.dp))

        // Detail item
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = item.name ?: "Flower Name",
                fontSize = 14.sp,
                color = Color.Black,
                fontWeight = FontWeight.Medium,
            )
            Text(
                text = item.description ?: "Description",
                fontSize = 12.sp,
                lineHeight = 18.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = base500,
                modifier = Modifier.height(38.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.star),
                    contentDescription = "Rating",
                    tint = secColor,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${item.rating} | ${item.reviewCount}",
                    fontSize = 10.sp,
                    color = base100
                )
            }

            Text(
                text = formatCurrencyFromString(item.price ?: "0000"),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )

        }
    }
}


@Composable
fun SearchMerchantFlowerItem(
    onFlowerClick: () -> Unit,
    item: StoreProduct
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    onFlowerClick()
                },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Gambar item

        if (item.picture.isNullOrEmpty()) {
//            Log.e("MerchantFlowerItem", "Image URL is null or empty")
            Image(
                painter = painterResource(id = R.drawable.placeholder),  // Use a placeholder image
                contentDescription = "Store Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(110.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
        } else {
//            Log.d("MerchantFlowerItem", "Image URL: ${item.picture}")
//            AsyncImage(
//                model = ImageRequest.Builder(LocalContext.current)
//                    .data(item.picture.firstOrNull()?.path)
//                    .crossfade(true)
//                    .memoryCachePolicy(CachePolicy.ENABLED)
//                    .build(),
//                contentDescription = "Store Image",
////                contentScale = ContentScale.Crop,
//                placeholder = painterResource(id = R.drawable.placeholder),
//                error = painterResource(id = R.drawable.placeholder),
//                modifier = Modifier
//                    .size(110.dp)
//                    .clip(RoundedCornerShape(10.dp))
//            )
            AsyncImage(
                model = item.picture.firstOrNull()?.path,
                contentDescription = null,
                placeholder = painterResource(R.drawable.placeholder),
                error       = painterResource(R.drawable.placeholder),
                contentScale = ContentScale.Crop,
                modifier    = Modifier.size(110.dp).clip(RoundedCornerShape(10.dp)),
            )
        }

        Spacer(modifier = Modifier.width(18.dp))

        // Detail item
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = item.name ?: "Flower Name",
                fontSize = 14.sp,
                color = Color.Black,
                fontWeight = FontWeight.Medium,
            )
            Text(
                text = item.description ?: "Description",
                fontSize = 12.sp,
                lineHeight = 18.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = base500,
                modifier = Modifier.height(38.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.star),
                    contentDescription = "Rating",
                    tint = secColor,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${item.rating} | ${item.reviewCount}",
                    fontSize = 10.sp,
                    color = base100
                )
            }

            Text(
                text = formatCurrencyFromString(item.price ?: "0000"),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )

        }
    }
}
