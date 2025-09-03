package com.course.fleupart.ui.screen.authentication.register

import android.net.Uri
import android.util.Log
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.course.fleupart.R
import com.course.fleupart.data.model.remote.RegisterResponse
import com.course.fleupart.ui.common.ResultResponse
import com.course.fleupart.ui.components.CustomButton
import com.course.fleupart.ui.components.CustomTextField
import com.course.fleupart.ui.components.CustomTopAppBar
import com.course.fleupart.ui.screen.navigation.FleupartSurface
import com.course.fleupart.ui.screen.navigation.MainDestinations
import com.course.fleupart.ui.theme.primaryLight

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    navigateToRoute: (String, Boolean) -> Unit,
    registerViewModel: RegisterScreenViewModel,
    onBackClick: () -> Unit
) {

    val registerState by registerViewModel.registerState.collectAsStateWithLifecycle(initialValue = ResultResponse.None)

    RegisterScreen(
        modifier = modifier,
        navigateToRoute = navigateToRoute,
        registerState = registerState,
        onBackClick = onBackClick,
        registerViewModel = registerViewModel,
        id = 0 // You can pass an ID or any other parameter if needed
    )
}


@Composable
private fun RegisterScreen(
    modifier: Modifier = Modifier,
    id: Int,
    navigateToRoute: (String, Boolean) -> Unit,
    registerViewModel: RegisterScreenViewModel,
    registerState: ResultResponse<RegisterResponse>,
    onBackClick: () -> Unit
) {

    val emailValue = Uri.encode(registerViewModel.emailValue)

    var showCircularProgress by remember { mutableStateOf(false) }

    val otpValue by registerViewModel.otpState.collectAsStateWithLifecycle(initialValue = ResultResponse.None)
    val focusManager = LocalFocusManager.current
    val title = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color(0xFFFFD700), fontWeight = FontWeight.ExtraBold)) {
            append("Hello! ")
        }
        withStyle(style = SpanStyle(color = primaryLight, fontWeight = FontWeight.ExtraBold)) {
            append("Register to get started")
        }

    }

    LaunchedEffect(registerState) {
        when (registerState) {
            is ResultResponse.Success -> {
                showCircularProgress = false
                Log.d(
                    "RegisterScreen",
                    "Registration successful: ${(registerState as ResultResponse.Success).data}"
                )
                Log.e("tempLog" , "${registerViewModel.tempLog}")
                navigateToRoute("${MainDestinations.OTP_ROUTE}?" + "email=$emailValue", true)
                registerViewModel.resetState()
            }

            is ResultResponse.Loading -> {
                showCircularProgress = true
            }

            is ResultResponse.Error -> {
                showCircularProgress = false
                Log.e("RegisterScreen", "Registration error: ${(registerState as ResultResponse.Error).error}")
                // Display error message to the user
                // Toast.makeText(context, registerState.message, Toast.LENGTH_SHORT).show()
            }

            else -> {}
        }
    }

    FleupartSurface(modifier = modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        onClick = {
                            focusManager.clearFocus()
                        },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    )
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CustomTopAppBar(
                    title = "",
                    showNavigationIcon = true,
                    horizontalPadding = 0.dp,
                    onBackClick = {
                        registerViewModel.resetState()
                        onBackClick()
                    }
                )
                Text(
                    text = title,
                    textAlign = TextAlign.Start,
                    fontSize = 36.sp,
                    color = primaryLight,
                    fontWeight = FontWeight.ExtraBold,
                    lineHeight = 50.sp,
                    modifier = Modifier
                        .padding(top = 30.dp, bottom = 30.dp)
                        .align(Alignment.Start)
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    CustomTextField(
                        value = registerViewModel.emailValue,
                        onChange = registerViewModel::setEmail,
                        placeholder = "Email",
                        isError = registerViewModel.emailError.isNotEmpty(),
                        icon = Icons.Rounded.Email,
                        errorMessage = registerViewModel.emailError,
                        keyboardType = KeyboardType.Email,
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    CustomTextField(
                        value = registerViewModel.passwordValue,
                        onChange = registerViewModel::setPassword,
                        placeholder = "Password",
                        isError = registerViewModel.passwordError.isNotEmpty(),
                        icon = Icons.Rounded.Lock,
                        isPassword = true,
                        errorMessage = registerViewModel.passwordError,
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    CustomTextField(
                        value = registerViewModel.confirmPasswordValue,
                        onChange = registerViewModel::setConfirmPassword,
                        placeholder = "Password",
                        isError = registerViewModel.confirmPasswordError.isNotEmpty(),
                        icon = Icons.Rounded.Lock,
                        isPassword = true,
                        errorMessage = registerViewModel.confirmPasswordError,
                    )
                }


                CustomButton(
                    text = "Register",
                    isOutlined = true,
                    isAvailable = registerViewModel.emailError.isEmpty() && registerViewModel.emailValue.isNotEmpty() && registerViewModel.passwordError.isEmpty() && registerViewModel.passwordValue.isNotEmpty() && registerViewModel.confirmPasswordError.isEmpty() && registerViewModel.confirmPasswordValue.isNotEmpty() && !showCircularProgress,
                    outlinedColor = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    onClick = {
                        focusManager.clearFocus()
                        Log.e("RegisterScreen", "Register button clicked")
                        Log.e("RegisterScreen", "$registerState")
                        Log.e("RegisterScreen", "$otpValue")
                        registerViewModel.registerSeller()
                    },
                    modifier = Modifier.padding(top = 30.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp)
                ) {
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        thickness = 1.5.dp,
                        color = Color.Gray
                    )
                    Text(
                        text = " Or Register with ",
                        color = Color(0xFF1C1C1C),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        thickness = 1.5.dp,
                        color = Color.Gray
                    )
                }

                CustomButton(
                    text = "",
                    isOutlined = true,
                    borderWidth = 1.dp,
                    outlinedColor = Color.Gray,
                    icon = painterResource(id = R.drawable.google_ic),
                    onClick = {
                        focusManager.clearFocus()
                        // Handle Google login action
                    },
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .fillMaxWidth()
                        .height(55.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text(
                        text = "Already have an account?",
                        fontWeight = FontWeight.Normal,
                        color = Color.Black,
                    )
                    Text(
                        text = " Login!",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable(
                                onClick = {
                                    navigateToRoute(MainDestinations.LOGIN_ROUTE, true)
                                    registerViewModel.resetState()
                                },
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ),
                        color = primaryLight,
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
        }
    }
}

