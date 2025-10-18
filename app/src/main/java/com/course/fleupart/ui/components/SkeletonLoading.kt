package com.course.fleupart.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.course.fleupart.ui.common.loadingFx


@Composable
fun ProductListLoading(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp)
            .padding(bottom = 10.dp, top = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(15.dp)
                    .loadingFx()
            )

            Box(
                modifier = Modifier
                    .padding(top = 5.dp, bottom = 2.dp)
                    .width(20.dp)
                    .height(15.dp)
                    .loadingFx()
            )

        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(top = 2.dp)
        )
        {
            items(3) {
                ProductItemLoading()
            }
        }
    }
}

@Composable
fun ProductItemLoading(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .width(140.dp),
    ) {
        Box(
            modifier = Modifier
                .padding(top = 5.dp, bottom = 2.dp)
                .fillMaxWidth()
                .height(110.dp)
                .clip(RoundedCornerShape(10.dp))
                .loadingFx()
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .padding(top = 10.dp)
                .height(12.dp)
                .fillMaxWidth(0.8f)
                .loadingFx()
        )
        Box(
            modifier = Modifier
                .padding(top = 10.dp, bottom = 2.dp)
                .height(10.dp)
                .fillMaxWidth(0.4f)
                .loadingFx()
        )
        Box(
            modifier = Modifier
                .padding(top = 10.dp, bottom = 2.dp)
                .height(10.dp)
                .fillMaxWidth(0.6f)
                .loadingFx()
        )
    }
}
