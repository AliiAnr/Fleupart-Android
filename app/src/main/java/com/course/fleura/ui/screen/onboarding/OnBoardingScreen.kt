package com.course.fleura.ui.screen.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.course.fleura.ui.components.CustomButton
import kotlinx.coroutines.launch
import com.course.fleura.R
import com.course.fleura.ui.theme.base60
import com.course.fleura.ui.theme.base80
import com.course.fleura.ui.theme.onPrimaryLight
import com.course.fleura.ui.theme.primaryLight

@Composable
fun OnBoardingScreen(
) {
    val vectorImages = listOf(
        R.drawable.ob_1,
        R.drawable.ob_2,
        R.drawable.ob_3,
    )

    val titles = listOf(
        stringResource(R.string.title_ob_1),
        stringResource(R.string.title_ob_2),
        stringResource(R.string.title_ob_3),
    )
    val descriptions = listOf(
        stringResource(R.string.desc_ob_1),
        stringResource(R.string.desc_ob_2),
        stringResource(R.string.desc_ob_3),
    )
    val pagerState = rememberPagerState(pageCount = { vectorImages.size })

    Column(
        modifier = Modifier
            .background(onPrimaryLight)
            .padding(horizontal = 20.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
        ) { page ->
            OnboardingPage(
                vectorResId = vectorImages[page],
                title = titles[page],
                description = descriptions[page]
            )
        }

        HorizontalPagerIndicator(
            pagerState = pagerState,
            pageCount = vectorImages.size,
            modifier = Modifier
                .padding(top = 40.dp)
                .align(Alignment.CenterHorizontally),
            activeColor = primaryLight,
            inactiveColor = base60.copy(alpha = 0.3f)
        )

        ButtonSection(
            pagerState = pagerState,
//            navController = navController,
//            context = context
        )
    }
}

@Composable
fun ButtonSection(
    pagerState: PagerState,
//    navController: NavHostController,
//    context: MainActivity
) {
    val coroutineScope = rememberCoroutineScope()

    fun navigateToNextPage() {
        coroutineScope.launch {
            pagerState.animateScrollToPage(
                page = pagerState.currentPage + 1
            )
        }
    }

    fun navigateToPreviousPage() {
        coroutineScope.launch {
            if (pagerState.currentPage > 0) {
                pagerState.animateScrollToPage(
                    page = pagerState.currentPage - 1
                )
            }
        }
    }

    Box(
        modifier = Modifier
            .height(120.dp)
            .fillMaxWidth()
    ) {
        when (pagerState.currentPage) {
            0 -> {
                Text(
                    text = stringResource(R.string.next_page),
                    modifier = Modifier
                        .padding(end = 16.dp, bottom = 16.dp)
                        .align(Alignment.BottomEnd)
                        .clickable(onClick = ::navigateToNextPage),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black
                )
            }

            pagerState.pageCount - 1 -> {
                CustomButton(
                    text = stringResource(R.string.start),
                    onClick = {
//                        navController.popBackStack()
//                        navController.navigate(Graph.REGISTER)
                    },
                    modifier = Modifier.align(Alignment.BottomCenter)
                )

            }

            else -> {
                Text(
                    text = stringResource(R.string.next_page),
                    modifier = Modifier
                        .padding(end = 16.dp, bottom = 16.dp)
                        .align(Alignment.BottomEnd)
                        .clickable(onClick = ::navigateToNextPage),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black
                )

                Text(
                    text = stringResource(R.string.back_page),
                    modifier = Modifier
                        .padding(start = 16.dp, bottom = 16.dp)
                        .align(Alignment.BottomStart)
                        .clickable(onClick = ::navigateToPreviousPage),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun OnboardingPage(
    vectorResId: Int,
    title: String,
    description: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = vectorResId),
            contentDescription = null,
            modifier = Modifier
                .size(270.dp)
        )
        Text(
            text = title,
            textAlign = TextAlign.Center,
            fontSize = 32.sp,
            color = primaryLight,
            fontWeight = FontWeight.ExtraBold,
            lineHeight = 40.sp,
            modifier = Modifier
                .padding(top = 30.dp)
                .width(300.dp)
                .align(Alignment.CenterHorizontally)
        )
        Text(
            text = description,
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            color = base80,
            fontWeight = FontWeight.Bold,
            lineHeight = 24.sp,
            modifier = Modifier
                .padding(top = 15.dp)
                .width(325.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}


@Composable
fun HorizontalPagerIndicator(
    pagerState: PagerState,
    pageCount: Int,
    modifier: Modifier = Modifier,
    activeColor: Color = Color.Black,
    inactiveColor: Color = Color.Gray
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        for (i in 0 until pageCount) {
            val color = if (i == pagerState.currentPage) activeColor else inactiveColor
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .size(8.dp)
                    .background(color, shape = CircleShape)
            )
        }
    }
}
