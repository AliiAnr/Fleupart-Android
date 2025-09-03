package com.course.fleupart.ui.screen.authentication.onDataBoarding


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
import androidx.compose.ui.text.input.KeyboardType
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
fun CitizenScreen(
    modifier: Modifier = Modifier,
    onDataBoardingViewModel: OnDataBoardingViewModel,
    navigateToRoute: (String, Boolean) -> Unit,
) {

    var showCircularProgress by remember { mutableStateOf(false) }

    val personalizeState by onDataBoardingViewModel.personalizeState.collectAsStateWithLifecycle(
        initialValue = ResultResponse.None
    )

    LaunchedEffect(personalizeState) {
        when (personalizeState) {
            is ResultResponse.Success -> {
                showCircularProgress = false
                navigateToRoute(MainDestinations.DASHBOARD_ROUTE, true)
            }
            is ResultResponse.Loading -> {
                showCircularProgress = true
            }
            is ResultResponse.Error -> {
                showCircularProgress = false
                // Show error message
            }
            else -> {}
        }
    }


    CitizenScreen(
        modifier = modifier,
        showCircularProgress = showCircularProgress,
        setCircularProgress = { value ->
            showCircularProgress = value
        },
        onDataBoardingViewModel = onDataBoardingViewModel,
    )

}

@Composable
private fun CitizenScreen(
    modifier: Modifier = Modifier,
    showCircularProgress: Boolean,
    setCircularProgress: (Boolean) -> Unit,
    onDataBoardingViewModel: OnDataBoardingViewModel,
) {

    val focusManager = LocalFocusManager.current
    val title = buildAnnotatedString {
        withStyle(style = SpanStyle(color = primaryLight, fontWeight = FontWeight.ExtraBold)) {
            append("Input your valid ")
        }

        withStyle(style = SpanStyle(color = Color(0xFFFFD700), fontWeight = FontWeight.ExtraBold)) {
            append("data! ")
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
                    onClick = {
                        focusManager.clearFocus()
                    },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ),
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
                CustomTextField(
                    value = onDataBoardingViewModel.nameValue,
                    onChange = onDataBoardingViewModel::setName,
                    placeholder = "Full Name",
                    isError = onDataBoardingViewModel.nameErrorValue.isNotEmpty(),
                    errorMessage = onDataBoardingViewModel.nameErrorValue,
                    keyboardType = KeyboardType.Text,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(modifier = Modifier.height(6.dp))

                CustomTextField(
                    value = onDataBoardingViewModel.citizenIdValue,
                    onChange = onDataBoardingViewModel::setCitizenId,
                    placeholder = "Citizen Id",
                    isError = onDataBoardingViewModel.citizenIdErrorValue.isNotEmpty(),
                    errorMessage = onDataBoardingViewModel.citizenIdErrorValue,
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(modifier = Modifier.height(6.dp))
                CustomTextField(
                    value = onDataBoardingViewModel.phoneNumberValue,
                    onChange = onDataBoardingViewModel::setPhoneNumber,
                    placeholder = "Phone Number",
                    isError = onDataBoardingViewModel.phoneNumberErrorValue.isNotEmpty(),
                    errorMessage = onDataBoardingViewModel.phoneNumberErrorValue,
                    keyboardType = KeyboardType.Phone,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(modifier = Modifier.height(6.dp))
                CustomTextField(
                    value = onDataBoardingViewModel.bankAccountNumberValue,
                    onChange = onDataBoardingViewModel::setBankAccountNumber,
                    placeholder = "Bank Account Number",
                    isError = onDataBoardingViewModel.bankAccountNumberErrorValue.isNotEmpty(),
                    errorMessage = onDataBoardingViewModel.bankAccountNumberErrorValue,
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                CustomButton(
                    text = "Next",
                    isOutlined = true,
                    outlinedColor = Color.Black,
                    isAvailable = onDataBoardingViewModel.nameValue.isNotEmpty() &&
                            onDataBoardingViewModel.nameErrorValue.isEmpty() &&
                            onDataBoardingViewModel.citizenIdValue.isNotEmpty() &&
                            onDataBoardingViewModel.citizenIdErrorValue.isEmpty() &&
                            onDataBoardingViewModel.phoneNumberValue.isNotEmpty() &&
                            onDataBoardingViewModel.phoneNumberErrorValue.isEmpty() &&
                            onDataBoardingViewModel.bankAccountNumberValue.isNotEmpty() &&
                            onDataBoardingViewModel.bankAccountNumberErrorValue.isEmpty() &&
                            !showCircularProgress,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    onClick = {
                        setCircularProgress(true)
                        focusManager.clearFocus()
                        onDataBoardingViewModel.inputCitizenData()
                    },
                    modifier = Modifier.padding(top = 30.dp)
                )
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