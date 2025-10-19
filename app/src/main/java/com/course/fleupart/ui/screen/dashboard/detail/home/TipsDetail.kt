package com.course.fleupart.ui.screen.dashboard.detail.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.course.fleupart.R
import com.course.fleupart.ui.components.CustomTopAppBar
import com.course.fleupart.ui.components.FakeCategory
import com.course.fleupart.ui.components.TipsContent
import com.course.fleupart.ui.components.TipsDetail
import com.course.fleupart.ui.screen.navigation.FleupartSurface
import com.course.fleupart.ui.theme.base100
import com.course.fleupart.ui.theme.base20


@Composable
fun TipsDetail(
    tipsId: Long,
    onBackClick: () -> Unit
) {
    val tipsDetail = FakeCategory.tipsDetailList.find { it.id == tipsId }

    if (tipsDetail == null) {
        TipsNotFound(onBackClick = onBackClick)
        return
    }

    TipsDetailContent(
        tipsDetail = tipsDetail,
        onBackClick = onBackClick
    )
}

@Composable
private fun TipsDetailContent(
    tipsDetail: TipsDetail,
    onBackClick: () -> Unit
) {
    FleupartSurface(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
            ) {
                // Header with back button
                item {
//                    TipsDetailHeader(
//                        onBackClick = onBackClick
//                    )
                    CustomTopAppBar(
                        title = "Tips Detail",
                        showNavigationIcon = true,
                        onBackClick = onBackClick
                    )
                }

                // Hero Image
                item {
                    TipsHeroImage(
                        imageRes = tipsDetail.imageRes
                    )
                }

                // Title and Meta Info
                item {
                    TipsTitleSection(
                        title = tipsDetail.title,
                        author = tipsDetail.author,
                        readTime = tipsDetail.readTime,
                        publishedDate = tipsDetail.publishedDate
                    )
                }

                // Description
                item {
                    TipsDescription(
                        description = tipsDetail.description
                    )
                }

                // Content Sections
                items(tipsDetail.content) { content ->
                    TipsContentSection(content = content)
                }

                // Bottom Spacer
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
private fun TipsDetailHeader(
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.back_arrow),
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier
                    .graphicsLayer(rotationZ = 180f)
            )
        }

        Text(
            text = "Tips Detail",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Composable
private fun TipsHeroImage(
    imageRes: Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Icon(
            painter = painterResource(imageRes),
            contentDescription = "Tips Image",
            tint = Color.Unspecified,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
        )
    }
}

@Composable
private fun TipsTitleSection(
    title: String,
    author: String,
    readTime: String,
    publishedDate: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Text(
            text = title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            lineHeight = 32.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "By $author",
                fontSize = 12.sp,
                color = base100,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = " • ",
                fontSize = 12.sp,
                color = base100
            )

            Text(
                text = readTime,
                fontSize = 12.sp,
                color = base100
            )

            Text(
                text = " • ",
                fontSize = 12.sp,
                color = base100
            )

            Text(
                text = publishedDate,
                fontSize = 12.sp,
                color = base100
            )
        }
    }
}

@Composable
private fun TipsDescription(
    description: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Text(
            text = description,
            fontSize = 14.sp,
            color = Color.Black,
            lineHeight = 22.sp,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
private fun TipsContentSection(
    content: TipsContent
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = content.subtitle,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = content.body,
                fontSize = 14.sp,
                color = Color.Black,
                lineHeight = 22.sp
            )
        }
    }
}

@Composable
private fun TipsNotFound(
    onBackClick: () -> Unit
) {
    FleupartSurface(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(base20)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
            ) {
                TipsDetailHeader(onBackClick = onBackClick)

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Tips Not Found",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "The tips you're looking for doesn't exist.",
                        fontSize = 14.sp,
                        color = base100,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}