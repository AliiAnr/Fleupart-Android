package com.course.fleupart.ui.screen.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.course.fleupart.LocalNavAnimatedVisibilityScope
import com.course.fleupart.ui.components.HomeSections
import com.course.fleupart.ui.screen.dashboard.finance.Finance
import com.course.fleupart.ui.screen.dashboard.home.Home
import com.course.fleupart.ui.screen.dashboard.order.Order
import com.course.fleupart.ui.screen.dashboard.order.OrderViewModel
import com.course.fleupart.ui.screen.dashboard.product.Product
import com.course.fleupart.ui.screen.dashboard.profile.Profile
import com.course.fleupart.ui.screen.dashboard.profile.ProfileViewModel

fun <T> spatialExpressiveSpring() = spring<T>(
    dampingRatio = 0.8f,
    stiffness = 380f
)

fun <T> nonSpatialExpressiveSpring() = spring<T>(
    dampingRatio = 1f,
    stiffness = 1600f
)

fun NavGraphBuilder.composableWithCompositionLocal(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    enterTransition: (
    @JvmSuppressWildcards
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?
    )? = {
        fadeIn(nonSpatialExpressiveSpring())
    },
    exitTransition: (
    @JvmSuppressWildcards
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?
    )? = {
        fadeOut(nonSpatialExpressiveSpring())
    },
    popEnterTransition: (
    @JvmSuppressWildcards
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?
    )? =
        enterTransition,
    popExitTransition: (
    @JvmSuppressWildcards
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?
    )? =
        exitTransition,
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route,
        arguments,
        deepLinks,
        enterTransition,
        exitTransition,
        popEnterTransition,
        popExitTransition
    ) {
        CompositionLocalProvider(
            LocalNavAnimatedVisibilityScope provides this@composable
        ) {
            content(it)
        }
    }
}


fun NavGraphBuilder.addHomeGraph(
    onSnackSelected: (Long, String, NavBackStackEntry) -> Unit,
    onProductDetail: (String, NavBackStackEntry) -> Unit,
    onProfileDetail: (String, NavBackStackEntry) -> Unit,
    onCompletedOrderDetail: (NavBackStackEntry) -> Unit,
    onOrderDetail: (NavBackStackEntry) -> Unit,
    orderViewModel: OrderViewModel,
    profileViewModel: ProfileViewModel,
    modifier: Modifier = Modifier
) {
    composable(HomeSections.Home.route) { from ->
        Home(
            onSnackClick = { id, origin -> onSnackSelected(id, origin, from) },
            modifier = modifier
        )
    }
    composable(HomeSections.Product.route) { from ->
        Product(
            modifier = modifier,
            onProductDetail = { to ->
                onProductDetail(to, from)
            }
        )
    }
    composable(HomeSections.Finance.route) { from ->
        Finance(
            modifier = modifier,
            orderViewModel = orderViewModel,
            onCompletedOrderDetail = {
                onCompletedOrderDetail(from)
            }
        )
    }
    composable(HomeSections.Order.route) { from ->
        Order(
            modifier = modifier,
            orderViewModel = orderViewModel,
            onOrderDetail = {
                onOrderDetail(from)
            }
        )
    }
    composable(HomeSections.Profile.route) { from ->
        Profile(
            modifier = modifier,
            onProfileDetailClick = {  to ->
                onProfileDetail(to, from)
            },
            profileViewModel = profileViewModel,
            id = 1
        )
    }
}