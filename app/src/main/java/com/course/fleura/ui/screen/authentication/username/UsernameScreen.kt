package com.course.fleura.ui.screen.authentication.username

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.course.fleura.ui.components.CustomButton
import com.course.fleura.ui.components.CustomTextField
import com.course.fleura.ui.screen.navigation.FleuraSurface
import com.course.fleura.ui.theme.primaryLight

@Composable
fun UsernameScreen(
//    navController: NavHostController,
//    context: MainActivity,
    modifier: Modifier = Modifier,
) {
    val viewModel: UsernameScreenViewModel = remember { UsernameScreenViewModel() }
    val isLoading by viewModel.loading.collectAsState(initial = false)
    val focusManager = LocalFocusManager.current

    val title = buildAnnotatedString {
        withStyle(style = SpanStyle(color = primaryLight, fontWeight = FontWeight.ExtraBold)) {
            append("How should we call you?")
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
                        .padding(top = 120.dp, bottom = 16.dp)
                        .align(Alignment.Start)
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Please enter your nickname (one word only)",
                        textAlign = TextAlign.Start,
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 50.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    CustomTextField(
                        value = viewModel.usernameValue,
                        onChange = viewModel::setUsername,
                        placeholder = "Username",
                        isError = viewModel.usernameError.isNotEmpty(),
                        icon = Icons.Rounded.Email,
                        errorMessage = viewModel.usernameError,
                        modifier = Modifier.padding(bottom = 40.dp)
                    )
                }

                CustomButton(
                    text = "Next",
                    onClick = {
                        focusManager.clearFocus()
//                        viewModel.login(
//                            onSuccess = {
////                            navController.popBackStack()
////                            navController.navigate(Screen.Home.route)
//                            },
//                            onError = { errorMessage ->
////                            handleLoginError(context, errorMessage)
//                            }
//                        )
                    },
                )

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
}

//private fun handleLoginError(context: Context, errorMessage: String) {
//    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
//}
