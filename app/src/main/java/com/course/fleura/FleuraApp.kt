package com.course.fleura

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.course.fleura.ui.screen.components.BottomNavBar
import com.course.fleura.ui.screen.navigation.NavGraph
import com.course.fleura.ui.screen.navigation.getNavItem

@Composable
fun FleuraApp(
    navController: NavHostController =  rememberNavController(),
    context: MainActivity,
    modifier: Modifier
) {
    Scaffold (
        bottomBar = {
            BottomBar(
                context = context,
                navController = navController
            )
        }
    ){
        NavGraph(
            navController = navController,
            context = context,
            innerPadding = it,
        )
    }
}

@Composable
fun BottomBar(
    context: MainActivity,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val bottomNavigation = getNavItem(context).any { it.screen.route == currentRoute }
    if (bottomNavigation) {
        BottomNavBar(
            context = context,
            modifier = modifier,
            navController = navController
        )
    }
}

