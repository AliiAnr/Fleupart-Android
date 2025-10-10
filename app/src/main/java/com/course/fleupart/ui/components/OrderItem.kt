package com.course.fleupart.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.course.fleupart.R
import com.course.fleupart.data.model.remote.OrderItemsItem
import com.course.fleupart.ui.common.formatCurrencyFromString
import com.course.fleupart.ui.theme.base100
import com.course.fleupart.ui.theme.base40

@Composable
fun OrderedItemCard(
    item: OrderItemsItem
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, base40, shape = RoundedCornerShape(10.dp))
            .height(100.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (item.product?.picture.isNullOrEmpty()) {
            Image(
                painter = painterResource(id = R.drawable.placeholder),
                contentDescription = "Store Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(110.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
        } else {
            AsyncImage(
                model = item.product.picture[0]?.path,
                contentDescription = item.product.name,
                placeholder = painterResource(R.drawable.placeholder),
                error       = painterResource(R.drawable.placeholder),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(start = 12.dp)
                    .size(80.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
        }

        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = item.product?.name ?: "Unknown Product",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 16.sp
                )
                Text(text = "${item.quantity} item", color = base100, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = formatCurrencyFromString(item.product?.price ?: "999999"),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}
