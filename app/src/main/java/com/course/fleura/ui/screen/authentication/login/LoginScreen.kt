package com.course.fleura.ui.screen.authentication.login

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.course.fleura.R
import com.course.fleura.ui.components.CustomButton
import com.course.fleura.ui.components.CustomTextField
import com.course.fleura.ui.components.TextFieldViewModel
import com.course.fleura.ui.screen.navigation.FleuraSurface
import com.course.fleura.ui.theme.primaryLight

@Composable
fun LoginScreen(
//    navController: NavHostController,
//    context: MainActivity,
    modifier: Modifier = Modifier,
) {
    val viewModel: TextFieldViewModel = remember { TextFieldViewModel() }
    val isLoading by viewModel.loading.collectAsState(initial = false)
    val focusManager = LocalFocusManager.current

    val title = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color(0xFFFFD700), fontWeight = FontWeight.ExtraBold)) {
            append("Welcome back!")
        }
        withStyle(style = SpanStyle(color = primaryLight, fontWeight = FontWeight.ExtraBold)) {
            append("\nGlad to see you")
        }

    }

    FleuraSurface(modifier = modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = title,
                    textAlign = TextAlign.Start,
                    fontSize = 36.sp,
                    color = primaryLight,
                    fontWeight = FontWeight.ExtraBold,
                    lineHeight = 50.sp,
                    modifier = Modifier
                        .padding(top = 120.dp, bottom = 30.dp)
                        .align(Alignment.Start)
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    CustomTextField(
                        value = viewModel.emailValue,
                        onChange = viewModel::setEmail,
                        placeholder = "Email",
                        isError = viewModel.emailError.isNotEmpty(),
                        icon = Icons.Rounded.Email,
                        errorMessage = viewModel.emailError,
                        keyboardType = KeyboardType.Email,
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    CustomTextField(
                        value = viewModel.passwordValue,
                        onChange = viewModel::setPassword,
                        placeholder = "Password",
                        isError = viewModel.passwordError.isNotEmpty(),
                        icon = Icons.Rounded.Lock,
                        isPassword = true,
                        errorMessage = viewModel.passwordError,
                    )
                    Text(
                        text = "Forgot Password?",
                        modifier = Modifier
                            .clickable {
//                            navController.navigate(Graph.FORGOT_PASSWORD)
                            }
                            .align(Alignment.End)
                            .padding(bottom = 35.dp),
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                    )
                }

                CustomButton(
                    text = "Sign In",
                    onClick = {
                        focusManager.clearFocus()
                        viewModel.login(
                            onSuccess = {
//                            navController.popBackStack()
//                            navController.navigate(Screen.Home.route)
                            },
                            onError = { errorMessage ->
//                            handleLoginError(context, errorMessage)
                            }
                        )
                    },
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp)
                ) {
                    Divider(
                        color = Color.Gray,
                        thickness = 1.5.dp,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = " Or Login with ",
                        color = Color(0xFF1C1C1C),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    Divider(
                        color = Color.Gray,
                        thickness = 1.5.dp,
                        modifier = Modifier.weight(1f)
                    )
                }

                CustomButton(
                    text = "",
                    isOutlined = true,
                    borderWidth = 1.dp,
                    outlinedColor = Color.Gray,
                    icon = painterResource(id = R.drawable.google_ic),
                    onClick = {
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
                        text = "Don't Have Account?",
                        fontWeight = FontWeight.Normal,
                        color = Color.Black,
                    )
                    Text(
                        text = " Register!",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable(
                                onClick = {
//                                navController.popBackStack()
//                                navController.navigate(Graph.REGISTER)
                                },
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ),
                        color = primaryLight,
                    )
                }
            }

            if (isLoading) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.8f))
                ) {
                    CircularProgressIndicator(color = primaryLight)
                }
            }
        }
    }
}

//private fun handleLoginError(context: Context, errorMessage: String) {
//    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
//}
