package com.course.fleura.ui.screen.common

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat

@Composable
fun ChangeStatusBarColor(
    newStatusBarColor: Color,
    isStatusBarIconsDark: Boolean
) {
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val window = (context as Activity).window
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)

        window.statusBarColor = newStatusBarColor.toArgb()
        windowInsetsController.isAppearanceLightStatusBars = isStatusBarIconsDark

        onDispose {

        }
    }
}


