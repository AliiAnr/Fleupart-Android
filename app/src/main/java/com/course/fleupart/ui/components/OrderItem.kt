package com.course.fleupart.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
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
import com.course.fleupart.data.model.remote.OrderDataItem
import com.course.fleupart.data.model.remote.OrderItemsItem
import com.course.fleupart.ui.common.extractOrderId
import com.course.fleupart.ui.common.formatCurrencyFromString
import com.course.fleupart.ui.theme.base100
import com.course.fleupart.ui.theme.base40
import com.course.fleupart.ui.theme.base500
import com.course.fleupart.ui.theme.err
import com.course.fleupart.ui.theme.tert


@Composable
fun CreatedOrderSummary(
    orderItem: OrderDataItem,
    onSelectedOrderItem: (OrderDataItem) -> Unit
) {
    val isCompleted: Boolean = orderItem.payment?.status == "paid"

    val paymentMethod = when (orderItem.payment?.methode) {
        "qris" -> "QRIS"
        "cash" -> "CASH"
        else -> "Unknown Payment Method"
    }

    Column(
        modifier = Modifier
            .padding(top = 8.dp)
            .background(
                Color.White
            )
            .clickable(
                onClick = {
                    onSelectedOrderItem(orderItem)
                },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.order_number),
                        contentDescription = null,
                        tint = base500,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "#${extractOrderId(orderItem.id ?: "0001")}",
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Icon(
                    painter = painterResource(id = R.drawable.back_arrow),
                    contentDescription = "Store Icon",
                    tint = Color.Black,
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            orderItem.orderItems?.forEach { item ->
                item?.let {
                    OrderedItemCard(item)
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Payment Method:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = base100
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.cash),
                            contentDescription = null,
                            tint = base500,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = paymentMethod,
                            color = Color.Black,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                HorizontalDivider(color = base40, thickness = 1.dp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Total Order:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = base100
                    )

                    Text(
                        text = formatCurrencyFromString(orderItem.total.toString()),
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                HorizontalDivider(color = base40, thickness = 1.dp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(5.dp))
                    .height(25.dp)
                    .width(120.dp)
                    .background(if (isCompleted) tert else err),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isCompleted) "Paid" else "Unpaid",
                    color = Color.White,
                    fontSize = 14.sp,
                )
            }
        }
    }
}


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
                error = painterResource(R.drawable.placeholder),
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


@Composable
fun OrderSummaryItem(
    modifier: Modifier = Modifier,
    quantity: Int = 0,
    name: String = "",
    description: String = "",
    price: String
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = "$quantity" + "x",
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.size(8.dp))
            Column(
            ) {
                Text(
                    text = name,
                    color = Color.Black,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = description,
                    color = base100,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 12.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier
                        .heightIn(max = 50.dp)
                        .padding(end = 20.dp)
                )
            }
        }

        Text(
            text = formatCurrencyFromString(price),
            color = Color.Black,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
