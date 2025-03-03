package com.course.fleupart.ui.screen.authentication.onDataBoarding


import android.net.Uri
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.course.fleupart.ui.components.CustomButton
import com.course.fleupart.ui.components.CustomTextField
import com.course.fleupart.ui.components.CustomTopAppBar
import com.course.fleupart.ui.components.ImagePickerCard
import com.course.fleupart.ui.screen.navigation.FleupartSurface
import com.course.fleupart.ui.theme.primaryLight

@Composable
fun AddressScreen(
    modifier: Modifier = Modifier,
    viewModel: OnDataBoardingViewModel? = null
) {

    AddressScreen(
        modifier = modifier,
        id = 0
    )

}

@Composable
private fun AddressScreen(
    modifier: Modifier = Modifier,
    id: Int = 0
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

    var citizenCardUri by remember { mutableStateOf<Uri?>(null) }

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
                    showNavigationIcon = true
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
                            value = "",
                            onChange = {

                            },
                            placeholder = "Street Name",
                            isError = false,
                            errorMessage = "",
                            modifier = Modifier.padding(horizontal = 20.dp)
                        )
                    }

                    item {
                        CustomTextField(
                            value = "",
                            onChange = {

                            },
                            placeholder = "Sub-District",
                            isError = false,
                            errorMessage = "",
                            modifier = Modifier.padding(horizontal = 20.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                    }

                    item {
                        CustomTextField(
                            value = "",
                            onChange = {

                            },
                            placeholder = "City/District",
                            isError = false,
                            errorMessage = "",
                            modifier = Modifier.padding(horizontal = 20.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                    }

                    item {
                        CustomTextField(
                            value = "",
                            onChange = {

                            },
                            placeholder = "Province",
                            isError = false,
                            errorMessage = "",
                            modifier = Modifier.padding(horizontal = 20.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                    }

                    item {
                        CustomTextField(
                            value = "",
                            onChange = {

                            },
                            placeholder = "Postal Code",
                            isError = false,
                            errorMessage = "",
                            modifier = Modifier.padding(horizontal = 20.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                    }

                    item {
                        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                            ImagePickerCard(
                                label = "Shop Photo",
                                imageUri = citizenCardUri,
                                onImagePicked = { uri ->
                                    citizenCardUri = uri
                                }
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    item {
                        CustomTextField(
                            value = "",
                            onChange = {

                            },
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
                            onClick = {
//                        focusManager.clearFocus()
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
                            modifier = Modifier.padding(top = 30.dp)
                        )
                        Spacer(modifier = Modifier.height(100.dp))
                    }

                }
            }
        }
    }
}