import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.course.fleupart.R
import com.course.fleupart.ui.theme.primaryLight

//package com.course.fleupart.ui.screen.dashboard.detail.Profile
//
//import android.net.Uri
//import android.util.Log
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.interaction.MutableInteractionSource
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxHeight
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.statusBarsPadding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.Icon
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalFocusManager
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.unit.Dp
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import coil3.compose.AsyncImage
//import com.course.fleupart.R
//import com.course.fleupart.data.model.remote.StoreAddress
//import com.course.fleupart.data.model.remote.StoreDetailData
//import com.course.fleupart.ui.common.ResultResponse
//import com.course.fleupart.ui.components.CustomButton
//import com.course.fleupart.ui.components.CustomPopUpDialog
//import com.course.fleupart.ui.components.CustomTextInput
//import com.course.fleupart.ui.components.CustomTopAppBar
//import com.course.fleupart.ui.components.ImagePickerCard
//import com.course.fleupart.ui.screen.dashboard.profile.ProfileViewModel
//import com.course.fleupart.ui.screen.navigation.FleupartSurface
//import com.course.fleupart.ui.theme.base20
//import com.course.fleupart.ui.theme.primaryLight
//
//
//// Store Header Photo
////== Section 1
//// Store Name
//// Store Description
//// Store Address
//// Store Phone Number
//
////Section 2
//// Title: Opening Hours
//// Opening Day Monday - Friday
//// Opening Hour 08.00 - 20.00
//
//
//@Composable
//fun StoreProfileDetail(
//    modifier
//    : Modifier = Modifier,
//    onBackClick: () -> Unit,
//    profileViewModel: ProfileViewModel,
//) {
//
//    val updateAddressState by profileViewModel.updateStoreAddressState.collectAsStateWithLifecycle(
//        initialValue = ResultResponse.None
//    )
//
//    var showCircularProgress by remember { mutableStateOf(false) }
//    var showSuccessDialog by remember { mutableStateOf(false) }
//
//    LaunchedEffect(updateAddressState) {
//        when (updateAddressState) {
//            is ResultResponse.Success -> {
//                showCircularProgress = false
//                showSuccessDialog = true
//
//                Log.e("AddProfileScreen", "User detail: $updateAddressState")
//
//            }
//
//            is ResultResponse.Loading -> {
//                showCircularProgress = true
//            }
//
//            is ResultResponse.Error -> {
//                showCircularProgress = false
//                Log.e(
//                    "AddAddressScreen",
//                    "addAddress error: ${(updateAddressState as ResultResponse.Error).error}"
//                )
//            }
//
//            else -> {}
//        }
//    }
//
//    val initialData = remember {
//        profileViewModel.storeAddressValue.value?.let { data ->
//            StoreAddress(
//                name = data.name ?: "",
//                phone = data.phone ?: "",
//                province = data.province ?: "",
//                city = data.city ?: "",
//                district = data.district ?: "",
//                road = data.road ?: "",
//                detail = data.detail ?: "",
//                postcode = data.postcode ?: ""
//            )
//        }
//    }
//
//    var name by remember { mutableStateOf(profileViewModel.storeAddressValue.value?.name ?: "") }
//    var phone by remember { mutableStateOf(profileViewModel.storeAddressValue.value?.phone ?: "") }
//
//    var province by remember {
//        mutableStateOf(
//            profileViewModel.storeAddressValue.value?.province ?: ""
//        )
//    }
//    var cityOrDistrict by remember {
//        mutableStateOf(
//            profileViewModel.storeAddressValue.value?.city ?: ""
//        )
//    }
//    var subDistrict by remember {
//        mutableStateOf(
//            profileViewModel.storeAddressValue.value?.district ?: ""
//        )
//    }
//    var postalCode by remember {
//        mutableStateOf(
//            profileViewModel.storeAddressValue.value?.postcode ?: ""
//        )
//    }
//    var streetName by remember {
//        mutableStateOf(
//            profileViewModel.storeAddressValue.value?.road ?: ""
//        )
//    }
//    var additionalDetail by remember {
//        mutableStateOf(
//            profileViewModel.storeAddressValue.value?.detail ?: ""
//        )
//    }
//
//    // Cek apakah ada perubahan data dibandingkan data awal
//    val isChanged = remember(
//        name,
//        phone,
//        province,
//        cityOrDistrict,
//        subDistrict,
//        streetName,
//        additionalDetail,
//        postalCode
//    ) {
//        name != (initialData?.name ?: "") ||
//                phone != (initialData?.phone ?: "") ||
//                province != (initialData?.province ?: "") ||
//                cityOrDistrict != (initialData?.city ?: "") ||
//                subDistrict != (initialData?.district ?: "") ||
//                streetName != (initialData?.road ?: "") ||
//                additionalDetail != (initialData?.detail ?: "") ||
//                postalCode != (initialData?.postcode ?: "")
//    }
//
//    val isAllFieldsFilled = name.isNotBlank() &&
//            phone.isNotBlank() &&
//            province.isNotBlank() &&
//            cityOrDistrict.isNotBlank() &&
//            subDistrict.isNotBlank() &&
//            postalCode.isNotBlank() &&
//            streetName.isNotBlank() &&
//            additionalDetail.isNotBlank()
//
//
//    val isButtonAvailable = !showCircularProgress && isChanged && isAllFieldsFilled
//
//    StoreProfileDetail(
//        modifier = modifier,
//        showSuccessDialog = showSuccessDialog,
//        showCircularProgress = showCircularProgress,
//        isButtonAvailable = isButtonAvailable,
//        name = name,
//        phone = phone,
//        province = province,
//        cityOrDistrict = cityOrDistrict,
//        subDistrict = subDistrict,
//        postalCode = postalCode,
//        streetName = streetName,
//        additionalDetail = additionalDetail,
//        onBackClick = onBackClick,
//        onNameChange = { name = it },
//        onPhoneChange = { phone = it },
//        onProvinceChange = { province = it },
//        onCityOrDistrictChange = { cityOrDistrict = it },
//        onSubDistrictChange = { subDistrict = it },
//        onPostalCodeChange = { postalCode = it },
//        onStreetNameChange = { streetName = it },
//        onAdditionalDetailChange = { additionalDetail = it },
//        onSaveClick = {
//            profileViewModel.updateStoreAddress(
//                name = name,
//                phone = phone,
//                province = province,
//                road = streetName,
//                city = cityOrDistrict,
//                district = subDistrict,
//                postcode = postalCode,
//                detail = additionalDetail,
//                latitude = 0.0,
//                longitude = 0.0
//            )
//        },
//        onDismiss = {
//            showSuccessDialog = false
//            onBackClick()
//            profileViewModel.resetUpdateState()
//        }
//    )
//}
//
//@Composable
//private fun StoreProfileDetail(
//    modifier: Modifier = Modifier,
//    showSuccessDialog: Boolean,
//    showCircularProgress: Boolean = false,
//    isButtonAvailable: Boolean = true,
//    name: String,
//    phone: String,
//    province: String,
//    cityOrDistrict: String,
//    subDistrict: String,
//    postalCode: String,
//    streetName: String,
//    additionalDetail: String,
//    onBackClick: () -> Unit,
//    onNameChange: (String) -> Unit,
//    onPhoneChange: (String) -> Unit,
//    onProvinceChange: (String) -> Unit,
//    onCityOrDistrictChange: (String) -> Unit,
//    onSubDistrictChange: (String) -> Unit,
//    onPostalCodeChange: (String) -> Unit,
//    onStreetNameChange: (String) -> Unit,
//    onAdditionalDetailChange: (String) -> Unit,
//    onSaveClick: () -> Unit,
//    onDismiss: () -> Unit
//) {
//
//    FleupartSurface(
//        modifier = modifier.fillMaxSize(),
//    ) {
//        val focusManager = LocalFocusManager.current
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .clickable(
//                    // Hapus fokus jika area lain diklik
//                    indication = null, // Tidak ada animasi klik
//                    interactionSource = remember { MutableInteractionSource() }
//                ) {
//                    focusManager.clearFocus()
//                },
//            contentAlignment = Alignment.Center
//
//        ) {
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .statusBarsPadding()
//                    .background(base20),
//                horizontalAlignment = Alignment.CenterHorizontally,
//            ) {
//                CustomTopAppBar(
//                    title = "Address Detail",
//                    showNavigationIcon = true,
//                    onBackClick = onBackClick
//                )
//
//                Box(
//                    modifier = Modifier.weight(1f) // Membuat LazyColumn fleksibel
//                ) {
//                    LazyColumn(
//                        modifier = Modifier
//                            .fillMaxSize()
//                    ) {
//
//                        item {
//                            Spacer(modifier = Modifier.height(8.dp))
//                            HeaderContent(
//
//                            )
//                        }
//
//                        item {
//                            Spacer(modifier = Modifier.height(8.dp))
//                            FirstContent(
//                                storeName = storeName,
//                            )
//                        }
//
//                        item {
//                            Spacer(modifier = Modifier.height(8.dp))
//                            SecondContent(
//                                province = province,
//                                cityOrDistrict = cityOrDistrict,
//                                subDistrict = subDistrict,
//                                postalCode = postalCode,
//                                streetName = streetName,
//                                additionalDetail = additionalDetail,
//                                onProvinceChange = onProvinceChange,
//                                onCityOrDistrictChange = onCityOrDistrictChange,
//                                onSubDistrictChange = onSubDistrictChange,
//                                onPostalCodeChange = onPostalCodeChange,
//                                onStreetNameChange = onStreetNameChange,
//                                onAdditionalDetailChange = onAdditionalDetailChange
//                            )
//                        }
//
//                        item {
////                            Spacer(modifier = Modifier.height(8.dp))
//                            Spacer(modifier = Modifier.height(20.dp))
////                            ThirdContent(
////                                label = "Location",
////                                onClick = { }
////                            )
//                        }
//                    }
//                }
//
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .background(Color.White)
//                        .height(90.dp),
//                    contentAlignment = Alignment.Center
//                ) {
//                    CustomButton(
//                        text = "Save",
//                        onClick = onSaveClick,
//                        isAvailable = isButtonAvailable
//                    )
//                }
//            }
//
//            if (showCircularProgress) {
//                Box(
//                    contentAlignment = Alignment.Center,
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .background(Color.Black.copy(alpha = 0.3f))
//                        .clickable(
//                            indication = null,
//                            interactionSource = remember { MutableInteractionSource() }
//                        ) {
//                            // Do nothing - this prevents clicks from passing through
//                        }
//                ) {
//                    CircularProgressIndicator(color = primaryLight)
//                }
//            }
//
//            if (showSuccessDialog) {
//                CustomPopUpDialog(
//                    onDismiss = onDismiss,
//                    isShowIcon = true,
//                    isShowTitle = true,
//                    isShowDescription = true,
//                    isShowButton = false,
//                    icon = {
//                        Box(
//                            modifier = Modifier
//                                .size(80.dp),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Icon(
//                                painter = painterResource(id = R.drawable.ceklist),
//                                contentDescription = null,
//                                tint = Color.Unspecified,
//                            )
//                        }
//                    },
//                    title = "Update Address Success",
//                    description = "Click the X button to close this dialog.",
//                )
//            }
//        }
//    }
//}
//
//
//@Composable
//private fun Header(
//    modifier: Modifier = Modifier,
//    storeData: StoreDetailData
//) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 20.dp),
//        horizontalArrangement = Arrangement.Center,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//
//        Column(
//            modifier = modifier.fillMaxHeight(),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally,
//        ) {
//
//            if (storeData.picture.isNullOrEmpty()) {
////            Log.e("WOIII KOSONG", "${storeData?.picture}")
//                Image(
//                    painter = painterResource(id = R.drawable.placeholder),  // Use a placeholder image
//                    contentDescription = "Store Image",
//                    contentScale = ContentScale.Crop,
//                    modifier = Modifier
//                        .size(70.dp)
//                        .clip(CircleShape)
//                        .background(Color(0xFFC8A2C8))
//                )
//            } else {
//                Log.e("WOIII ADA", "${storeData?.picture}")
//                AsyncImage(
////                    model = ImageRequest.Builder(LocalContext.current)
////                        .data(storeData?.picture)
////                        .crossfade(true)
////                        .build(),
//                    model = storeData?.picture,
//                    contentDescription = "Store Image",
//                    contentScale = ContentScale.Crop,
//                    placeholder = painterResource(id = R.drawable.placeholder),
//                    error = painterResource(id = R.drawable.placeholder),
//                    modifier = Modifier
//                        .fillMaxSize()
//                )
//            }
//            Spacer(modifier = Modifier.height(8.dp))
//            Text(
//                text = storeData.name ?: "No Name",
//                color = Color.Black,
//                fontSize = 20.sp,
//                fontWeight = FontWeight.Medium
//            )
//            Spacer(modifier = Modifier.height(8.dp))
//            CustomButton(
//                text = "Visit Store",
//                onClick = {
//
//                },
//                shape = RoundedCornerShape(15.dp),
//                defaultWidth = 120.dp,
//                defaultHeight = 40.dp,
//                fontSize = 14.sp,
//                fontWeight = FontWeight.Normal,
//            )
//        }
//    }
//}
//
//@Composable
//private fun HeaderContent(
//    modifier: Modifier = Modifier,
//    storeBanner: Uri,
//    storeLogo: Uri,
//    onLogoChange: (Uri) -> Unit,
//    onBannerChange: (Uri) -> Unit,
//) {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .background(Color.White)
//            .padding(top = 14.dp, bottom = 8.dp)
//    ) {
//        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
//            Text(
//                text = "Store Photos",
//                color = Color.Black,
//                fontSize = 14.sp,
//                fontWeight = FontWeight.W600,
//            )
//            Spacer(modifier = Modifier.height(4.dp))
//
////            ImagePickerCard(
////                label = "Store Banner",
////                imageUri = storeBanner,
////                onImagePicked = { uri ->
////                    onLogoChange(uri)
////                }
////            )
////            Spacer(modifier = Modifier.height(10.dp))
////            ImagePickerCard(
////                label = "Store Logo",
////                imageUri = storeLogo,
////                onImagePicked = { uri ->
////                    onBannerChange(uri)
////                }
////            )
//        }
//    }
//}
//
//// Store Header Photo
////== Section 1
//// Store Name
//// Store Description
//// Store Address
//// Store Phone Number
//
//@Composable
//private fun FirstContent(
//    modifier: Modifier = Modifier,
//    storeName: String,
//    storeDescription: String,
//    storePhone: String,
//    onStorePhoneChange: (String) -> Unit,
//    onStoreNameChange: (String) -> Unit,
//    onStoreDescriptionChange: (String) -> Unit,
//) {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .background(Color.White)
//            .padding(top = 14.dp, bottom = 8.dp)
//    ) {
//        EditItem(
//            label = "Stora Name",
//            value = storeName,
//            onChage = onStoreNameChange,
//            placeHolder = "e.g. Main Address"
//        )
//
//        EditItem(
//            label = "Store Description",
//            value = storeDescription,
//            onChage = onStoreDescriptionChange,
//            isLongText = true,
//            placeHolder = "Store A is a store that ...",
//            height = 100.dp,
//            borderColor = Color.Black,
//        )
//
//        EditItem(
//            label = "Store Phone Number",
//            keyboardType = KeyboardType.Password,
//            value = storePhone,
//            onChage = onStorePhoneChange,
//            placeHolder = "e.g. 08123456789",
//        )
//    }
//}
//
//@Composable
//private fun SecondContent(
//    modifier: Modifier = Modifier,
//    storeOperationDay: String,
//    storeOperationHour: String,
//    onOperationDayChange: (String) -> Unit,
//    onOperationHourChange: (String) -> Unit,
//) {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .background(Color.White)
//            .padding(top = 14.dp, bottom = 8.dp)
//    ) {
//        EditItem(
//            label = "Operation Day",
//            value = storeOperationDay,
//            onChage = onOperationDayChange,
//            placeHolder = "e.g. Monday - Friday"
//        )
//
//        EditItem(
//            label = "Operation Hour",
//            value = storeOperationHour,
//            onChage = onOperationHourChange,
//            placeHolder = "e.g. 08.00 - 20.00 WITA"
//        )
//
//    }
//}
//
//
//@Composable
//private fun EditItem(
//    modifier: Modifier = Modifier,
//    label: String,
//    value: String,
//    placeHolder: String = "",
//    isError: Boolean = false,
//    errorMessage: String = "",
//    onChage: (String) -> Unit,
//    isLongText: Boolean = false,
//    keyboardType: KeyboardType = KeyboardType.Text,
//    height: Dp = 50.dp,
//    borderColor: Color = Color.Gray
//) {
//    Column(
//        modifier = Modifier.fillMaxWidth()
//    ) {
//        Text(
//            text = label,
//            color = Color.Black,
//            fontSize = 14.sp,
//            fontWeight = FontWeight.W600,
//            modifier = Modifier.padding(horizontal = 20.dp)
//        )
//        Spacer(modifier = Modifier.height(4.dp))
//        CustomTextInput(
//            value = value,
//            onChange = onChage,
//            placeholder = placeHolder,
//            isError = isError,
//            errorMessage = errorMessage,
//            isLongText = isLongText,
//            keyboardType = keyboardType,
//            height = height,
//            borderColor = borderColor
//        )
//    }
//}


//
//@Composable
//private fun HeaderPhoto(
//    modifier: Modifier = Modifier,
//    storeImage: String?,
//    size: Dp = 250.dp,
//) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 20.dp),
//        horizontalArrangement = Arrangement.Center,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//
//
//
//        Column(
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally,
//        ) {
//            Box(
//                modifier = modifier
//                    .size(size),
//                contentAlignment = Alignment.BottomEnd
//            ) {
//
//                if (storeImage.isNullOrEmpty()) {
////            Log.e("WOIII KOSONG", "${storeData?.picture}")
//                    Image(
//                        painter = painterResource(id = R.drawable.placeholder),  // Use a placeholder image
//                        contentDescription = "Store Image",
//                        contentScale = ContentScale.Crop,
//                        modifier = Modifier
//                            .matchParentSize()
//                            .clip(CircleShape)
//                            .background(Color(0xFFC8A2C8))
//                    )
//                } else {
//                    Log.e("WOIII ADA", "${storeImage}")
//                    AsyncImage(
////                    model = ImageRequest.Builder(LocalContext.current)
////                        .data(storeData?.picture)
////                        .crossfade(true)
////                        .build(),
//                        model = storeImage,
//                        contentDescription = "Store Image",
//                        contentScale = ContentScale.Crop,
//                        placeholder = painterResource(id = R.drawable.placeholder),
//                        error = painterResource(id = R.drawable.placeholder),
//                        modifier = Modifier
//                            .matchParentSize()
//                            .clip(CircleShape)
//                    )
//                }
//
//                Box(
//                    modifier = modifier
//                        .size(40.dp)
//                        .offset(x = 2.dp, y = 2.dp)
//                        .clip(CircleShape)
//                        .background(primaryLight)
//                        .clickable(
//                            interactionSource = remember { MutableInteractionSource() },
//                            indication = null
//                        ) {
//
//                        },
//                    contentAlignment = Alignment.Center
//                ) {
//                    Icon(
//                        painter = painterResource(id = R.drawable.editprofile),
//                        contentDescription = "Ubah foto",
//                        tint = Color.White
//                    )
//                }
//            }
//        }
//    }
//}
