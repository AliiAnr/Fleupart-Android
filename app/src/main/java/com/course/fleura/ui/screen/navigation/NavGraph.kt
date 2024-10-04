package com.course.fleura.ui.screen.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.course.fleura.MainActivity
import com.course.fleura.ui.screen.dashboard.cart.Cart
import com.course.fleura.ui.screen.dashboard.home.Home
import com.course.fleura.ui.screen.dashboard.order.Order
import com.course.fleura.ui.screen.dashboard.point.Point
import com.course.fleura.ui.screen.dashboard.profile.Profile
import com.course.fleura.ui.screen.navigation.Screen.Order

@Composable
fun NavGraph(
    navController: NavHostController,
    context: MainActivity,
    innerPadding : PaddingValues,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {

        composable(
            route = Screen.Home.route
        ) {
            Home()
        }

        composable(
            route = Screen.Cart.route
        ) {
            Cart()
        }

        composable(
            route = Screen.Point.route
        ) {
            Point()
        }

        composable(
            route = Screen.Order.route
        ) {
            Order()
        }

        composable(
            route = Screen.Profile.route
        ) {
            Profile()
        }
    }

}