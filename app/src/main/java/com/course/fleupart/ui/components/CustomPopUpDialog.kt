package com.course.fleupart.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.course.fleupart.R
import com.course.fleupart.ui.theme.primaryLight


@Composable
fun CustomPopUpDialog(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    isShowIcon: Boolean = true,
    isShowTitle: Boolean = true,
    isShowDescription: Boolean = true,
    isShowButton: Boolean = true,
    icon: @Composable (() -> Unit)? = null,      // Composable icon/image
    title: String = "",
    description: String = "",
    buttons: @Composable (() -> Unit)? = null,   // Composable untuk button custom
) {
    // Dialog overlay full screen
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.3f))
            .clickable(enabled = false) { }, // biar nggak close kalau klik luar dialog
        contentAlignment = Alignment.Center
    ) {
        // Popup card
        Box(
            modifier = modifier
                .widthIn(min = 280.dp, max = 360.dp)
                .wrapContentHeight()
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(18.dp)
                )
                .padding(vertical = 20.dp, horizontal = 20.dp)
        ) {
            // X icon absolute
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(32.dp)
                    .clickable { onDismiss() }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.xmark), // ganti dengan xmark mu
                    contentDescription = "Close",
                    tint = primaryLight, // pink
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.TopEnd)
                )
            }
            // Main content
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 12.dp)
            ) {
                if (isShowIcon && icon != null) {
                    icon()
                    Spacer(Modifier.height(20.dp))
                }
                if (isShowTitle && title.isNotBlank()) {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                }
                if (isShowDescription && description.isNotBlank()) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = description,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.widthIn(max = 250.dp)
                    )
                }
                if (isShowButton && buttons != null) {
                    Spacer(Modifier.height(28.dp))
                    buttons()
                }
            }
        }
    }
}
