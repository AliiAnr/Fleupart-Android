package com.course.fleupart.ui.screen.dashboard.detail.finance

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.course.fleupart.ui.components.CustomButton
import com.course.fleupart.ui.components.CustomTextField
import com.course.fleupart.ui.components.CustomTextInput
import com.course.fleupart.ui.components.CustomTopAppBar
import com.course.fleupart.ui.screen.dashboard.detail.product.DetailProduct
import com.course.fleupart.ui.screen.navigation.FleupartSurface
import com.course.fleupart.ui.theme.base20

@Composable
fun AddBankAccount(
    modifier: Modifier = Modifier
) {

    AddBankAccount(
        modifier = modifier,
        id = 0
    )
}

@Composable
private fun AddBankAccount(
    modifier: Modifier = Modifier,
    id: Int = 0
) {

    var bankNameValue by remember { mutableStateOf("") }
    var numberValue by remember { mutableStateOf("") }
    var isButtonEnabled by remember { mutableStateOf(false) }

    LaunchedEffect(bankNameValue, numberValue) {
        isButtonEnabled = bankNameValue.isNotEmpty() && numberValue.isNotEmpty()
    }

    val focusManager = LocalFocusManager.current

    FleupartSurface(
        modifier = modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .background(base20)
                    .clickable(
                        onClick = {
                            focusManager.clearFocus()
                        },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CustomTopAppBar(
                    title = "Add Bank Account",
                    showNavigationIcon = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                InputSection(
                    bankNameValue = bankNameValue,
                    bankNameOnChange = { bankNameValue = it },
                    numberValue = numberValue,
                    numberOnChange = { numberValue = it }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CustomButton(
                        text = "Add Bank Account",
                        onClick = { },
                        isAvailable = isButtonEnabled
                    )
                }
            }
        }
    }
}

@Composable
private fun InputSection(
    modifier: Modifier = Modifier,
    isBankNameError: Boolean = false,
    bankNameErrorMessage: String = "",
    bankNameValue: String = "",
    bankNameOnChange: (String) -> Unit = {},
    isNumberError: Boolean = false,
    numberValue: String = "",
    numberErrorMessage: String = "",
    numberOnChange: (String) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Bank Name",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        CustomTextInput(
            value = bankNameValue,
            placeholder = "",
            onChange = bankNameOnChange,
            isError = isBankNameError,
            errorMessage = bankNameErrorMessage,
            horizontalPadding = 0.dp,
            borderColor = Color.Black
        )

        Text(
            text = "Enter Number",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        CustomTextInput(
            value = numberValue,
            placeholder = "",
            onChange = numberOnChange,
            isError = isNumberError,
            errorMessage = numberErrorMessage,
            horizontalPadding = 0.dp,
            borderColor = Color.Black
        )
    }
}