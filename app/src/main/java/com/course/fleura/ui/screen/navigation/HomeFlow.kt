package com.course.fleura.ui.screen.navigation

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
import com.course.fleura.LocalNavAnimatedVisibilityScope
import com.course.fleura.ui.components.HomeSections
import com.course.fleura.ui.screen.dashboard.cart.Cart
import com.course.fleura.ui.screen.dashboard.home.Home
import com.course.fleura.ui.screen.dashboard.order.Order
import com.course.fleura.ui.screen.dashboard.point.Point
import com.course.fleura.ui.screen.dashboard.profile.Profile

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
    modifier: Modifier = Modifier
) {
    composable(HomeSections.Home.route) { from ->
        Home(
            onSnackClick = { id, origin -> onSnackSelected(id, origin, from) },
            modifier = modifier
        )
    }
    composable(HomeSections.Cart.route) { from ->
        Cart(

        )
    }
    composable(HomeSections.Point.route) { from ->
        Point(

        )
    }
    composable(HomeSections.Order.route) {
        Order(

        )
    }
    composable(HomeSections.Profile.route) {
        Profile(

        )
    }
}