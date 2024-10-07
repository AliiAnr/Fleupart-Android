package com.course.fleura.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.course.fleura.R
import com.course.fleura.ui.theme.base40
import com.course.fleura.ui.theme.base80
import com.course.fleura.ui.theme.errorLight
import com.course.fleura.ui.theme.onPrimaryLight
import com.course.fleura.ui.theme.primaryLight
import com.course.fleura.ui.theme.tfbackground

@Composable
fun CustomTextField(
    value: String,
    placeholder: String,
    onChange: (String) -> Unit,
    isError: Boolean,
    icon: ImageVector,
    errorMessage: String,
    isPassword: Boolean = false,
    imeAction: ImeAction = ImeAction.Next,
    keyboardType: KeyboardType = KeyboardType.Text,
    borderWidth: Dp = 1.dp,
    modifier: Modifier = Modifier,
) {
    var showPassword by rememberSaveable { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.Start,
        modifier = modifier.fillMaxWidth(),
    ) {
        Box(
            modifier = Modifier
                .height(65.dp)
                .fillMaxWidth()
                .border(
                    width = borderWidth,
                    color = if (isError) errorLight else base40,
                    shape = RoundedCornerShape(50.dp)
                )
                .background(tfbackground, RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.CenterStart
        ) {
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = base80,
                    modifier = Modifier.padding(start = 35.dp)
                )
            }

            BasicTextField(
                value = value,
                onValueChange = {
                    if (!it.contains("\n"))
                        onChange(it)
                },
                singleLine = true,
                textStyle = TextStyle(
                    color = Color.Black,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Start
                ),
                cursorBrush = SolidColor(primaryLight),
                keyboardOptions = KeyboardOptions(
                    keyboardType = keyboardType,
                    imeAction = imeAction
                ),
                visualTransformation = if (isPassword && !showPassword) {
                    PasswordVisualTransformation()
                } else {
                    VisualTransformation.None
                },
                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            innerTextField()
                        }
                        if (isPassword) {
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clickable(
                                        onClick = { showPassword = !showPassword },
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = if (showPassword) painterResource(id = R.drawable.eye_open) else painterResource(
                                        id = R.drawable.eye_close
                                    ),
                                    contentDescription = if (showPassword) "Show Password" else "Hide Password",
                                    tint = base40,
                                    modifier = Modifier
                                        .size(24.dp)
                                )
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp)
            )
        }

        if (isError) {
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.labelSmall,
                color = errorLight,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                textAlign = TextAlign.Start
            )
        } else {
            Spacer(modifier = Modifier.height(16.dp)) // Menambahkan spacer tetap saat tidak ada error
        }
    }
}