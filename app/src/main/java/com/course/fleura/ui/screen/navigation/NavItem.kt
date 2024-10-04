package com.course.fleura.ui.screen.navigation

import android.content.Context
import com.course.fleura.R

data class NavItem(
    val title: String,
    val icon: Int,
    val iconSelected: Int,
    val screen: Screen
)

fun getNavItem(context: Context): List<NavItem> {
    return listOf(
        NavItem(
            title = context.getString(R.string.menu_home),
            icon = R.drawable.home_non,
            iconSelected = R.drawable.home_filled,
            screen = Screen.Home
        ),
        NavItem(
            title = context.getString(R.string.menu_cart),
            icon = R.drawable.shopping_cart_non,
            iconSelected = R.drawable.shopping_cart_filled,
            screen = Screen.Cart
        ),
        NavItem(
            title = context.getString(R.string.menu_point),
            icon = R.drawable.crown_non,
            iconSelected = R.drawable.crown_filled,
            screen = Screen.Point
        ),
        NavItem(
            title = context.getString(R.string.menu_order),
            icon = R.drawable.note_non,
            iconSelected = R.drawable.note_filled,
            screen = Screen.Order
        ),
        NavItem(
            title = context.getString(R.string.menu_profile),
            icon = R.drawable.profile_non,
            iconSelected = R.drawable.profile_filled,
            screen = Screen.Profile
        ),
    )
}