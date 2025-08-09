package com.course.fleupart.ui.screen.authentication.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
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
import com.course.fleupart.ui.components.CustomButton
import com.course.fleupart.ui.screen.navigation.FleupartSurface
import com.course.fleupart.ui.screen.navigation.MainDestinations
import com.course.fleupart.ui.theme.base80
import com.course.fleupart.ui.theme.primaryLight

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    navigateToRoute: (String, Boolean) -> Unit
) {
    FleupartSurface(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.welcome),
                contentDescription = null,
                modifier = Modifier
                    .size(250.dp)
                    .padding(bottom = 16.dp)
            )

            Spacer(modifier = Modifier.height(25.dp))

            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = Color.Black,
                            fontWeight = FontWeight.ExtraBold
                        )
                    ) {
                        append("Start ")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = primaryLight,
                            fontWeight = FontWeight.ExtraBold
                        )
                    ) {
                        append("growing ")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = Color.Black,
                            fontWeight = FontWeight.ExtraBold
                        )
                    ) {
                        append("your ")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFFFFD700),
                            fontWeight = FontWeight.ExtraBold
                        )
                    ) {
                        append("business ")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = Color.Black,
                            fontWeight = FontWeight.ExtraBold
                        )
                    ) {
                        append("here. ")
                    }
                },
                fontSize = 34.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.ExtraBold,
                lineHeight = 44.sp,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Grow your flower business with our platform, connecting you to customers who appreciate quality blooms",
                fontSize = 16.sp,
                lineHeight = 24.sp,
                textAlign = TextAlign.Start,
                color = base80,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(26.dp))

            CustomButton(
                text = "Login",
                backgroundColor = primaryLight,
                textColor = Color.White,
                shape = RoundedCornerShape(50.dp),
                fontWeight = FontWeight.Bold,
                onClick = {
                    navigateToRoute(MainDestinations.LOGIN_ROUTE, false)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
            )

            Spacer(modifier = Modifier.height(28.dp))

            CustomButton(
                text = "Register",
                isOutlined = true,
                outlinedColor = Color.Black,
                shape = RoundedCornerShape(50.dp),
                fontWeight = FontWeight.Bold,
                onClick = {
                    navigateToRoute(MainDestinations.REGISTER_ROUTE, false)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
            )
        }
    }
}
