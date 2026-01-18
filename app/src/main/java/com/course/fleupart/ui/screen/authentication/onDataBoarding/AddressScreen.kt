package com.course.fleupart.ui.screen.authentication.onDataBoarding


import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.course.fleupart.ui.common.ResultResponse
import com.course.fleupart.ui.components.CustomButton
import com.course.fleupart.ui.components.CustomTextField
import com.course.fleupart.ui.components.CustomTopAppBar
import com.course.fleupart.ui.screen.authentication.login.LoginScreenViewModel
import com.course.fleupart.ui.screen.navigation.FleupartSurface
import com.course.fleupart.ui.screen.navigation.MainDestinations
import com.course.fleupart.ui.theme.primaryLight

@Composable
fun AddressScreen(
    modifier: Modifier = Modifier,
    navigateToRoute: (String, Boolean) -> Unit,
    onDataBoardingViewModel: OnDataBoardingViewModel,
    loginViewModel: LoginScreenViewModel
) {

    var showCircularProgress by remember { mutableStateOf(false) }

    val personalizeState by onDataBoardingViewModel.personalizeState.collectAsStateWithLifecycle(
        initialValue = ResultResponse.None
    )

    val storeCheckState by loginViewModel.storeCheckState.collectAsStateWithLifecycle(initialValue = ResultResponse.None)
    val updateStoreState by loginViewModel.updateStoreState.collectAsStateWithLifecycle(initialValue = ResultResponse.None)

    val userState by loginViewModel.userState.collectAsStateWithLifecycle(
        initialValue = ResultResponse.None
    )

    LaunchedEffect(personalizeState) {
        when (personalizeState) {
            is ResultResponse.Success -> {
                showCircularProgress = true
                loginViewModel.getUser()
            }

            is ResultResponse.Loading -> {
                showCircularProgress = true
            }

            is ResultResponse.Error -> {
                showCircularProgress = false
                onDataBoardingViewModel.setPersonalizeState(ResultResponse.None)
            }

            else -> {}
        }
    }

    LaunchedEffect(userState) {
        when (userState) {
            is ResultResponse.Success -> {
                showCircularProgress = false
                val detail = (userState as ResultResponse.Success).data.data
                Log.e("AddressScreen", "User detail: $detail")
                if (detail.isProfileComplete()) {
//                    loginViewModel.setPersonalizeCompleted()
//                    navigateToRoute(MainDestinations.DASHBOARD_ROUTE, true)
//                    onDataBoardingViewModel.resetDataBoardingValue()
                    loginViewModel.checkStore()
                } else {
                    navigateToRoute(MainDestinations.CITIZEN_ROUTE, true)
                    onDataBoardingViewModel.resetDataBoardingValue()
                }
//                onDataBoardingViewModel.setPersonalizeState(ResultResponse.None)
            }

            is ResultResponse.Loading -> {
                showCircularProgress = true
            }

            is ResultResponse.Error -> {
                showCircularProgress = false
                onDataBoardingViewModel.setPersonalizeState(ResultResponse.None)
            }

            else -> {}
        }
    }

    LaunchedEffect(storeCheckState) {
        when (storeCheckState) {
            is ResultResponse.Success -> {
                val isStoreExist = (storeCheckState as ResultResponse.Success).data

                if (isStoreExist) {
                    // CASE A: Toko sudah ada, Profil sudah lengkap -> Dashboard
                    loginViewModel.setPersonalizeCompleted()
                    navigateToRoute(MainDestinations.DASHBOARD_ROUTE, true)
                    onDataBoardingViewModel.resetDataBoardingValue()
                    loginViewModel.resetState()
                } else {
                    // CASE B: Toko belum ada -> Auto-Register Store
                    val currentUser = loginViewModel.userState.value
                    val storeName = if (currentUser is ResultResponse.Success) {
                        currentUser.data.data.name ?: "My Store"
                    } else {
                        "My Store"
                    }
                    loginViewModel.updateStore(storeName)
                }
            }
            is ResultResponse.Loading -> showCircularProgress = true
            is ResultResponse.Error -> {
                showCircularProgress = false
                Log.e("AddressScreen", "Check store failed: ${(storeCheckState as ResultResponse.Error).error}")
            }
            else -> {}
        }
    }

    // 3. UPDATE STORE SUCCESS -> DASHBOARD
    LaunchedEffect(updateStoreState) {
        when (updateStoreState) {
            is ResultResponse.Success -> {
                loginViewModel.setPersonalizeCompleted()
                navigateToRoute(MainDestinations.DASHBOARD_ROUTE, true)
                onDataBoardingViewModel.resetDataBoardingValue()
                loginViewModel.resetState()
            }
            is ResultResponse.Loading -> showCircularProgress = true
            is ResultResponse.Error -> {
                showCircularProgress = false
                Log.e("AddressScreen", "Update store failed: ${(updateStoreState as ResultResponse.Error).error}")
            }
            else -> {}
        }
    }

    AddressScreen(
        modifier = modifier,
        showCircularProgress = showCircularProgress,
        setCircularProgress = { value ->
            showCircularProgress = value
        },
        onDataBoardingViewModel = onDataBoardingViewModel,
    )

}

@Composable
private fun AddressScreen(
    modifier: Modifier = Modifier,
    showCircularProgress: Boolean,
    setCircularProgress: (Boolean) -> Unit,
    onDataBoardingViewModel: OnDataBoardingViewModel,
) {

    val focusManager = LocalFocusManager.current

    val title = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color(0xFFFFD700), fontWeight = FontWeight.ExtraBold)) {
            append("Yeay ")
        }

        withStyle(style = SpanStyle(color = primaryLight, fontWeight = FontWeight.ExtraBold)) {
            append("you are in the final step to sign in!")
        }

    }

    FleupartSurface(
        modifier = modifier
            .fillMaxSize(),
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
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CustomTopAppBar(
                    title = "",
                    showNavigationIcon = false
                )
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    item {
                        Text(
                            text = title,
                            textAlign = TextAlign.Start,
                            fontSize = 36.sp,
                            color = primaryLight,
                            fontWeight = FontWeight.ExtraBold,
                            lineHeight = 50.sp,
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .align(Alignment.Start)
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    item {
                        CustomTextField(
                            value = onDataBoardingViewModel.streetNameValue,
                            onChange = onDataBoardingViewModel::setStreetName,
                            placeholder = "Street Name",
                            isError = false,
                            errorMessage = "",
                            modifier = Modifier.padding(horizontal = 20.dp)
                        )
                    }

                    item {
                        CustomTextField(
                            value = onDataBoardingViewModel.subDistrictValue,
                            onChange = onDataBoardingViewModel::setSubDistrict,
                            placeholder = "Sub-District",
                            isError = false,
                            errorMessage = "",
                            modifier = Modifier.padding(horizontal = 20.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                    }

                    item {
                        CustomTextField(
                            value = onDataBoardingViewModel.cityValue,
                            onChange = onDataBoardingViewModel::setCity,
                            placeholder = "City/District",
                            isError = false,
                            errorMessage = "",
                            modifier = Modifier.padding(horizontal = 20.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                    }

                    item {
                        CustomTextField(
                            value = onDataBoardingViewModel.provinceValue,
                            onChange = onDataBoardingViewModel::setProvince,
                            placeholder = "Province",
                            isError = false,
                            errorMessage = "",
                            modifier = Modifier.padding(horizontal = 20.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                    }

                    item {
                        CustomTextField(
                            value = onDataBoardingViewModel.postalCodeValue,
                            onChange = onDataBoardingViewModel::setPostalCode,
                            placeholder = "Postal Code",
                            isError = false,
                            errorMessage = "",
                            modifier = Modifier.padding(horizontal = 20.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                    }

//                    item {
//                        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
//                            ImagePickerCard(
//                                label = "Shop Photo",
//                                imageUri = citizenCardUri,
//                                onImagePicked = { uri ->
//                                    citizenCardUri = uri
//                                }
//                            )
//                        }
//                        Spacer(modifier = Modifier.height(20.dp))
//                    }

                    item {
                        CustomTextField(
                            value = onDataBoardingViewModel.additionalDetailValue,
                            onChange = onDataBoardingViewModel::setAdditionalDetail,
                            placeholder = "Additional Detail",
                            isError = false,
                            errorMessage = "",
                            modifier = Modifier.padding(horizontal = 20.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                    }

                    item {
                        CustomButton(
                            text = "Register",
                            isOutlined = true,
                            outlinedColor = Color.Black,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            isAvailable = onDataBoardingViewModel.streetNameValue.isNotEmpty() &&
                                    onDataBoardingViewModel.subDistrictValue.isNotEmpty() &&
                                    onDataBoardingViewModel.cityValue.isNotEmpty() &&
                                    onDataBoardingViewModel.provinceValue.isNotEmpty() &&
                                    onDataBoardingViewModel.postalCodeValue.isNotEmpty() &&
                                    onDataBoardingViewModel.additionalDetailValue.isNotEmpty() &&
                                    !showCircularProgress,
                            onClick = {
                                setCircularProgress(true)
                                focusManager.clearFocus()
                                onDataBoardingViewModel.inputAddressData()
                            },
                            modifier = Modifier.padding(top = 30.dp)
                        )
                        Spacer(modifier = Modifier.height(100.dp))
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
        }
    }
}