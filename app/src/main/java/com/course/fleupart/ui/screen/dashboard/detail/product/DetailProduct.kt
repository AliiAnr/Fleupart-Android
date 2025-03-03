package com.course.fleupart.ui.screen.dashboard.detail.product

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.course.fleupart.R
import com.course.fleupart.ui.common.formatCurrency
import com.course.fleupart.ui.components.CustomTopAppBar
import com.course.fleupart.ui.screen.dashboard.product.Product
import com.course.fleupart.ui.screen.navigation.FleupartSurface
import com.course.fleupart.ui.theme.base20
import com.course.fleupart.ui.theme.base300

@Composable
fun DetailProduct(
    modifier: Modifier = Modifier
) {

    DetailProduct(
        modifier = modifier,
        id = 0
    )
}

@Composable
private fun DetailProduct(
    modifier: Modifier = Modifier,
    id: Int = 0
) {
    FleupartSurface(
        modifier = modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .background(base20),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CustomTopAppBar(
                    title = "Product Status",
                    showNavigationIcon = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                HeaderSection()
                Spacer(modifier = Modifier.height(8.dp))
                DescriptionSection()
                Spacer(modifier = Modifier.height(8.dp))
                ReviewStatusSection()
                Spacer(modifier = Modifier.height(8.dp))
                NoteSection()
            }
        }
    }
}

@Composable
private fun HeaderSection(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Detail Product",
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.cc_1),
            contentDescription = "Detail Product",
            modifier = Modifier.size(100.dp).clip(RoundedCornerShape(10.dp))
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Price",
                fontSize = 14.sp,
                fontWeight = FontWeight.W600,
                color = base300
            )
            Text(
                text = formatCurrency(50000),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = base300
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Category",
                fontSize = 14.sp,
                fontWeight = FontWeight.W600,
                color = base300
            )
            Text(
                text = "bukett",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = base300
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Arrange time",
                fontSize = 14.sp,
                fontWeight = FontWeight.W600,
                color = base300
            )
            Text(
                text = "dadadda",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = base300
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Pre-Order",
                fontSize = 14.sp,
                fontWeight = FontWeight.W600,
                color = base300
            )
            Text(
                text = "Yes",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = base300
            )
        }
    }
}

@Composable
private fun DescriptionSection(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 14.dp),
    ) {
        Text(
            text = "Description",
            fontSize = 14.sp,
            fontWeight = FontWeight.W600,
            color = base300
        )
        Text(
            text = "Bouquet filled with bright sunflower and small flowers.",
            fontSize = 12.sp,
            color = base300
        )
    }
}

@Composable
private fun ReviewStatusSection(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Review Status",
            fontSize = 14.sp,
            fontWeight = FontWeight.W600,
            color = base300
        )
        Text(
            text = "Accepted",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF8BC34A)
        )
    }
}

@Composable
private fun NoteSection(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 14.dp),
    ) {
        Text(
            text = "Note",
            fontSize = 14.sp,
            fontWeight = FontWeight.W600,
            color = base300
        )
        Text(
            text = "Bouquet filled with bright sunflower and small flowers.",
            fontSize = 12.sp,
            color = base300
        )
    }
}