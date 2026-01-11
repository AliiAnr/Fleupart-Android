package com.course.fleupart.ui.screen.dashboard.detail.Profile

import android.net.Uri
import android.util.Log
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.course.fleupart.R
import com.course.fleupart.data.model.remote.StoreAddress
import com.course.fleupart.ui.common.ResultResponse
import com.course.fleupart.ui.components.CustomButton
import com.course.fleupart.ui.components.CustomPopUpDialog
import com.course.fleupart.ui.components.CustomTextField
import com.course.fleupart.ui.components.CustomTextInput
import com.course.fleupart.ui.components.CustomTopAppBar
import com.course.fleupart.ui.screen.dashboard.profile.ProfileViewModel
import com.course.fleupart.ui.screen.navigation.FleupartSurface
import com.course.fleupart.ui.theme.base20
import com.course.fleupart.ui.theme.base60
import com.course.fleupart.ui.theme.primaryLight

@Composable
fun StoreAddressDetail(
    modifier
    : Modifier = Modifier,
    onBackClick: () -> Unit,
    profileViewModel: ProfileViewModel,
) {

    val updateAddressState by profileViewModel.updateStoreAddressState.collectAsStateWithLifecycle(
        initialValue = ResultResponse.None
    )

    var showCircularProgress by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    LaunchedEffect(updateAddressState) {
        when (updateAddressState) {
            is ResultResponse.Success -> {
                showCircularProgress = false
                showSuccessDialog = true

                Log.e("AddProfileScreen", "User detail: $updateAddressState")

            }

            is ResultResponse.Loading -> {
                showCircularProgress = true
            }

            is ResultResponse.Error -> {
                showCircularProgress = false
                Log.e(
                    "AddAddressScreen",
                    "addAddress error: ${(updateAddressState as ResultResponse.Error).error}"
                )
            }

            else -> {}
        }
    }

    val initialData = remember {
        profileViewModel.storeAddressValue.value?.let { data ->
            StoreAddress(
                name = data.name ?: "",
                phone = data.phone ?: "",
                province = data.province ?: "",
                city = data.city ?: "",
                district = data.district ?: "",
                road = data.road ?: "",
                detail = data.detail ?: "",
                postcode = data.postcode ?: ""
            )
        }
    }

    var name by remember { mutableStateOf(profileViewModel.storeAddressValue.value?.name ?: "") }
    var phone by remember { mutableStateOf(profileViewModel.storeAddressValue.value?.phone ?: "") }

    var province by remember {
        mutableStateOf(
            profileViewModel.storeAddressValue.value?.province ?: ""
        )
    }
    var cityOrDistrict by remember {
        mutableStateOf(
            profileViewModel.storeAddressValue.value?.city ?: ""
        )
    }
    var subDistrict by remember {
        mutableStateOf(
            profileViewModel.storeAddressValue.value?.district ?: ""
        )
    }
    var postalCode by remember {
        mutableStateOf(
            profileViewModel.storeAddressValue.value?.postcode ?: ""
        )
    }
    var streetName by remember {
        mutableStateOf(
            profileViewModel.storeAddressValue.value?.road ?: ""
        )
    }
    var additionalDetail by remember {
        mutableStateOf(
            profileViewModel.storeAddressValue.value?.detail ?: ""
        )
    }

    val isChanged = remember(
        name,
        phone,
        province,
        cityOrDistrict,
        subDistrict,
        streetName,
        additionalDetail,
        postalCode
    ) {
        name != (initialData?.name ?: "") ||
                phone != (initialData?.phone ?: "") ||
                province != (initialData?.province ?: "") ||
                cityOrDistrict != (initialData?.city ?: "") ||
                subDistrict != (initialData?.district ?: "") ||
                streetName != (initialData?.road ?: "") ||
                additionalDetail != (initialData?.detail ?: "") ||
                postalCode != (initialData?.postcode ?: "")
    }

    val isAllFieldsFilled = name.isNotBlank() &&
            phone.isNotBlank() &&
            province.isNotBlank() &&
            cityOrDistrict.isNotBlank() &&
            subDistrict.isNotBlank() &&
            postalCode.isNotBlank() &&
            streetName.isNotBlank() &&
            additionalDetail.isNotBlank()


    val isButtonAvailable = !showCircularProgress && isChanged && isAllFieldsFilled

    StoreAddressDetail(
        modifier = modifier,
        showSuccessDialog = showSuccessDialog,
        showCircularProgress = showCircularProgress,
        isButtonAvailable = isButtonAvailable,
        name = name,
        phone = phone,
        province = province,
        cityOrDistrict = cityOrDistrict,
        subDistrict = subDistrict,
        postalCode = postalCode,
        streetName = streetName,
        additionalDetail = additionalDetail,
        onBackClick = onBackClick,
        onNameChange = { name = it },
        onPhoneChange = { phone = it },
        onProvinceChange = { province = it },
        onCityOrDistrictChange = { cityOrDistrict = it },
        onSubDistrictChange = { subDistrict = it },
        onPostalCodeChange = { postalCode = it },
        onStreetNameChange = { streetName = it },
        onAdditionalDetailChange = { additionalDetail = it },
        onSaveClick = {
            profileViewModel.updateStoreAddress(
                name = name,
                phone = phone,
                province = province,
                road = streetName,
                city = cityOrDistrict,
                district = subDistrict,
                postcode = postalCode,
                detail = additionalDetail,
                latitude = 0.0,
                longitude = 0.0
            )
        },
        onDismiss = {
            showSuccessDialog = false
            onBackClick()
            profileViewModel.resetUpdateState()
        }
    )
}

@Composable
private fun StoreAddressDetail(
    modifier: Modifier = Modifier,
    showSuccessDialog: Boolean,
    showCircularProgress: Boolean,
    isButtonAvailable: Boolean,
    name: String,
    phone: String,
    province: String,
    cityOrDistrict: String,
    subDistrict: String,
    postalCode: String,
    streetName: String,
    additionalDetail: String,
    onBackClick: () -> Unit,
    onNameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onProvinceChange: (String) -> Unit,
    onCityOrDistrictChange: (String) -> Unit,
    onSubDistrictChange: (String) -> Unit,
    onPostalCodeChange: (String) -> Unit,
    onStreetNameChange: (String) -> Unit,
    onAdditionalDetailChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    onDismiss: () -> Unit
) {

    FleupartSurface(
        modifier = modifier.fillMaxSize(),
    ) {
        val focusManager = LocalFocusManager.current
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    // Hapus fokus jika area lain diklik
                    indication = null, // Tidak ada animasi klik
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
                    title = "Address Detail",
                    showNavigationIcon = true,
                    onBackClick = onBackClick
                )

                // Konten utama menggunakan LazyColumn
                Box(
                    modifier = Modifier.weight(1f) // Membuat LazyColumn fleksibel
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            FirstContent(
                                name = name,
                                phone = phone,
                                onNameChange = onNameChange,
                                onPhoneChange = onPhoneChange
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            SecondContent(
                                province = province,
                                cityOrDistrict = cityOrDistrict,
                                subDistrict = subDistrict,
                                postalCode = postalCode,
                                streetName = streetName,
                                additionalDetail = additionalDetail,
                                onProvinceChange = onProvinceChange,
                                onCityOrDistrictChange = onCityOrDistrictChange,
                                onSubDistrictChange = onSubDistrictChange,
                                onPostalCodeChange = onPostalCodeChange,
                                onStreetNameChange = onStreetNameChange,
                                onAdditionalDetailChange = onAdditionalDetailChange
                            )
                        }

                        item {
//                            Spacer(modifier = Modifier.height(8.dp))
                            Spacer(modifier = Modifier.height(20.dp))
//                            ThirdContent(
//                                label = "Location",
//                                onClick = { }
//                            )
                        }
                    }
                }

                // Tombol berada di bagian bawah
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .navigationBarsPadding()
                        .height(90.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CustomButton(
                        text = "Save",
                        onClick = onSaveClick,
                        isAvailable = isButtonAvailable
                    )
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
                    onDismiss = onDismiss,
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
                    title = "Update Address Success",
                    description = "Click the X button to close this dialog.",
                )
            }
        }
    }
}

@Composable
private fun FirstContent(
    modifier: Modifier = Modifier,
    name: String,
    phone: String,
    onNameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(top = 14.dp, bottom = 8.dp)
    ) {
        EditItem(
            label = "Name",
            value = name,
            onChage = onNameChange,
            placeHolder = "e.g. Main Address"
        )
        EditItem(
            label = "Phone Number",
            value = phone,
            onChage = onPhoneChange,
            placeHolder = "e.g. 081234567890"
        )
    }
}

@Composable
private fun SecondContent(
    modifier: Modifier = Modifier,
    province: String,
    cityOrDistrict: String,
    subDistrict: String,
    postalCode: String,
    streetName: String,
    additionalDetail: String,
    onProvinceChange: (String) -> Unit,
    onCityOrDistrictChange: (String) -> Unit,
    onSubDistrictChange: (String) -> Unit,
    onPostalCodeChange: (String) -> Unit,
    onStreetNameChange: (String) -> Unit,
    onAdditionalDetailChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(top = 14.dp, bottom = 8.dp)
    ) {
        EditItem(
            label = "Province",
            value = province,
            onChage = onProvinceChange,
            placeHolder = "e.g. DKI Jakarta"
        )
//        EditItem(
//            label = "Province, City/District, Sub-district, Postal Code",
//            value = value,
//            onChage = onChage
//        )

        //add update/edit profile on PROFILE SCREEN !!

        EditItem(
            label = "City/District",
            value = cityOrDistrict,
            onChage = onCityOrDistrictChange
        )
        EditItem(
            label = "Sub-district",
            value = subDistrict,
            onChage = onSubDistrictChange
        )
        EditItem(
            label = "Postal Code",
            value = postalCode,
            onChage = onPostalCodeChange
        )
        EditItem(
            label = "Street Name, Building, House No.",
            value = streetName,
            onChage = onStreetNameChange
        )
        EditItem(
            label = "Additional Detail",
            value = additionalDetail,
            onChage = onAdditionalDetailChange
        )
    }
}

@Composable
private fun ThirdContent(
    modifier: Modifier = Modifier,
    label: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(top = 14.dp, bottom = 20.dp)
    ) {
        Text(
            text = label,
            color = Color.Black,
            fontSize = 14.sp,
            fontWeight = FontWeight.W600,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier
                .height(80.dp)
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .border(
                    width = 1.dp,
                    color = base60,
                    shape = RoundedCornerShape(10.dp)
                )
                .clickable { onClick() },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.address_map),
                contentDescription = "Rating",
                tint = Color.Unspecified,
                modifier = Modifier.size(35.dp)
            )
            Text(
                text = "Tekan untuk memilih lokasi spesifik",
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.W600,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }
    }
}


@Composable
private fun EditItem(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    placeHolder: String = "",
    isError: Boolean = false,
    errorMessage: String = "",
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
        )
    }
}
