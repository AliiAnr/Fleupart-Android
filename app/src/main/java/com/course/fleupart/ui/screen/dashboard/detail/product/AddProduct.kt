package com.course.fleupart.ui.screen.dashboard.detail.product

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.course.fleupart.R
import com.course.fleupart.ui.common.CustomBottomSheet
import com.course.fleupart.ui.common.NumberPicker
import com.course.fleupart.ui.common.convertMinutesToHours
import com.course.fleupart.ui.components.CustomButton
import com.course.fleupart.ui.components.CustomTextInput
import com.course.fleupart.ui.components.CustomTopAppBar
import com.course.fleupart.ui.components.ImagePickerCard
import com.course.fleupart.ui.components.ImagePickerList
import com.course.fleupart.ui.screen.navigation.FleupartSurface
import com.course.fleupart.ui.theme.base20
import com.course.fleupart.ui.theme.base300
import com.course.fleupart.ui.theme.primaryLight
import kotlin.rem

@Composable
fun AddProduct(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit
) {

    AddProduct(
        id = 0,
        onBackClick = onBackClick,
    )
}



@Composable
private fun AddProduct(
    modifier: Modifier = Modifier,
    id: Int = 0,
    onBackClick: () -> Unit
) {
    var temp by remember { mutableStateOf("") }
    var arrangeValue by remember { mutableIntStateOf(0) }

    var productName by remember { mutableStateOf("") }
    var shortDescription by remember {mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var arrangeTime by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }

    var imageList by remember { mutableStateOf(listOf<Uri>()) }

    var isPreOrder by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

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
                    .background(base20),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CustomTopAppBar(
                    title = "Add Product",
                    showNavigationIcon = true,
                    onBackClick = onBackClick
                )
                HorizontalDivider(
                    color = base20,
                    thickness = 8.dp
                )
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White),
                    ) {
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            EditItem(
                                label = "Product Name",
                                value = productName,
                                onChage = {
                                    productName = it
                                },
                                placeHolder = "Lily Flower",
                                borderColor = Color.Black,
                            )
                        }

                        item {
                            EditItem(
                                label = "Short Description",
                                value = shortDescription,
                                onChage = {
                                    shortDescription = it
                                },
                                isLongText = true,
                                placeHolder = "This is a flower made from ...",
                                height = 100.dp,
                                borderColor = Color.Black,
                            )
                        }

                        item {
                            EditItem(
                                label = "Price",
                                value = price,
                                onChage = {
                                    price = it
                                },
                                leadingText = "Rp",
                                placeHolder = "10000",
                                borderColor = Color.Black,
                                keyboardType = KeyboardType.NumberPassword,
                            )
                        }

                        item {
                            Text(
                                text = "Category",
                                color = Color.Black,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W600,
                                modifier = Modifier.padding(horizontal = 20.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            CustomDropdownMenu()
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        item {
                            EditItem(
                                label = "Arrange Time",
                                value = arrangeTime,
                                onChage = {
                                    arrangeTime = it
                                },
                                placeHolder = "30 - 60 minutes",
                                borderColor = Color.Black,
                                keyboardType = KeyboardType.NumberPassword,
                            )
                        }

//                        item {
//                            Text(
//                                text = "Arrange Time",
//                                color = Color.Black,
//                                fontSize = 14.sp,
//                                fontWeight = FontWeight.W600,
//                                modifier = Modifier.padding(horizontal = 20.dp)
//                            )
//                            Spacer(modifier = Modifier.height(4.dp))
//                            ArrangeTime(
//                                onSelectedValue = {
//                                    arrangeValue = it
//                                },
//                                arrangeValue = arrangeValue
//                            )
//                            Spacer(modifier = Modifier.height(16.dp))
//                        }

                        item {
                            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                                ImagePickerList(
                                    label = "Image",
                                    imageUri = imageList,
                                    onImagePicked = {
                                        imageList = imageList + it
                                    },
                                    onImageRemoved = { removedUri ->
                                        imageList = imageList.filter { it != removedUri }
                                    }
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        item {
                            EditItem(
                                label = "Stock",
                                value = stock,
                                onChage = {
                                    stock = it
                                },
                                placeHolder = "10",
                                keyboardType = KeyboardType.NumberPassword,
                                borderColor = Color.Black,
                            )
                        }

                        item {
                            HorizontalDivider(
                                color = base20,
                                thickness = 8.dp
                            )

                            PreOrderSwitch(
                                isChecked = isPreOrder,
                                onCheckedChange = {
                                    isPreOrder = it
                                }
                            )

                            HorizontalDivider(
                                color = base20,
                                thickness = 8.dp
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CustomButton(
                        text = "Add Product",
                        onClick = {

                        }
                    )
                }
            }
        }
    }
}

@Composable
fun PreOrderSwitch(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Pre-Order",
            color = Color.Black,
            fontSize = 14.sp,
            fontWeight = FontWeight.W600,
        )

        Spacer(modifier = Modifier.weight(1f))

        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFFBCE455),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color.LightGray,
                uncheckedBorderColor = Color.LightGray,

            )
        )
    }
}
@Composable
private fun EditItem(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    height: Dp = 50.dp,
    leadingIcon: Int? = null,
    leadingText: String? = null,
    borderColor: Color,
    placeHolder: String = "",
    isError: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    errorMessage: String = "",
    isLongText: Boolean = false, // Tambahkan parameter ini
    onChage: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            color = Color.Black,
            fontSize = 14.sp,
            fontWeight = FontWeight.W600,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        CustomTextInput(
            value = value,
            onChange = onChage,
            placeholder = placeHolder,
            isError = isError,
            errorMessage = errorMessage,
            leadingIcon = leadingIcon,
            leadingText = leadingText,
            borderColor = borderColor,
            keyboardType = keyboardType,
            height = height,
            isLongText = isLongText // Tambahkan parameter ini
        )
    }
}


@Composable
private fun ArrangeTime(
    modifier: Modifier = Modifier,
    onSelectedValue: (Int) -> Unit,
    arrangeValue: Int
) {
    var isDialogOpen by remember { mutableStateOf(false) }
    var currentValue by remember { mutableIntStateOf(arrangeValue) }
    var arrangeTime by remember { mutableStateOf(convertMinutesToHours(arrangeValue)) }

    Box(
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .border(
                width = 1.dp,
                color = Color.Black,
                shape = RoundedCornerShape(10.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    onClick = { isDialogOpen = true },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = arrangeTime,
                color = Color.Black,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 12.dp)
            )
        }
    }

    if (isDialogOpen) {
        AlertDialog(
            onDismissRequest = { isDialogOpen = false },
            title = { Text("Edit Value", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        NumberPicker(
                            range = IntRange(0, 100).filter { it % 10 == 0 }
                                .let { IntRange(it.first(), it.last()) },
                            initialValue = currentValue,
                            onValueChange = {
                                currentValue = it
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    Row {
                        Button(
                            onClick = {
                                isDialogOpen = false
                            },
                            colors = ButtonDefaults.buttonColors(primaryLight),
                            modifier = Modifier.padding(top = 16.dp)
                        ) {
                            Text("Cancel")
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(
                            onClick = {
                                isDialogOpen = false
                                arrangeTime = convertMinutesToHours(currentValue)
                                onSelectedValue(currentValue)
                            },
                            colors = ButtonDefaults.buttonColors(primaryLight),
                            modifier = Modifier.padding(top = 16.dp)
                        ) {
                            Text("Confirm")
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {}
        )
    }
}

@Composable
fun CustomDropdownMenu(
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf("Graduation") }
    val menuItems = listOf(
        Pair(Icons.Default.Home, "Graduation"),
        Pair(Icons.Default.Person, "Birthday"),
        Pair(Icons.Default.ShoppingCart, "Cart"),
        Pair(Icons.Default.Settings, "Settings"),
        Pair(Icons.Default.Call, "Calls"),
        Pair(Icons.Default.Email, "Emails")
    )

    Box(
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .border(
                width = 1.dp,
                color = Color.Black,
                shape = RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.Center
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    onClick = { expanded = true },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedItem,
                color = Color.Black,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 12.dp)
            )

            Icon(
                painter = painterResource(id = R.drawable.back_arrow),
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier
                    .padding(end = 12.dp)
                    .graphicsLayer(rotationZ = if (expanded) 270f else 90f)
            )

        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            offset = DpOffset(x = 0.dp, y = 700.dp),
            containerColor = Color.Transparent,
            tonalElevation = 0.dp,
            shadowElevation = 0.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .background(Color.White, shape = RoundedCornerShape(12.dp))
                .border(
                    width = 1.dp,
                    color = base300,
                    shape = RoundedCornerShape(12.dp)
                ),
        ) {
            menuItems.forEach { (icon, title) ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = title,
                            fontSize = 16.sp,
                            color = Color.Black,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    },
                    onClick = {
                        expanded = false
                        selectedItem = title
                    }
                )
                if (title != menuItems.last().second) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                }
            }
        }
    }
}