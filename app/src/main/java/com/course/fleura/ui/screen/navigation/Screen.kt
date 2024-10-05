package com.course.fleura.ui.screen.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Cart : Screen("cart")
    object Point : Screen("point")
    object Order : Screen("order")
    object Profile : Screen("profile")
    object DetailTest : Screen("detailtest")
}