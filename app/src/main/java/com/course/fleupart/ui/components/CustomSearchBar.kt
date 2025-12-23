package com.course.fleupart.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.course.fleupart.R
import com.course.fleupart.ui.theme.base80
import com.course.fleupart.ui.theme.primaryLight
import kotlin.comparisons.then


@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    query: String,
    placeholder: String = "Search your desired flower",
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    focusRequester: FocusRequester? = null,
) {
    var textState by remember { mutableStateOf(query) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .border(1.dp, base80, RoundedCornerShape(10.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.search),
                contentDescription = "Notification",
                tint = base80
            )
            Spacer(modifier = Modifier.width(14.dp))
            BasicTextField(
                value = textState,
                onValueChange = {
                    textState = it
                    onQueryChange(it)
                },
                cursorBrush = SolidColor(primaryLight),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(fontSize = 16.sp, color = Color.Gray),
                modifier = Modifier
                    .weight(1f)
                    .then(
                        if (focusRequester != null) Modifier.focusRequester(focusRequester) else Modifier
                    )
                    .onKeyEvent {
                        if (it.nativeKeyEvent.keyCode.toLong() == Key.Enter.keyCode) {
                            onSearch(textState)
                            true
                        } else {
                            false
                        }
                    },
                decorationBox = { innerTextField ->
                    if (textState.isEmpty()) {
                        Text(
                            text = placeholder,
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                    }
                    innerTextField()
                }
            )
        }
    }
}

@Composable
fun SearchBarForNavigation(
    modifier: Modifier = Modifier,
    placeholder: String = "Search your desired flower",
    onNavigate: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .border(1.dp, base80, RoundedCornerShape(10.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .clickable(
                onClick = onNavigate,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.search),
                contentDescription = "Search",
                tint = base80
            )
            Spacer(modifier = Modifier.width(14.dp))
            Text(
                text = placeholder,
                fontSize = 16.sp,
                color = Color.Gray
            )
        }
    }
}