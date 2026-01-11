package com.course.fleupart.ui.screen.dashboard.detail.product

import android.net.Uri
import android.util.Log
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.course.fleupart.R
import com.course.fleupart.data.model.remote.CategoryDataItem
import com.course.fleupart.data.model.remote.GetAllCategoryResponse
import com.course.fleupart.data.model.remote.StoreProduct
import com.course.fleupart.ui.common.NumberPicker
import com.course.fleupart.ui.common.ResultResponse
import com.course.fleupart.ui.common.convertMinutesToHours
import com.course.fleupart.ui.components.CustomButton
import com.course.fleupart.ui.components.CustomPopUpDialog
import com.course.fleupart.ui.components.CustomTextInput
import com.course.fleupart.ui.components.CustomTopAppBar
import com.course.fleupart.ui.components.ImagePickerList
import com.course.fleupart.ui.screen.dashboard.home.HomeViewModel
import com.course.fleupart.ui.screen.dashboard.product.ProductViewModel
import com.course.fleupart.ui.screen.navigation.FleupartSurface
import com.course.fleupart.ui.theme.base20
import com.course.fleupart.ui.theme.base300
import com.course.fleupart.ui.theme.primaryLight

@Composable
fun EditProduct(
    modifier: Modifier = Modifier,
    productViewModel: ProductViewModel,
    flowerId: String,
    selectedEditProduct: StoreProduct,
    homeViewModel: HomeViewModel,
    onBackClick: () -> Unit
) {

    var showCircularProgress by remember { mutableStateOf(false) }

    var showSuccessDialog by remember { mutableStateOf(false) }

    var shouldClearForm by remember { mutableStateOf(true) }

    DisposableEffect(Unit) {
        onDispose {
            if (shouldClearForm) {
                productViewModel.clearProductForm()
                Log.e("productViewModel", "DisposableEffect: ProductViewModel called inside")
            }
            Log.e("ProductViewModel", "DisposableEffect: ProductViewModel")
        }
    }

    LaunchedEffect(selectedEditProduct) {
        selectedEditProduct.let {
            productViewModel.setInitialEditData(it)
        }
    }

    val categoryState by productViewModel.categoryState.collectAsStateWithLifecycle(initialValue = ResultResponse.None)

    val updateState by productViewModel.updateProductState.collectAsStateWithLifecycle(initialValue = ResultResponse.None)
    LaunchedEffect(Unit) {
        productViewModel.getAllCategory()
    }

    val originalPrice = remember(selectedEditProduct) {
        runCatching { selectedEditProduct?.price?.toBigDecimal()?.stripTrailingZeros()?.toPlainString() }
            .getOrDefault(selectedEditProduct?.price ?: "")
    }

    LaunchedEffect(updateState) {
        when (updateState) {
            is ResultResponse.Success -> {
                showCircularProgress = false
                showSuccessDialog = true
            }
            is ResultResponse.Loading -> {
                showCircularProgress = true
            }
            is ResultResponse.Error -> {
                showCircularProgress = false
                Log.e("EditProduct", "Error: ${(updateState as ResultResponse.Error).error}")
            }
            else -> {}
        }
    }


    val categoryList = when (categoryState) {
        is ResultResponse.Success -> (categoryState as ResultResponse.Success<GetAllCategoryResponse>).data.data
        else -> emptyList()
    }

    val isLoading = categoryState is ResultResponse.Loading ||
            (categoryState is ResultResponse.None)

    val isNameChanged = productViewModel.nameValue != (selectedEditProduct?.name ?: "")
    val isDescChanged = productViewModel.descriptionValue != (selectedEditProduct?.description ?: "")
    val isPriceChanged = productViewModel.priceValue != originalPrice
    val isStockChanged = productViewModel.stockValue != (selectedEditProduct?.stock?.toString() ?: "")
    val isPointChanged = productViewModel.pointValue != (selectedEditProduct?.point?.toString() ?: "")
    val isTimeChanged = productViewModel.arrangeTimeValue != (selectedEditProduct?.arrangeTime ?: "")
    val isCategoryChanged = productViewModel.categoryIdValue != (selectedEditProduct?.category?.id ?: "")

    // Image Logic: Changed if new images added OR existing images removed
    val isNewImagesAdded = productViewModel.productImages.isNotEmpty()
    val isExistingImagesRemoved = productViewModel.existingImageUrls.size != (selectedEditProduct?.picture?.size ?: 0)
    val isImageChanged = isNewImagesAdded || isExistingImagesRemoved

    val isChanged = isNameChanged || isDescChanged || isPriceChanged || isStockChanged ||
            isPointChanged || isTimeChanged || isCategoryChanged || isImageChanged

    val isAllFieldsFilled = productViewModel.nameValue.isNotBlank() &&
            productViewModel.stockValue.isNotBlank() &&
            productViewModel.descriptionValue.isNotBlank() &&
            productViewModel.arrangeTimeValue.isNotBlank() &&
            productViewModel.pointValue.isNotBlank() &&
            productViewModel.priceValue.isNotBlank() &&
            productViewModel.categoryIdValue.isNotBlank() &&
            (productViewModel.productImages.isNotEmpty() || productViewModel.existingImageUrls.isNotEmpty())

    val isButtonAvailable = !showCircularProgress && isChanged && isAllFieldsFilled

    EditProduct(
        productViewModel = productViewModel,
        showCircularProgress = showCircularProgress,
        isLoading = isLoading,
        isButtonAvailable = isButtonAvailable,
        showSuccessDialog = showSuccessDialog,
        onSuccessDialogDismiss = {
            productViewModel.resetUpdateState()
            productViewModel.clearProductForm()
            homeViewModel.refreshDataAfterUpdate()
            onBackClick()
            onBackClick()
        },
        onSaveClick = {
            productViewModel.updateProductNew()
        },
        categoryList = categoryList,
        onBackClick = {
            productViewModel.resetUpdateState()
            productViewModel.clearProductForm() // Clean up when leaving
            onBackClick()
        },
    )
}



@Composable
private fun EditProduct(
    modifier: Modifier = Modifier,
    productViewModel: ProductViewModel,
    isButtonAvailable: Boolean,
    onSaveClick: () -> Unit,
    showCircularProgress: Boolean,
    isLoading: Boolean,
    showSuccessDialog: Boolean,
    onSuccessDialogDismiss: () -> Unit,
    categoryList: List<CategoryDataItem>,
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

    var showConfirmDialog by remember { mutableStateOf(false) }

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
                    title = "Edit Product",
                    showNavigationIcon = true,
                    onBackClick = onBackClick
                )
                HorizontalDivider(
                    color = base20,
                    thickness = 8.dp
                )

                if (isLoading) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        CircularProgressIndicator(color = primaryLight)
                    }
                } else {

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
                                    value = productViewModel.nameValue,
                                    onChage = productViewModel::setName,
                                    placeHolder = "Lily Flower",
                                    borderColor = Color.Black,
                                )
                            }

                            item {
                                EditItem(
                                    label = "Short Description",
                                    value = productViewModel.descriptionValue,
                                    onChage = productViewModel::setDescription,
                                    isLongText = true,
                                    placeHolder = "This is a flower made from ...",
                                    height = 100.dp,
                                    borderColor = Color.Black,
                                )
                            }

                            item {
                                EditItem(
                                    label = "Price",
                                    value = productViewModel.priceValue,
                                    onChage = productViewModel::setPrice,
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
                                CustomDropdownMenu(
                                    modifier = Modifier.fillMaxWidth(),
                                    categoryList = categoryList,
                                    productViewModel = productViewModel
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                            }

                            item {
                                EditItem(
                                    label = "Arrange Time",
                                    value = productViewModel.arrangeTimeValue,
                                    onChage = productViewModel::setArrangeTime,
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
                                    Text(
                                        text = "Images",
                                        color = Color.Black,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.W600,
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))

                                    // 1. Existing Images (URLs)
                                    if (productViewModel.existingImageUrls.isNotEmpty()) {
                                        LazyRow(
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            modifier = Modifier.padding(bottom = 8.dp)
                                        ) {
                                            items(productViewModel.existingImageUrls) { url ->
                                                Box(
                                                    modifier = Modifier
                                                        .height(100.dp)
                                                        .width(100.dp)
                                                        .clip(RoundedCornerShape(10.dp))

                                                ) {
                                                    AsyncImage(
                                                        model = url,
                                                        contentDescription = "Existing Image",
                                                        contentScale = ContentScale.Crop,
                                                        modifier = Modifier.fillMaxSize()
                                                    )
                                                    // Delete Button for Existing Image

                                                    Box(
                                                        modifier = Modifier
                                                            .align(Alignment.TopEnd)
                                                            .padding(4.dp)
                                                            .size(20.dp)
                                                            .background(Color.Gray.copy(alpha = 0.5f), CircleShape)
                                                            .clickable { productViewModel.removeExistingImage(url) },
                                                        contentAlignment = Alignment.Center
                                                    ) {
                                                        Icon(
                                                            painter = painterResource(R.drawable.close_icon),
                                                            contentDescription = "Remove Image",
                                                            tint = Color.Black,
                                                            modifier = Modifier.size(12.dp)
                                                        )
                                                    }

                                                }
                                            }
                                        }
                                    }

                                    // 2. New Images Picker (URIs)
                                    val imageList = productViewModel.productImages
                                    ImagePickerList(
                                        label = if(productViewModel.existingImageUrls.isEmpty()) "Add Images" else "Add More Images",
                                        imageUri = imageList,
                                        onImagePicked = productViewModel::addProductImage,
                                        onImageRemoved = { removedUri ->
                                            val index = imageList.indexOf(removedUri)
                                            if (index != -1) {
                                                productViewModel.removeProductImage(index)
                                            }
                                        },
                                        onImageRetaken = { index, uri ->
                                            productViewModel.replaceProductImage(index, uri)
                                        }
                                    )
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                            }

                            item {
                                EditItem(
                                    label = "Stock",
                                    value = productViewModel.stockValue,
                                    onChage = productViewModel::setStock,
                                    placeHolder = "10",
                                    keyboardType = KeyboardType.NumberPassword,
                                    borderColor = Color.Black,
                                )
                            }

                            item {
                                EditItem(
                                    label = "Point",
                                    value = productViewModel.pointValue,
                                    onChage = productViewModel::setPoint,
                                    placeHolder = "200",
                                    keyboardType = KeyboardType.NumberPassword,
                                    borderColor = Color.Black,
                                )
                            }

//                            item {
//                                HorizontalDivider(
//                                    color = base20,
//                                    thickness = 8.dp
//                                )
//
//                                PreOrderSwitch(
//                                    isChecked = productViewModel.isPreOrderValue,
//                                    onCheckedChange = productViewModel::setPreOrder
//                                )
//
//                                HorizontalDivider(
//                                    color = base20,
//                                    thickness = 8.dp
//                                )
//                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .height(90.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CustomButton(
                            isAvailable = isButtonAvailable,
                            text = "Save Changes",
                            onClick = {
                                showConfirmDialog = true
                            }
                        )
                    }
                }
            }

            if (showCircularProgress) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f))
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            // Do nothing - this prevents clicks from passing through
                        }
                ) {
                    CircularProgressIndicator(color = primaryLight)
                }
            }

            if (showSuccessDialog) {
                CustomPopUpDialog(
                    onDismiss = {
                        onSuccessDialogDismiss()
                    },
                    isShowIcon = true,
                    isShowTitle = true,
                    isShowDescription = true,
                    isShowButton = false,
                    icon = {
                        Box(
                            modifier = Modifier
                                .size(80.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ceklist),
                                contentDescription = null,
                                tint = Color.Unspecified,
                            )
                        }
                    },
                    title = "Update Product Successful",
                    description = "Your product has been updated successfully.",
                )
            }

            if (showConfirmDialog) {
                CustomPopUpDialog(
                    onDismiss = { showConfirmDialog = false },
                    isShowIcon = true,
                    isShowTitle = true,
                    isShowDescription = false,
                    isShowButton = true,
                    icon = {

                        Icon(
                            painter = painterResource(id = R.drawable.think),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier.height(150.dp)
                        )
                    },
                    title = "Are you sure you want to update the product?",
                    buttons = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            OutlinedButton(
                                onClick = { showConfirmDialog = false },
                                border = BorderStroke(1.dp, primaryLight),
                                shape = RoundedCornerShape(28.dp),
                                modifier = Modifier.weight(1f).padding(end = 6.dp)
                            ) {
                                Text("Cancel", color = primaryLight)
                            }
                            Button(
                                onClick = {
                                    focusManager.clearFocus()
                                    onSaveClick()
                                    showConfirmDialog = false
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = primaryLight
                                ),
                                shape = RoundedCornerShape(28.dp),
                                modifier = Modifier.weight(1f).padding(start = 6.dp)
                            ) {
                                Text("Confirm", color = Color.White)
                            }
                        }
                    }
                )
            }
        }
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
