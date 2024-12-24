@file:OptIn(
    ExperimentalSharedTransitionApi::class
)

package com.course.fleupart

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
import com.course.fleupart.ui.components.FleupartBottomBar
import com.course.fleupart.ui.components.HomeSections
import com.course.fleupart.ui.screen.authentication.login.LoginScreen
import com.course.fleupart.ui.screen.authentication.register.RegisterScreen
import com.course.fleupart.ui.screen.authentication.username.UsernameScreen
import com.course.fleupart.ui.screen.authentication.welcome.WelcomeScreen
import com.course.fleupart.ui.screen.dashboard.detail.home.DetailTest
import com.course.fleupart.ui.screen.navigation.FleupartScaffold
import com.course.fleupart.ui.screen.navigation.MainDestinations
import com.course.fleupart.ui.screen.navigation.addHomeGraph
import com.course.fleupart.ui.screen.navigation.composableWithCompositionLocal
import com.course.fleupart.ui.screen.navigation.nonSpatialExpressiveSpring
import com.course.fleupart.ui.screen.navigation.rememberFleupartNavController
import com.course.fleupart.ui.screen.navigation.rememberFleupartScaffoldState
import com.course.fleupart.ui.screen.navigation.spatialExpressiveSpring
import com.course.fleupart.ui.screen.onboarding.OnBoardingScreen
import com.course.fleupart.ui.theme.FleupartTheme

@Composable
fun FleupartApp() {
    FleupartTheme {
        val fleupartNavController = rememberFleupartNavController()
        SharedTransitionLayout {
            CompositionLocalProvider(
                LocalSharedTransitionScope provides this
            ) {
                NavHost(
                    navController = fleupartNavController.navController,
                    startDestination = MainDestinations.WELCOME_ROUTE,
                    contentAlignment = Alignment.Center
                ) {
                    composableWithCompositionLocal(
                        route = MainDestinations.WELCOME_ROUTE
                    ) { backStackEntry ->
                        WelcomeScreen()
                    }

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
                        route = MainDestinations.REGISTER_ROUTE
                    ) { backStackEntry ->
                        RegisterScreen()
                    }

                    composableWithCompositionLocal(
                        route = MainDestinations.USERNAME_ROUTE
                    ) { backStackEntry ->
                        UsernameScreen()
                    }

                    composableWithCompositionLocal(
                        route = MainDestinations.DASHBOARD_ROUTE
                    ) { backStackEntry ->
                        MainContainer(
                            onSnackSelected = fleupartNavController::navigateToSnackDetail
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
                            upPress = fleupartNavController::upPress
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
    val fleuraScaffoldState = rememberFleupartScaffoldState()
    val nestedNavController = rememberFleupartNavController()
    val navBackStackEntry by nestedNavController.navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val sharedTransitionScope = LocalSharedTransitionScope.current
        ?: throw IllegalStateException("No SharedElementScope found")
    val animatedVisibilityScope = LocalNavAnimatedVisibilityScope.current
        ?: throw IllegalStateException("No SharedElementScope found")
    FleupartScaffold(
        bottomBar = {
            with(animatedVisibilityScope) {
                with(sharedTransitionScope) {
                    FleupartBottomBar(
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

