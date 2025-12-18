package com.course.fleupart.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.course.fleupart.R
import com.course.fleupart.data.model.remote.StoreProduct
import com.course.fleupart.ui.common.formatCurrency
import com.course.fleupart.ui.common.formatCurrencyFromString
import com.course.fleupart.ui.theme.base100
import com.course.fleupart.ui.theme.base40
import com.course.fleupart.ui.theme.tert

@Composable
fun OrderItemCard(
    modifier: Modifier = Modifier,
    item: StoreProduct,
    onEditItemClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, base40, shape = RoundedCornerShape(10.dp))
            .padding(horizontal = 18.dp, vertical = 8.dp)
            .height(100.dp)
            .clickable(
                onClick = onEditItemClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (item.picture[0].path.isNullOrBlank()) {
            Image(
                painter = painterResource(id = R.drawable.placeholder),
                contentDescription = "Flower Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
        } else {
            AsyncImage(
                model = item.picture[0].path,
                contentDescription = null,
                placeholder = painterResource(R.drawable.placeholder),
                error = painterResource(R.drawable.placeholder),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
        }

        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.padding(start = 8.dp))
        {
            Text(
                text = item.name,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = formatCurrencyFromString(item.price),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.edit_product),
                    contentDescription = item.name,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Edit",
                    color = base100,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}

@Composable
fun OrderItemStatusCard(item: StoreProduct) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, base40, shape = RoundedCornerShape(10.dp))
            .padding(horizontal = 18.dp, vertical = 8.dp)
            .height(100.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (item.picture[0].path.isNullOrBlank()) {
            Image(
                painter = painterResource(id = R.drawable.placeholder),
                contentDescription = "Flower Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
        } else {
            AsyncImage(
                model = item.picture[0].path,
                contentDescription = null,
                placeholder = painterResource(R.drawable.placeholder),
                error = painterResource(R.drawable.placeholder),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
        }

        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.padding(start = 8.dp))
        {
            Text(
                text = item.name,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = formatCurrencyFromString(item.price),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "Status: ",
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Accepted",
                    color = tert,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

            }
        }
    }
}