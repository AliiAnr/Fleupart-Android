package com.course.fleura

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import com.course.fleura.ui.screen.common.ChangeStatusBarColor
import com.course.fleura.ui.theme.*
import com.course.fleura.ui.theme.FleuraTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.light(Color.White.toArgb(), Color.White.toArgb()),
        )
        setContent {
            ChangeStatusBarColor(
                newStatusBarColor = Color.Transparent,
                isStatusBarIconsDark = true
            )
            FleuraTheme(dynamicColor = false) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .navigationBarsPadding(),
                    color = onPrimaryLight
                ) {
                    FleuraApp(context = this, modifier = Modifier)
                }
            }
        }
    }
}
