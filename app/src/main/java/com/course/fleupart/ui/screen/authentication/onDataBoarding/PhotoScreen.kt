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
import com.course.fleupart.ui.common.ResultResponse
import com.course.fleupart.ui.components.CustomButton
import com.course.fleupart.ui.components.CustomTextField
import com.course.fleupart.ui.components.CustomTopAppBar
import com.course.fleupart.ui.components.ImagePickerCard
import com.course.fleupart.ui.screen.navigation.FleupartSurface
import com.course.fleupart.ui.theme.primaryLight

@Composable
fun PhotoScreen(
    modifier: Modifier = Modifier,
    onDataBoardingViewModel: OnDataBoardingViewModel,
    navigateToRoute: (String, Boolean) -> Unit,
) {

    PhotoScreen(
        modifier = modifier,
        onDataBoardingViewModel = onDataBoardingViewModel,
        id = 1
    )

}

@Composable
private fun PhotoScreen(
    modifier: Modifier = Modifier,
    onDataBoardingViewModel: OnDataBoardingViewModel,
    id: Int = 0
) {

    val focusManager = LocalFocusManager.current
    val title = buildAnnotatedString {
        withStyle(style = SpanStyle(color = primaryLight, fontWeight = FontWeight.ExtraBold)) {
            append("Make sure your ")
        }

        withStyle(style = SpanStyle(color = Color(0xFFFFD700), fontWeight = FontWeight.ExtraBold)) {
            append("photos ")
        }

        withStyle(style = SpanStyle(color = primaryLight, fontWeight = FontWeight.ExtraBold)) {
            append("are clear!")
        }

    }

    var citizenCardUri by remember { mutableStateOf<Uri?>(null) }
    var selfPictureUri by remember { mutableStateOf<Uri?>(null) }

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

                    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                        ImagePickerCard(
                            label = "Citizen Card Picture",
                            imageUri = citizenCardUri,
                            onImagePicked = { uri ->
                                citizenCardUri = uri
                            }
                        )
                    }

                Spacer(modifier = Modifier.height(10.dp))
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    ImagePickerCard(
                        label = "Self Picture",
                        imageUri = selfPictureUri,
                        onImagePicked = { uri ->
                            selfPictureUri = uri
                        }
                    )
                }
                CustomButton(
                    text = "Next",
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
            }
        }
    }
}