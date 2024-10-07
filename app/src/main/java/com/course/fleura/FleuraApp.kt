@file:OptIn(
    ExperimentalSharedTransitionApi::class
)

package com.course.fleura

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.course.fleura.ui.components.FleuraBottomBar
import com.course.fleura.ui.components.HomeSections
import com.course.fleura.ui.screen.authentication.login.LoginScreen
import com.course.fleura.ui.screen.authentication.welcome.WelcomeScreen
import com.course.fleura.ui.screen.dashboard.detail.home.DetailTest
import com.course.fleura.ui.screen.navigation.FleuraScaffold
import com.course.fleura.ui.screen.navigation.MainDestinations
import com.course.fleura.ui.screen.navigation.addHomeGraph
import com.course.fleura.ui.screen.navigation.composableWithCompositionLocal
import com.course.fleura.ui.screen.navigation.nonSpatialExpressiveSpring
import com.course.fleura.ui.screen.navigation.rememberFleuraNavController
import com.course.fleura.ui.screen.navigation.rememberFleuraScaffoldState
import com.course.fleura.ui.screen.navigation.spatialExpressiveSpring
import com.course.fleura.ui.screen.onboarding.OnBoardingScreen
import com.course.fleura.ui.theme.FleuraTheme

@Composable
fun FleuraApp() {
    FleuraTheme {
        val fleuraNavController = rememberFleuraNavController()
        SharedTransitionLayout {
            CompositionLocalProvider(
                LocalSharedTransitionScope provides this
            ) {
                NavHost(
                    navController = fleuraNavController.navController,
                    startDestination = MainDestinations.LOGIN_ROUTE,
                    contentAlignment = Alignment.Center
                ) {
                    composableWithCompositionLocal(
                        route = MainDestinations.ONBOARDING_ROUTE
                    ) { backStackEntry ->
                        OnBoardingScreen()
                    }

                    composableWithCompositionLocal(
                        route = MainDestinations.LOGIN_ROUTE
                    ) { backStackEntry ->
                        LoginScreen()
                    }

                    composableWithCompositionLocal(
                        route = MainDestinations.WELCOME_ROUTE
                    ) { backStackEntry ->
                        WelcomeScreen()
                    }

                    composableWithCompositionLocal(
                        route = MainDestinations.DASHBOARD_ROUTE
                    ) { backStackEntry ->
                        MainContainer(
                            onSnackSelected = fleuraNavController::navigateToSnackDetail
                        )
                    }

                    composableWithCompositionLocal(
                        "${MainDestinations.SNACK_DETAIL_ROUTE}/" +
                                "{${MainDestinations.SNACK_ID_KEY}}" +
                                "?origin={${MainDestinations.ORIGIN}}",
                        arguments = listOf(
                            navArgument(MainDestinations.SNACK_ID_KEY) {
                                type = NavType.LongType
                            }
                        ),

                        ) { backStackEntry ->
                        val arguments = requireNotNull(backStackEntry.arguments)
                        val id = arguments.getLong(MainDestinations.SNACK_ID_KEY)
                        val origin = arguments.getString(MainDestinations.ORIGIN)
                        DetailTest(
                            id,
                            origin = origin ?: "",
                            upPress = fleuraNavController::upPress
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MainContainer(
    modifier: Modifier = Modifier,
    onSnackSelected: (Long, String, NavBackStackEntry) -> Unit
) {
    val fleuraScaffoldState = rememberFleuraScaffoldState()
    val nestedNavController = rememberFleuraNavController()
    val navBackStackEntry by nestedNavController.navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val sharedTransitionScope = LocalSharedTransitionScope.current
        ?: throw IllegalStateException("No SharedElementScope found")
    val animatedVisibilityScope = LocalNavAnimatedVisibilityScope.current
        ?: throw IllegalStateException("No SharedElementScope found")
    FleuraScaffold(
        bottomBar = {
            with(animatedVisibilityScope) {
                with(sharedTransitionScope) {
                    FleuraBottomBar(
                        tabs = HomeSections.entries.toTypedArray(),
                        currentRoute = currentRoute ?: HomeSections.Home.route,
                        navigateToRoute = nestedNavController::navigateToBottomBarRoute,
                        modifier = Modifier
                            .renderInSharedTransitionScopeOverlay(
                                zIndexInOverlay = 1f,
                            )
                            .animateEnterExit(
                                enter = fadeIn(nonSpatialExpressiveSpring()) + slideInVertically(
                                    spatialExpressiveSpring()
                                ) {
                                    it
                                },
                                exit = fadeOut(nonSpatialExpressiveSpring()) + slideOutVertically(
                                    spatialExpressiveSpring()
                                ) {
                                    it
                                }
                            )
                    )
                }
            }
        },
        modifier = modifier

    ) { padding ->
        NavHost(
            navController = nestedNavController.navController,
            startDestination = HomeSections.Home.route,
            contentAlignment = Alignment.Center
        ) {
            addHomeGraph(
                onSnackSelected = onSnackSelected,
                modifier = Modifier
                    .padding(padding)
                    .consumeWindowInsets(padding)
            )
        }
    }
}

val LocalNavAnimatedVisibilityScope = compositionLocalOf<AnimatedVisibilityScope?> { null }
val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> { null }

