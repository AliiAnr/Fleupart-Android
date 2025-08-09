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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.course.fleupart.data.resource.Resource
import com.course.fleupart.di.factory.OnBoardingViewModelFactory
import com.course.fleupart.di.factory.RegisterViewModelFactory
import com.course.fleupart.ui.components.FleupartBottomBar
import com.course.fleupart.ui.components.HomeSections
import com.course.fleupart.ui.screen.authentication.login.LoginScreen
import com.course.fleupart.ui.screen.authentication.onDataBoarding.AddressScreen
import com.course.fleupart.ui.screen.authentication.onDataBoarding.CitizenScreen
import com.course.fleupart.ui.screen.authentication.onDataBoarding.PhotoScreen
import com.course.fleupart.ui.screen.authentication.onDataBoarding.RegistrationPendingScreen
import com.course.fleupart.ui.screen.authentication.otp.OtpScreen
import com.course.fleupart.ui.screen.authentication.register.RegisterScreen
import com.course.fleupart.ui.screen.authentication.register.RegisterScreenViewModel
import com.course.fleupart.ui.screen.authentication.username.UsernameScreen
import com.course.fleupart.ui.screen.authentication.welcome.WelcomeScreen
import com.course.fleupart.ui.screen.dashboard.detail.finance.AddBankAccount
import com.course.fleupart.ui.screen.dashboard.detail.finance.BalanceValue
import com.course.fleupart.ui.screen.dashboard.detail.finance.SalesReport
import com.course.fleupart.ui.screen.dashboard.detail.finance.WithdrawBalance
import com.course.fleupart.ui.screen.dashboard.detail.home.DetailTest
import com.course.fleupart.ui.screen.dashboard.detail.product.AddProduct
import com.course.fleupart.ui.screen.dashboard.detail.product.DetailProduct
import com.course.fleupart.ui.screen.navigation.DetailDestinations
import com.course.fleupart.ui.screen.navigation.FleupartScaffold
import com.course.fleupart.ui.screen.navigation.MainDestinations
import com.course.fleupart.ui.screen.navigation.QueryKeys
import com.course.fleupart.ui.screen.navigation.addHomeGraph
import com.course.fleupart.ui.screen.navigation.composableWithCompositionLocal
import com.course.fleupart.ui.screen.navigation.nonSpatialExpressiveSpring
import com.course.fleupart.ui.screen.navigation.rememberFleupartNavController
import com.course.fleupart.ui.screen.navigation.rememberFleupartScaffoldState
import com.course.fleupart.ui.screen.navigation.spatialExpressiveSpring
import com.course.fleupart.ui.screen.onboarding.OnBoardingScreen
import com.course.fleupart.ui.screen.onboarding.OnBoardingViewModel
import com.course.fleupart.ui.theme.FleupartTheme

@Composable
fun FleupartApp() {

    val onBoardingViewModel: OnBoardingViewModel = viewModel(
            factory = OnBoardingViewModelFactory.getInstance(
                Resource.appContext
            )
    )

    val registerViewModel: RegisterScreenViewModel = viewModel(
        factory = RegisterViewModelFactory.getInstance(
            Resource.appContext
        )
    )

    FleupartTheme {
        val fleupartNavController = rememberFleupartNavController()
        SharedTransitionLayout {
            CompositionLocalProvider(
                LocalSharedTransitionScope provides this
            ) {
                NavHost(
                    navController = fleupartNavController.navController,
                    startDestination = MainDestinations.ONBOARDING_ROUTE,
                    contentAlignment = Alignment.Center
                ) {
                    composableWithCompositionLocal(
                        route = MainDestinations.WELCOME_ROUTE
                    ) { backStackEntry ->
                        WelcomeScreen(
                            navigateToRoute = fleupartNavController::navigateToNonBottomBarRoute
                        )
                    }

                    composableWithCompositionLocal(
                        route = MainDestinations.ONBOARDING_ROUTE
                    ) { backStackEntry ->
                        OnBoardingScreen(
                            navigateToRoute = fleupartNavController::navigateToNonBottomBarRoute,
                            setOnBoardingCompleted = onBoardingViewModel::setOnBoardingCompleted
                        )
                    }

                    composableWithCompositionLocal(
                        route = MainDestinations.LOGIN_ROUTE
                    ) { backStackEntry ->
                        LoginScreen(
                            navigateToRoute = fleupartNavController::navigateToNonBottomBarRoute,
                            onBackClick = fleupartNavController::upPress
                        )
                    }

                    composableWithCompositionLocal(
                        route = MainDestinations.REGISTER_ROUTE
                    ) { backStackEntry ->
                        RegisterScreen(
                            navigateToRoute = fleupartNavController::navigateToNonBottomBarRoute,
                            onBackClick = fleupartNavController::upPress,
                            registerViewModel = registerViewModel
                        )
                    }

                    composableWithCompositionLocal(
                        route = "${MainDestinations.OTP_ROUTE}?" + "email={${QueryKeys.EMAIL}}"
                    ) { backStackEntry ->
                        val arguments = requireNotNull(backStackEntry.arguments)
                        val email = arguments.getString(QueryKeys.EMAIL) ?: "Email tidak ditemukan"
                        OtpScreen(
                            email = email,
                            navigateToRoute = fleupartNavController::navigateToNonBottomBarRoute,
                            onBackClick = fleupartNavController::upPress
                        )
                    }

                    composableWithCompositionLocal(
                        route = DetailDestinations.PRODUCT_STATUS_ROUTE
                    ) { backStackEntry ->
                        DetailProduct()
                    }

                    composableWithCompositionLocal(
                        route = DetailDestinations.ADD_PRODUCT_ROUTE
                    ) { backStackEntry ->
                        AddProduct(

                        )
                    }

                    composableWithCompositionLocal(
                        route = DetailDestinations.WITHDRAW_BALANCE_ROUTE
                    ) { backStackEntry ->
                        WithdrawBalance(

                        )
                    }

                    composableWithCompositionLocal(
                        route = DetailDestinations.ADD_PRODUCT_ROUTE
                    ) { backStackEntry ->
                        AddBankAccount(

                        )
                    }

                    composableWithCompositionLocal(
                        route = DetailDestinations.BALANCE_AMOUNT_ROUTE
                    ) { backStackEntry ->
                        BalanceValue(

                        )
                    }

                    composableWithCompositionLocal(
                        route = DetailDestinations.SALES_REPORT_ROUTE
                    ) { backStackEntry ->
                        SalesReport(

                        )
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

                    onDataGraph(
                        onSnackSelected = fleupartNavController::navigateToSnackDetail
                    )

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
            startDestination = HomeSections.Order.route,
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

fun NavGraphBuilder.onDataGraph(
    onSnackSelected: (Long, String, NavBackStackEntry) -> Unit,
) {
    composableWithCompositionLocal(
        route = MainDestinations.CITIZEN_ROUTE
    ) { backStackEntry ->
        CitizenScreen(

        )
    }

    composableWithCompositionLocal(
        route = MainDestinations.PHOTO_ROUTE
    ) { backStackEntry ->
        PhotoScreen(

        )
    }

    composableWithCompositionLocal(
        route = MainDestinations.ADDRESS_ROUTE
    ) { backStackEntry ->
        AddressScreen(

        )
    }

    composableWithCompositionLocal(
        route = MainDestinations.REGISTER_PENDING_ROUTE
    ) { backStackEntry ->
        RegistrationPendingScreen(

        )
    }
}

val LocalNavAnimatedVisibilityScope = compositionLocalOf<AnimatedVisibilityScope?> { null }
val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> { null }

