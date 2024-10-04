package com.course.fleura.ui.screen.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.course.fleura.MainActivity

@Composable
fun NavGraph(
    navController: NavHostController,
    context: MainActivity,
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

        }

        composable(
            route = Screen.Cart.route
        ) {

        }

        composable(
            route = Screen.Point.route
        ) {

        }

        composable(
            route = Screen.Order.route
        ) {

        }

        composable(
            route = Screen.Profile.route
        ) {

        }
    }

}