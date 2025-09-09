package com.course.fleupart.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.course.fleupart.R
import com.course.fleupart.ui.theme.base300
import com.course.fleupart.ui.theme.base40
import com.course.fleupart.ui.theme.base60
import com.course.fleupart.ui.theme.base80
import com.course.fleupart.ui.theme.errorLight
import com.course.fleupart.ui.theme.primaryLight
import com.course.fleupart.ui.theme.tfbackground
import java.text.NumberFormat
import java.util.Locale
import kotlin.collections.plusAssign
import kotlin.comparisons.then
import kotlin.text.format

@Composable
fun CustomTextField(
    value: String,
    placeholder: String,
    onChange: (String) -> Unit,
    isError: Boolean,
    icon: ImageVector? = null,
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
                .background(
                    color = Color(0xFFF8F5F4),
                    shape = RoundedCornerShape(50.dp)
                ),
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
                textStyle = LocalTextStyle.current.copy(
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
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun WithdrawTextField(
    modifier: Modifier = Modifier,
    value: String,
    placeholder: String,
    imeAction: ImeAction = ImeAction.Next,
    keyboardType: KeyboardType = KeyboardType.NumberPassword,
    onChange: (String) -> Unit,
    leadingText: String? = null
) {
    var textFieldValue by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(text = formatNumber(value)))
    }

    Column(
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
    ) {
        Box(
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (leadingText != null) {
                    Text(
                        text = leadingText,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = primaryLight
                    )
                }

                BasicTextField(
                    value = textFieldValue,
                    onValueChange = { newValue ->
                        val cleanValue = newValue.text.replace(".", "").filter { it.isDigit() }
                        val formattedValue = formatNumber(cleanValue)

                        val newCursorPosition = calculateNewCursorPosition(
                            cleanValue,
                            formattedValue,
                            newValue.selection.start
                        )

                        textFieldValue = TextFieldValue(
                            text = formattedValue,
                            selection = TextRange(newCursorPosition)
                        )
                        onChange(cleanValue)
                    },
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(
                        color = primaryLight,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 24.sp,
                        textAlign = TextAlign.Start
                    ),
                    cursorBrush = SolidColor(primaryLight),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = keyboardType,
                        imeAction = imeAction
                    ),
                    decorationBox = { innerTextField ->
                        Box(
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (textFieldValue.text.isEmpty()) {
                                Text(
                                    text = placeholder,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFFF9C0DA),
                                    textAlign = TextAlign.Start
                                )
                            }
                            innerTextField()
                        }
                    }
                )

            }
        }
    }
}

fun formatNumber(amount: String): String {
    return amount.toLongOrNull()?.let {
        val formatter = NumberFormat.getInstance(Locale("in", "ID"))
        formatter.format(it)
    } ?: ""
}

fun calculateNewCursorPosition(cleanValue: String, formattedValue: String, oldCursor: Int): Int {
    val diff = formattedValue.length - cleanValue.length
    return (oldCursor + diff).coerceAtMost(formattedValue.length)
}

@Composable
fun CustomTextInput(
    value: String,
    placeholder: String,
    onChange: (String) -> Unit,
    isError: Boolean = false,
    errorMessage: String = "",
    imeAction: ImeAction = ImeAction.Next,
    keyboardType: KeyboardType = KeyboardType.Text,
    borderWidth: Dp = 1.dp,
    horizontalPadding: Dp = 20.dp,
    leadingIcon: Int? = null,
    leadingText: String? = null,
    isLongText: Boolean = false,
    borderColor: Color = base60,
    height: Dp = 50.dp,
    modifier: Modifier = Modifier,
) {

    Column(
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding),
    ) {
        Box(
            modifier = Modifier
                .height(height)
                .fillMaxWidth()
                .border(
                    width = borderWidth,
                    color = if (isError) errorLight else borderColor,
                    shape = RoundedCornerShape(10.dp)
                ),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                if (leadingIcon != null) {
                    Icon(
                        painter = painterResource(id = leadingIcon),
                        contentDescription = null,
                        tint = base300,
                        modifier = Modifier.size(24.dp),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                } else if (leadingText != null) {
                    Text(
                        text = leadingText,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = base80,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .then(
                            if (isLongText) {
                                Modifier.fillMaxSize()
                            } else {
                                Modifier
                            }
                        ),
                    contentAlignment = if (isLongText) Alignment.TopStart else Alignment.CenterStart
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = base80,
                            modifier = if (isLongText) Modifier.padding(top = 0.dp) else Modifier
                        )
                    }

                    BasicTextField(
                        value = value,
                        onValueChange = { newValue ->
                            if (isLongText || !newValue.contains("\n")) {
                                onChange(newValue)
                            }
                        },
                        singleLine = !isLongText,
                        textStyle = LocalTextStyle.current.copy(
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
                        maxLines = if (isLongText) Int.MAX_VALUE else 1,
                        modifier = if (isLongText) {
                            Modifier.fillMaxSize()
                        } else {
                            Modifier.fillMaxWidth()
                        }
                    )
                }
            }
        }

        if (isError) {
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.labelSmall,
                color = errorLight,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = horizontalPadding),
                textAlign = TextAlign.Start
            )
        } else {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
