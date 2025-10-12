package com.course.fleupart.ui.screen.dashboard.detail.order

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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.course.fleupart.R
import com.course.fleupart.data.model.remote.OrderDataItem
import com.course.fleupart.ui.common.OrderDummyData
import com.course.fleupart.ui.common.extractOrderId
import com.course.fleupart.ui.common.formatCurrency
import com.course.fleupart.ui.common.parseDateTime
import com.course.fleupart.ui.components.CustomTopAppBar
import com.course.fleupart.ui.components.OrderSummaryItem
import com.course.fleupart.ui.screen.dashboard.order.OrderViewModel
import com.course.fleupart.ui.screen.navigation.FleupartSurface
import com.course.fleupart.ui.theme.base100
import com.course.fleupart.ui.theme.base20
import com.course.fleupart.ui.theme.base40
import com.course.fleupart.ui.theme.base500
import com.course.fleupart.ui.theme.err
import com.course.fleupart.ui.theme.primaryLight
import com.course.fleupart.ui.theme.tert
import java.util.Locale


@Composable
fun DetailOrderItem(
    modifier: Modifier = Modifier,
//    selectedOrderItem: OrderDataItem,
    orderViewModel: OrderViewModel,
    onBackClick: () -> Unit,
) {

    val dummyOrder = OrderDummyData.newOrdersDummy[1]

    var showCircularProgress by remember { mutableStateOf(false) }

    DetailOrderItem(
        selectedOrderItem = dummyOrder, // Use updated item
        showCircularProgress = showCircularProgress,
        onBackClick = {
            onBackClick()
        },
    )
}

@Composable
private fun DetailOrderItem(
    modifier: Modifier = Modifier,
    selectedOrderItem: OrderDataItem,
    showCircularProgress: Boolean,
//    orderData: List<CartItem>? = null,
    onBackClick: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    // State untuk menyimpan status yang dipilih di UI
    var selectedStatus by remember { mutableStateOf(selectedOrderItem.status ?: "Pending") }

    // Gunakan LaunchedEffect untuk memastikan state UI selalu sinkron dengan data awal
    LaunchedEffect(selectedOrderItem.status) {
        selectedStatus = selectedOrderItem.status ?: "created"
    }

    FleupartSurface(
        modifier = modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    focusManager.clearFocus()
                },
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .background(base20),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CustomTopAppBar(
                    title = "Detail Order",
                    showNavigationIcon = true,
                    onBackClick = onBackClick
                )

                if (showCircularProgress) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                // Do nothing - this prevents clicks from passing through
                            }
                    ) {
                        CircularProgressIndicator(color = primaryLight)
                    }
                } else {

                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {

                            //Harusnya ada address tetapi belum bisa di GET di API
//                            item {
//                                Spacer(modifier = Modifier.height(8.dp))
//                                DetailOrderAddressSection(
//                                    selectedAddress = selectedOrderItem
//                                )
//                            }

//                            item {
//                                Spacer(modifier = Modifier.height(8.dp))
//                                DetailOrderMerchantProfileSection(
//                                    storeData = selectedOrderItem
//                                )
//                            }

                            item {
                                Spacer(modifier = Modifier.height(8.dp))
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.White)
                                        .padding(horizontal = 20.dp, vertical = 12.dp),
                                ) {
                                    Text(
                                        text = "Order Summary",
                                        color = Color.Black,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )

                                    selectedOrderItem.orderItems?.forEachIndexed { index, item ->
                                        val isLastItem =
                                            index == selectedOrderItem.orderItems.size - 1
                                        OrderSummaryItem(
                                            quantity = item?.quantity ?: 0,
                                            name = item?.product?.name ?: "",
                                            description = item?.product?.description ?: "",
                                            price = item?.product?.price ?: ""
                                        )
                                        if (!isLastItem) {
                                            HorizontalDivider(
                                                color = base40,
                                            )
                                        }
                                    }
                                }
                            }

                            item {
                                Spacer(modifier = Modifier.height(8.dp))
                                DateAndTimeDisplay(
                                    selectedOrderItem = selectedOrderItem
                                )
                            }

                            item {
                                Spacer(modifier = Modifier.height(8.dp))
                                NoteSection(
                                    note = selectedOrderItem.note ?: "-"
                                )
                            }

                            item {
                                Spacer(modifier = Modifier.height(8.dp))
                                PaymentSummary(
                                    selectedOrderItem = selectedOrderItem
                                )
                            }

                            item {
                                Spacer(modifier = Modifier.height(8.dp))
                                DetailOrderTotalPriceSection(
                                    selectedOrderItem = selectedOrderItem
                                )
                            }

                            item {
                                Spacer(modifier = Modifier.height(8.dp))
                                SetStatusSection(
                                    currentStatus = selectedStatus,
                                    selectedOrderItem = selectedOrderItem,
                                    onStatusChange = { newStatus ->
                                        selectedStatus = newStatus
                                        // TODO: Panggil fungsi ViewModel untuk memanggil API di sini
                                        // Contoh:
                                        // orderViewModel.updateOrderStatus(selectedOrderItem.id, newStatus)
                                        Log.d("StatusUpdate", "Status baru dipilih: $newStatus untuk Order ID: ${selectedOrderItem.id}")
                                    }
                                )
                            }

                        }
                    }
                }
            }

        }
    }
}

//
//@Composable
//private fun DetailOrderAddressSection(
//    modifier: Modifier = Modifier,
//    selectedAddress: OrderAddressData
//) {
//    Row(
//        modifier = modifier
//            .fillMaxWidth()
//            .background(Color.White)
//            .padding(horizontal = 20.dp, vertical = 12.dp),
//        horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//        Row(
//            modifier = Modifier.weight(1f),
//        ) {
//
//            Icon(
//                painter = painterResource(id = R.drawable.loc),
//                contentDescription = null,
//                tint = MaterialTheme.colorScheme.primary,
//                modifier = Modifier.size(18.dp)
//            )
//            Spacer(modifier = Modifier.size(8.dp))
//            Column(
//            ) {
//                Text(
//                    text = "Destination Address",
//                    color = Color.Black,
//                    fontSize = 14.sp,
//                    fontWeight = FontWeight.Bold
//                )
//                Column {
//                    Text(
//                        text = selectedAddress.name,
//                        color = Color.Black,
//                        fontSize = 16.sp,
//                        fontWeight = FontWeight.Normal
//                    )
//                    Text(
//                        text = selectedAddress.phone,
//                        color = Color.DarkGray,
//                        fontSize = 14.sp,
//                        fontWeight = FontWeight.Normal
//                    )
//                    Text(
//                        text = buildString {
//                            append(selectedAddress.detail)
//                            if (selectedAddress.detail != "") append(", ")
//                            append(selectedAddress.road)
//                            append(", ")
//                            append(selectedAddress.district)
//                            append(", ")
//                            append(selectedAddress.city)
//                            append(", ")
//                            append(selectedAddress.province)
//                            append(" ")
//                            append(selectedAddress.postcode)
//                        },
//                        color = Color.Black,
//                        fontSize = 14.sp,
//                        fontWeight = FontWeight.Normal,
//                        modifier = Modifier.padding(vertical = 4.dp)
//                    )
//                }
//            }
//        }
//    }
//}

@Composable
private fun NoteSection(
    modifier: Modifier = Modifier,
    note: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Text(
            text = "Note",
            color = Color.Black,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = note.ifEmpty { "-" },
            color = Color.Black,
            fontSize = 12.sp,
            lineHeight = 20.sp,
            modifier = Modifier.fillMaxWidth()
        )
    }

}

@Composable
private fun DateAndTimeDisplay(
    modifier: Modifier = Modifier,
    selectedOrderItem: OrderDataItem
) {

    val (pickupDate, pickupTime) = parseDateTime(selectedOrderItem.takenDate ?: "-")

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 12.dp),
    ) {
        Column(
        ) {
            Text(
                text = "Pickup Date",
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.calendar_edit),
                        contentDescription = null,
                        tint = base500,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = "$pickupDate",
                        color = base100,
                        fontSize = 12.sp,
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Column(

        ) {
            Text(
                text = "Pickup Time",
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.clock),
                        contentDescription = null,
                        tint = base500,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "$pickupTime",
                        color = base100,
                        fontSize = 12.sp,
                        modifier = Modifier
                    )
                }
            }
        }
    }
}

@Composable
private fun PaymentSummary(
    modifier: Modifier = Modifier,
    selectedOrderItem: OrderDataItem
) {

    val paymentMethod = when (selectedOrderItem.payment?.methode) {
        "qris" -> "QRIS"
        "cash" -> "CASH"
        else -> "Unknown Payment Method"
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Text(
            text = "Payment Method",
            color = Color.Black,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
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
        Text(
            text = "Order Number",
            color = Color.Black,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
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
                text = "#${extractOrderId(selectedOrderItem.id ?: "0000")}",
                color = Color.Black,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun DetailOrderTotalPriceSection(
    modifier: Modifier = Modifier,
    selectedOrderItem: OrderDataItem
) {

    val subtotal = selectedOrderItem.total

    val deliveryFee = if (selectedOrderItem.takenMethod == "delivery") 15000 else 0


    val paymentStatus = when (selectedOrderItem.payment?.status) {
        "paid" -> "Paid"
        "unpaid" -> "Unpaid"
        else -> "Unknown Status"
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Total Order",
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )
            Text(
                text = formatCurrency(subtotal?.toLong() ?: 666),
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Delivery Fee",
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )
            Text(
                text = formatCurrency(deliveryFee.toLong()),
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Total Payment",
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = formatCurrency(subtotal?.toLong() ?: 777),
                color = MaterialTheme.colorScheme.primary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Payment Status",
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = paymentStatus,
                color = if (selectedOrderItem.payment?.status == "paid") tert else err,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(26.dp))
    }
}

@Composable
private fun SetStatusSection(
    currentStatus: String,
    selectedOrderItem: OrderDataItem,
    onStatusChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Set Status",
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        StatusDropdown(
            currentStatus = currentStatus,
            onStatusSelected = onStatusChange,
            takenMethod = selectedOrderItem.takenMethod
        )
    }
}


@Composable
fun StatusDropdown(
    currentStatus: String,
    onStatusSelected: (String) -> Unit,
    takenMethod: String?,
    modifier: Modifier = Modifier
) {
    val statusDisplayMap = mapOf(
        "created" to "Created",
        "process" to "Process",
        "pickup" to "Pickup",
        "delivery" to "Delivery",
        "completed" to "Completed"
    )

    val statusApiMap = mapOf(
        "Created" to "created",
        "Process" to "process",
        "Pickup" to "pickup",
        "Delivery" to "delivery",
        "Completed" to "completed"
    )

    val baseOptions = listOf("Created", "Process", "Pickup", "Delivery", "Completed")

    val statusOptions = when (takenMethod?.lowercase()) {
        "delivery" -> baseOptions.filter { it != "Pickup" }
        "pickup" -> baseOptions.filter { it != "Delivery" }
        else -> baseOptions
    }

    var isExpanded by remember { mutableStateOf(false) }

    val displayStatus = statusDisplayMap[currentStatus.lowercase()] ?: currentStatus.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
    }

    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .width(150.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    color = primaryLight,
                    shape = RoundedCornerShape(12.dp)
                )
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    isExpanded = true
                }
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = displayStatus,
                color = Color.White,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = "Dropdown Arrow",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }

        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
            modifier = Modifier
                .width(150.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            statusOptions.forEach { status ->
                val isCurrentStatus = status.equals(displayStatus, ignoreCase = true)
                DropdownMenuItem(
                    text = {
                        Text(
                            text = status,
                            color = if (isCurrentStatus) primaryLight else Color.Black,
                            fontWeight = if (isCurrentStatus) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 14.sp
                        )
                    },
                    onClick = {
                        isExpanded = false
                        if (!status.equals(displayStatus, ignoreCase = true)) {
                            val apiStatus = statusApiMap[status] ?: status.lowercase()
                            onStatusSelected(apiStatus)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            if (isCurrentStatus) base20 else Color.Transparent
                        )
                )
            }
        }
    }
}