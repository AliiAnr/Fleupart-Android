package com.course.fleura.ui.screen.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.course.fleura.ui.screen.navigation.NavItem
import com.course.fleura.ui.screen.navigation.Screen
import com.course.fleura.ui.screen.navigation.getNavItem
import com.course.fleura.ui.theme.base40
import com.course.fleura.R

@Composable
fun BottomNavBar(
    context : Context,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    var selectedItem by rememberSaveable { mutableStateOf(Screen.Home.route) }

    Box(
        modifier = Modifier
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
            .height(70.dp)
            .background(MaterialTheme.colorScheme.onPrimary),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {

            getNavItem(context = context).forEach { item ->
                BottomNavBarItem(
                    item = item,
                    isSelected = selectedItem == item.screen.route,
                    onClick = {
                        if (selectedItem != item.screen.route) {
                            selectedItem = item.screen.route
                            navController.navigate(item.screen.route) {
                                popUpTo(Screen.Home.route) {
                                    saveState = true
                                }
                                restoreState = true
                                launchSingleTop = true
                            }
                        }
                    }
                )
            }

        }
    }
}

@Composable
fun BottomNavBarItem(
    modifier: Modifier = Modifier,
    item: NavItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {

    val icon = if (isSelected) item.iconSelected else item.icon

    Column (
        modifier = modifier
            .clickable(
                onClick = onClick,
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
                contentDescription = item.title,
                tint = if (isSelected) MaterialTheme.colorScheme.primary else base40,
                modifier = Modifier.size(24.dp)
            )

            Text(
                text = item.title,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) MaterialTheme.colorScheme.primary else base40
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun PrevButtonItem() {
    BottomNavBarItem(
        item = NavItem(
            title = "Home",
            icon = R.drawable.home_non,
            iconSelected = R.drawable.home_filled,
            screen = Screen.Home,

            ),
        isSelected = false,
        onClick = {}
    )
}