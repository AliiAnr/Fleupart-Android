@file:OptIn(
    ExperimentalSharedTransitionApi::class
)

package com.course.fleupart

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.course.fleupart.data.resource.Resource
import com.course.fleupart.di.factory.LoginViewModelFactory
import com.course.fleupart.di.factory.OnBoardingViewModelFactory
import com.course.fleupart.di.factory.OnDataBoardingViewModelFactory
import com.course.fleupart.di.factory.RegisterViewModelFactory
import com.course.fleupart.ui.components.FleupartBottomBar
import com.course.fleupart.ui.components.HomeSections
import com.course.fleupart.ui.screen.authentication.login.LoginScreen
import com.course.fleupart.ui.screen.authentication.login.LoginScreenViewModel
import com.course.fleupart.ui.screen.authentication.onDataBoarding.AddressScreen
import com.course.fleupart.ui.screen.authentication.onDataBoarding.CitizenScreen
import com.course.fleupart.ui.screen.authentication.onDataBoarding.OnDataBoardingViewModel
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
import kotlinx.coroutines.flow.first

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

    val loginViewModel: LoginScreenViewModel = viewModel(
        factory = LoginViewModelFactory.getInstance(
            Resource.appContext
        )
    )

    val onDataBoardingViewModel: OnDataBoardingViewModel = viewModel(
        factory = OnDataBoardingViewModelFactory.getInstance(
            Resource.appContext
        )
    )

    val navigationViewModel: NavigationDestinationViewModel = viewModel(
        factory = StartupNavigationViewModelFactory(
            onBoardingViewModel,
            loginViewModel
        )
    )

    val destination by navigationViewModel.startDestination.collectAsStateWithLifecycle(
        initialValue = MainDestinations.ONBOARDING_ROUTE
    )

    LaunchedEffect(key1 = Unit) {
        val isLoggedIn = loginViewModel.isUserLoggedIn().first()
        if (isLoggedIn) {
            loginViewModel.saveToken()
        }
    }

    FleupartTheme {
        val fleupartNavController = rememberFleupartNavController()
        SharedTransitionLayout {
            CompositionLocalProvider(
                LocalSharedTransitionScope provides this
            ) {
                NavHost(
                    navController = fleupartNavController.navController,
                    startDestination = destination,
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
                            onBackClick = fleupartNavController::upPress,
                            loginViewModel = loginViewModel,
                            onDataBoardingViewModel = onDataBoardingViewModel
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
                        route = DetailDestinations.ADD_PRODUCT_ROUTE,
                        enterTransition = {
                            // Muncul dari kanan
                            slideInHorizontally(
                                initialOffsetX = { it },
                                animationSpec = tween(durationMillis = 350)
                            )
                        },
                        exitTransition = {
                            // Keluar ke kanan
                            slideOutHorizontally(
                                targetOffsetX = { it },
                                animationSpec = tween(durationMillis = 350)
                            )
                        },
                        popEnterTransition = {
                            // Jika balik (back), bisa juga dari kiri ke posisi normal
                            slideInHorizontally(
                                initialOffsetX = { it },
                                animationSpec = tween(durationMillis = 350)
                            )
                        },
                        popExitTransition = {
                            // Jika back, keluar ke kanan
                            slideOutHorizontally(
                                targetOffsetX = { it },
                                animationSpec = tween(durationMillis = 350)
                            )
                        }
                    ) { backStackEntry ->
                        AddProduct(
                            onBackClick = fleupartNavController::upPress
                        )
                    }

                    composableWithCompositionLocal(
                        route = DetailDestinations.WITHDRAW_BALANCE_ROUTE
                    ) { backStackEntry ->
                        WithdrawBalance(

                        )
                    }

                    composableWithCompositionLocal(
                        route = DetailDestinations.ADD_BANK_ACCOUNT_ROUTE
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
                            onSnackSelected = fleupartNavController::navigateToSnackDetail,
                            onProductDetail = fleupartNavController::navigateToProductDetail
                        )
                    }

                    //MARK: Personalize Screen
                    composableWithCompositionLocal(
                        route = MainDestinations.CITIZEN_ROUTE
                    ) { backStackEntry ->
                        CitizenScreen(
                            onDataBoardingViewModel = onDataBoardingViewModel,
                            navigateToRoute = fleupartNavController::navigateToNonBottomBarRoute
                        )
                    }

                    composableWithCompositionLocal(
                        route = MainDestinations.PHOTO_ROUTE
                    ) { backStackEntry ->
                        PhotoScreen(
                            onDataBoardingViewModel = onDataBoardingViewModel,
                            navigateToRoute = fleupartNavController::navigateToNonBottomBarRoute,
                        )
                    }

                    composableWithCompositionLocal(
                        route = MainDestinations.ADDRESS_ROUTE
                    ) { backStackEntry ->
                        AddressScreen(
                            navigateToRoute = fleupartNavController::navigateToNonBottomBarRoute,
                            onDataBoardingViewModel = onDataBoardingViewModel,
                            loginViewModel = loginViewModel
                        )
                    }

                    composableWithCompositionLocal(
                        route = MainDestinations.REGISTER_PENDING_ROUTE
                    ) { backStackEntry ->
                        RegistrationPendingScreen(

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
    onSnackSelected: (Long, String, NavBackStackEntry) -> Unit,
    onProductDetail: (String, NavBackStackEntry) -> Unit,
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
                onProductDetail = onProductDetail,
                modifier = Modifier
                    .padding(padding)
                    .consumeWindowInsets(padding)
            )
        }
    }
}

val LocalNavAnimatedVisibilityScope = compositionLocalOf<AnimatedVisibilityScope?> { null }
val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> { null }

