package com.course.fleupart.ui.screen.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

object MainDestinations {
    const val DASHBOARD_ROUTE = "dashboard"
    const val ONBOARDING_ROUTE = "onboarding"
    const val WELCOME_ROUTE = "welcome"
    const val LOGIN_ROUTE = "login"
    const val REGISTER_PENDING_ROUTE = "register_pending"
    const val REGISTER_ROUTE = "register"
    const val USERNAME_ROUTE = "username"
    const val ADDRESS_ROUTE = "address"
    const val CITIZEN_ROUTE = "citizen"
    const val PHOTO_ROUTE = "photo"
    const val SNACK_DETAIL_ROUTE = "snack"
    const val SNACK_ID_KEY = "snackId"
    const val ORIGIN = "origin"
}

object DetailDestinations {
    const val PRODUCT_STATUS_ROUTE = "productStatus"
    const val ADD_PRODUCT_ROUTE = "addProduct"
    const val WITHDRAW_BALANCE_ROUTE = "withdrawBalance"
    const val BALANCE_AMOUNT_ROUTE = "balanceAmount"
    const val ADD_BANK_ACCOUNT_ROUTE = "addBankAccount"
}

@Composable
fun rememberFleupartNavController(
    navController: NavHostController = rememberNavController()
): FleupartNavController = remember(navController) {
    FleupartNavController(navController)
}

@Stable
class FleupartNavController(
    val navController: NavHostController,
) {

    fun upPress() {
        navController.navigateUp()
    }

    fun navigateToBottomBarRoute(route: String) {
        if (route != navController.currentDestination?.route) {
            navController.navigate(route) {
                launchSingleTop = true
                restoreState = true
                popUpTo(findStartDestination(navController.graph).id) {
                    saveState = true
                }
            }
        }
    }

    fun navigateToSnackDetail(snackId: Long, origin: String, from: NavBackStackEntry) {
        if (from.lifecycleIsResumed()) {
            navController.navigate("${MainDestinations.SNACK_DETAIL_ROUTE}/$snackId?origin=$origin")
        }
    }
}

private fun NavBackStackEntry.lifecycleIsResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED

private val NavGraph.startDestination: NavDestination?
    get() = findNode(startDestinationId)

private tailrec fun findStartDestination(graph: NavDestination): NavDestination {
    return if (graph is NavGraph) findStartDestination(graph.startDestination!!) else graph
}