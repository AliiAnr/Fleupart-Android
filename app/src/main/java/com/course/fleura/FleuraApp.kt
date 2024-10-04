package com.course.fleura

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.course.fleura.ui.theme.primaryLight
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp

@Composable
fun FleuraApp(
    context: MainActivity,
    modifier: Modifier = Modifier
) {
    Text(
        text = "Fleura App",
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(top = 50.dp)
    )
    BadComposable()
}

@Composable
fun BadComposable(
) {
    var count by remember {
        mutableStateOf(0)
    }

    Button(onClick = { count++ }, Modifier.wrapContentSize()) {
        Text("Recompose")
    }

    Text("$count"
    , modifier = Modifier.padding(top = 90.dp)
    )


}