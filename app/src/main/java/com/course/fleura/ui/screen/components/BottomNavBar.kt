package com.course.fleura.ui.screen.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.course.fleura.R
import com.course.fleura.ui.screen.navigation.FleuraSurface
import com.course.fleura.ui.theme.base40
import com.course.fleura.ui.theme.onPrimaryLight
import com.course.fleura.ui.theme.primaryLight

enum class HomeSections(
    @StringRes val title: Int,
    val icon_non: Int,
    val icon_filled: Int,
    val route: String
) {
    Home(R.string.menu_home, R.drawable.home_non, R.drawable.home_filled, "home"),
    Cart(R.string.menu_cart, R.drawable.shopping_cart_non, R.drawable.shopping_cart_filled, "cart"),
    Point(R.string.menu_point, R.drawable.crown_non, R.drawable.crown_filled, "point"),
    Order(R.string.menu_order, R.drawable.note_non, R.drawable.note_filled, "order"),
    Profile(R.string.menu_profile, R.drawable.profile_non, R.drawable.profile_filled, "profile")
}

@Composable
fun FleuraBottomBar(
    tabs: Array<HomeSections>,
    currentRoute: String,
    navigateToRoute: (String) -> Unit,
    modifier: Modifier = Modifier,
    color: Color = onPrimaryLight,
    contentColor: Color = base40
) {
    val currentSection = tabs.first { it.route == currentRoute }

    FleuraSurface(
        modifier = modifier,
        color = color,
        contentColor = contentColor
    ) {
        Box(
            modifier = Modifier
                .navigationBarsPadding()
                .drawWithContent {
                    drawContent()
                    drawLine(
                        color = base40,
                        start = Offset.Zero,
                        end = Offset(size.width, 0f),
                        strokeWidth = 0.6.dp.toPx()
                    )
                }
                .fillMaxWidth()
                .height(80.dp)
                .background(onPrimaryLight),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                tabs.forEach { section ->
                    val selected = section == currentSection
                    val tint = if (selected) primaryLight else base40
                    val icon = if (selected) section.icon_filled else section.icon_non

                    Column(
                        modifier = Modifier
                            .clickable(
                                onClick = { navigateToRoute(section.route) },
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = icon),
                                contentDescription = stringResource(section.title),
                                tint = tint,
                                modifier = Modifier.size(24.dp)
                            )

                            Text(
                                text = stringResource(section.title),
                                fontSize = 12.sp,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                                color = tint
                            )
                        }
                    }
                }
            }
        }
    }
}
