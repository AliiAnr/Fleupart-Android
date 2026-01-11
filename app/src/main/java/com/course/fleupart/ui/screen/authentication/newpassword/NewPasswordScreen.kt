package com.course.fleupart.ui.screen.authentication.newpassword

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.course.fleupart.ui.components.CustomButton
import com.course.fleupart.ui.components.CustomTextField
import com.course.fleupart.ui.components.CustomTopAppBar
import com.course.fleupart.ui.screen.navigation.FleupartSurface
import com.course.fleupart.ui.theme.base300


@Composable
fun NewPasswordScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit
) {
    NewPasswordScreen(
        id = 0,
        onBackClick = onBackClick
    )
}

@Composable
private fun NewPasswordScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    id: Int = 0,
) {
    val focusManager = LocalFocusManager.current
    val passwordValue = remember { mutableStateOf("") }
    val confirmPasswordValue = remember { mutableStateOf("") }

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
                }
                .statusBarsPadding()
                .padding(horizontal = 20.dp),
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
                    showNavigationIcon = true,
                    horizontalPadding = 0.dp,
                    onBackClick = {
                        onBackClick()
                    }
                )
                Column(
                    modifier = Modifier
                ) {
                    Text(
                        text = "Create New Password",
                        fontSize = 24.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 20.dp)
                    )
                    Text(
                        text = "Enter a new password to reset the password on your account",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = base300,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    CustomTextField(
                        value = passwordValue.value,
                        onChange = {
                            passwordValue.value = it
                        },
                        placeholder = "Password",
                        isError = false,
                        icon = Icons.Rounded.Lock,
                        isPassword = true,
                        errorMessage = "dksamkdms",
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    CustomTextField(
                        value = confirmPasswordValue.value,
                        onChange = {
                            confirmPasswordValue.value = it
                        },
                        placeholder = "Password",
                        isError = false,
                        icon = Icons.Rounded.Lock,
                        isPassword = true,
                        errorMessage = "daksdkasj",
                    )
                }

                CustomButton(
                    text = "Reset Password",
                    isOutlined = false,
                    isAvailable = (passwordValue.value.isNotEmpty() && confirmPasswordValue.value.isNotEmpty()) && (passwordValue.value == confirmPasswordValue.value),
                    outlinedColor = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    onClick = {
                        focusManager.clearFocus()
//                        viewModel.register(
//                            onSuccess = {
////                            navController.popBackStack()
////                            navController.navigate(Screen.Home.route)
//                            },
//                            onError = { errorMessage ->
////                            handleLoginError(context, errorMessage)
//                            }
//                        )
                    },
                    modifier = Modifier.padding(top = 50.dp)
                )

            }
        }
    }
}
