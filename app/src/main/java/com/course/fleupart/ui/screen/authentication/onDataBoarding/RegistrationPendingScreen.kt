package com.course.fleupart.ui.screen.authentication.onDataBoarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.course.fleupart.R
import com.course.fleupart.ui.screen.navigation.FleupartSurface
import com.course.fleupart.ui.theme.base80
import com.course.fleupart.ui.theme.primaryLight


@Composable
fun RegistrationPendingScreen(
    modifier: Modifier = Modifier,
    viewModel: OnDataBoardingViewModel? = null
) {

    RegistrationPendingScreen(
        modifier = modifier,
        id = 0
    )

}


@Composable
private fun RegistrationPendingScreen(
    modifier: Modifier = Modifier,
    id: Int = 0
) {

    val title = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color(0xFFFFD700), fontWeight = FontWeight.ExtraBold)) {
            append("Oops! ")
        }

        withStyle(style = SpanStyle(color = primaryLight, fontWeight = FontWeight.ExtraBold)) {
            append("Your registration haven’t been accepted by system!")
        }


    }

    FleupartSurface(
        modifier = modifier
            .fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = title,
                    fontSize = 22.sp,
                    lineHeight = 32.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(350.dp)
                )

                Spacer(modifier = Modifier.height(25.dp))

                Image(
                    painter = painterResource(R.drawable.pending_regis),
                    contentDescription = "Registration Pending",
                    modifier = Modifier.width(250.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "You can access merchant dashboard when admin already accept your registration. Please check your email regularly.",
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    color = base80,
                    modifier = Modifier.width(250.dp)
                )
            }
        }
    }
}