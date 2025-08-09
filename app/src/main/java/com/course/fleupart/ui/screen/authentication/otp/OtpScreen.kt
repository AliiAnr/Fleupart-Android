package com.course.fleupart.ui.screen.authentication.otp

import android.net.Uri
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.course.fleupart.R
import com.course.fleupart.data.model.remote.VerifyOtpResponse
import com.course.fleupart.data.resource.Resource
import com.course.fleupart.di.factory.OtpViewModelFactory
import com.course.fleupart.ui.common.OtpAction
import com.course.fleupart.ui.common.OtpState
import com.course.fleupart.ui.common.ResultResponse
import com.course.fleupart.ui.components.CustomTopAppBar
import com.course.fleupart.ui.components.OtpInputField
import com.course.fleupart.ui.screen.navigation.FleupartSurface
import com.course.fleupart.ui.screen.navigation.MainDestinations
import com.course.fleupart.ui.theme.primaryLight
import kotlinx.coroutines.delay


@Composable
fun OtpScreen(
    modifier: Modifier = Modifier,
    email: String,
    navigateToRoute: (String, Boolean) -> Unit,
    onBackClick: () -> Unit
) {

    var countdown by remember { mutableIntStateOf(0) }

    val otpViewModel: OtpScreenViewModel = viewModel(
        factory = OtpViewModelFactory.getInstance(
            Resource.appContext
        )
    )

    val otpState by otpViewModel.verifyOtpState.collectAsStateWithLifecycle(initialValue = ResultResponse.None)
    Log.e("OtPSTET", "otpState: $otpState")


    val state by otpViewModel.state.collectAsStateWithLifecycle()
    Log.e("OtpScreen", "email: $email")
    Log.e("OTPVALID?", "${state.isValid}")

    val decodedEmail: String = Uri.decode(email)
    otpViewModel.setEmail(decodedEmail)
    Log.e("OtpScreen", "email: ${otpViewModel.emailValue}")

    val focusRequesters = remember {
        List(5) { FocusRequester() }
    }

    val focusManager = LocalFocusManager.current
    val keyboardManager = LocalSoftwareKeyboardController.current

    LaunchedEffect(state.focusedIndex) {
        state.focusedIndex?.let { index ->
            focusRequesters.getOrNull(index)?.requestFocus()
        }
    }

    LaunchedEffect(state.code, keyboardManager) {
        val allNumbersEntered = state.code.none { it == null }
        if (allNumbersEntered) {
            focusRequesters.forEach {
                it.freeFocus()
            }
            focusManager.clearFocus()
            keyboardManager?.hide()
        }
    }

    LaunchedEffect(state.isValid, otpState) {
        if (state.isValid == false || otpState is ResultResponse.Error) {
            otpViewModel.onAction(OtpAction.OnError)
            focusManager.clearFocus()
        }
    }

    LaunchedEffect(countdown) {
        if (countdown > 0) {
            delay(1000L)
            countdown -= 1
        }
    }

    OtpScreen(
        state = state,
        otpState = otpState,
        otpViewModel = otpViewModel,
        navigateToRoute = navigateToRoute,
        onBackClick = onBackClick,
        focusRequesters = focusRequesters,
        onAction = { action ->
            when (action) {
                is OtpAction.OnEnterNumber -> {
                    if (action.number != null) {
                        focusRequesters[action.index].freeFocus()
                    }
                }

                else -> Unit
            }
            otpViewModel.onAction(action)
        },
        onResendClick = {
            countdown = 60
            // Call your resend OTP function here
        },
        countdown = countdown
    )
}


@Composable
private fun OtpScreen(
    modifier: Modifier = Modifier,
    state: OtpState,
    otpState: ResultResponse<VerifyOtpResponse>,
    otpViewModel: OtpScreenViewModel,
    focusRequesters: List<FocusRequester>,
    onAction: (OtpAction) -> Unit,
    navigateToRoute: (String, Boolean) -> Unit,
    onBackClick: () -> Unit,
    onResendClick: () -> Unit,
    countdown: Int
) {
    val focusManager = LocalFocusManager.current
    var showInvalidOtpMessage by remember { mutableStateOf(false) }
    var showCircularProgress by remember { mutableStateOf(false) }

    LaunchedEffect(state.isValid) {
        if (state.isValid == false) {
            showInvalidOtpMessage = true
            delay(5000)
            showInvalidOtpMessage = false
        }
    }

    LaunchedEffect(otpState) {
        when (otpState) {
            is ResultResponse.Success -> {
                showCircularProgress = false
                Log.e(
                    "OtpScreen",
                    "Otp Sukses: ${(otpState as ResultResponse.Success).data}"
                )
                navigateToRoute(MainDestinations.LOGIN_ROUTE, true)
                otpViewModel.resetState()
            }

            is ResultResponse.Loading -> {
                showCircularProgress = true
            }

            is ResultResponse.Error -> {
                showCircularProgress = false
                Log.e(
                    "RegisterScreen",
                    "Registration error: ${(otpState as ResultResponse.Error).error}"
                )
                // Display error message to the user
                // Toast.makeText(context, otpState.message, Toast.LENGTH_SHORT).show()
            }

            else -> {}
        }
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
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CustomTopAppBar(
                    title = "",
                    showNavigationIcon = false
                )
                Spacer(modifier = Modifier.height(80.dp))
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val title = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color.Black,
                                fontWeight = FontWeight.Normal
                            )
                        ) {
                            append("Didn't receive an email? ")
                        }
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Normal
                            )
                        ) {
                            if (countdown > 0) {
                                append("$countdown")
                            } else {
                                append("Resend")
                            }
                        }
                    }

                    Image(
                        painter = painterResource(id = R.drawable.amico),
                        contentDescription = null,
                        modifier = Modifier
                            .size(250.dp)
                            .padding(bottom = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "OTP Verification",
                        textAlign = TextAlign.Start,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Enter the verification code was just send to your email address",
                        textAlign = TextAlign.Start,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W600,
                        color = Color.Black,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        state.code.forEachIndexed { index, number ->
                            OtpInputField(
                                number = number,
                                focusRequester = focusRequesters[index],
                                onFocusChanged = { isFocused ->
                                    if (isFocused) {
                                        onAction(OtpAction.OnChangeFieldFocused(index))
                                    }
                                },
                                onNumberChanged = { newNumber ->
                                    onAction(OtpAction.OnEnterNumber(newNumber, index))
                                },
                                onKeyboardBack = {
                                    onAction(OtpAction.OnKeyboardBack)
                                },
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    if (!showInvalidOtpMessage) {
                        Spacer(modifier = Modifier.height(24.dp))
                    } else {
                        AnimatedVisibility(
                            visible = showInvalidOtpMessage,
                            enter = fadeIn() + scaleIn(initialScale = 0.8f),
                            exit = fadeOut(animationSpec = tween(durationMillis = 500)) + scaleOut(
                                targetScale = 0.8f
                            )
                        ) {
                            Text(
                                text = "OTP is invalid!",
                                color = Color.Red,
                                fontSize = 16.sp
                            )
                        }
                    }

//                    Text(
//                        text = title,
//                        textAlign = TextAlign.Center,
//                        fontSize = 14.sp,
//                        fontWeight = FontWeight.W600,
//                        color = Color.Black,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .clickable { if (countdown == 0) onResendClick() }
//                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                    ) {
                        Text(
                            text = "Didn't receive an email? ",
                            textAlign = TextAlign.Center,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W600,
                            color = Color.Black,
                        )

                        Text(
                            text = if (countdown > 0) "$countdown" else "Resend",
                            textAlign = TextAlign.Center,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W600,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.
                            clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                if (countdown == 0) {
                                    onResendClick()
                                }
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
        }
    }
}