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
import co.yml.charts.common.extensions.isNotNull
import com.course.fleupart.ui.common.ResultResponse
import com.course.fleupart.ui.components.CustomButton
import com.course.fleupart.ui.components.CustomTopAppBar
import com.course.fleupart.ui.components.ImagePickerCard
import com.course.fleupart.ui.screen.navigation.FleupartSurface
import com.course.fleupart.ui.screen.navigation.MainDestinations
import com.course.fleupart.ui.theme.primaryLight

@Composable
fun PhotoScreen(
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
                navigateToRoute(MainDestinations.ADDRESS_ROUTE, true)
                onDataBoardingViewModel.setPersonalizeState(ResultResponse.None)
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

    PhotoScreen(
        modifier = modifier,
        showCircularProgress = showCircularProgress,
        setCircularProgress = { value ->
            showCircularProgress = value
        },
        onDataBoardingViewModel = onDataBoardingViewModel,
        id = 1
    )

}

@Composable
private fun PhotoScreen(
    modifier: Modifier = Modifier,
    onDataBoardingViewModel: OnDataBoardingViewModel,
    showCircularProgress: Boolean,
    setCircularProgress : (Boolean) -> Unit = {},
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
                            imageUri = onDataBoardingViewModel.citizenCardPictureValue,
                            onImagePicked = { uri ->
                                onDataBoardingViewModel.setCitizenCardPicture(uri)
                            }
                        )
                    }

                Spacer(modifier = Modifier.height(10.dp))
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    ImagePickerCard(
                        label = "Self Picture",
                        imageUri = onDataBoardingViewModel.selfPictureValue,
                        onImagePicked = { uri ->
                            onDataBoardingViewModel.setSelfPicture(uri)
                        }
                    )
                }

                CustomButton(
                    text = "Next",
                    isOutlined = true,
                    isAvailable = onDataBoardingViewModel.selfPictureValue.isNotNull() && onDataBoardingViewModel.citizenCardPictureValue.isNotNull(),
                    outlinedColor = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    onClick = {
                        setCircularProgress(true)
                        onDataBoardingViewModel.uploadUserPhotos()
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