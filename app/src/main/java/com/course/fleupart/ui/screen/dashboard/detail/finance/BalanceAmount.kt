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
import com.course.fleupart.ui.components.BankItem
import com.course.fleupart.ui.components.CustomButton
import com.course.fleupart.ui.components.CustomTopAppBar
import com.course.fleupart.ui.components.FakeCategory
import com.course.fleupart.ui.components.WithdrawTextField
import com.course.fleupart.ui.screen.navigation.FleupartSurface
import com.course.fleupart.ui.theme.base20

@Composable
fun BalanceValue(
    modifier: Modifier = Modifier
) {

    BalanceValue(
        id = 0
    )
}
@Composable
private fun BalanceValue(
    modifier: Modifier = Modifier,
    id: Int
) {
    var amount by remember { mutableStateOf("0") }
    var isButtonEnabled by remember { mutableStateOf(false) }

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
                    title = "Balance Value",
                    showNavigationIcon = true
                )

                BalanceValueItem(
                    item = FakeCategory.BankList[0]
                )

                InputValue(
                    value = amount,
                    onValueChange = {
                        amount = it
                        isButtonEnabled = (it.toIntOrNull() ?: 0) >= 50000
                    }
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CustomButton(
                        text = "Save",
                        onClick = { },
                        isAvailable = isButtonEnabled
                    )
                }
            }
        }
    }
}
@Composable
private fun BalanceValueItem(
    modifier: Modifier = Modifier,
    item: BankItem
) {
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = "Bank Account",
        color = Color.Black,
        fontSize = 14.sp,
        fontWeight = FontWeight.W700,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 8.dp)
    )
    BankAccountItem(
        item = item
    )
}

@Composable
private fun InputValue(
    modifier: Modifier = Modifier,
    value : String,
    onValueChange: (String) -> Unit
) {

    Column(
        modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth().background(Color.White).padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Enter the amount you want to withdraw",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        WithdrawTextField(
            value = value,
            onChange = onValueChange,
            leadingText = "Rp ",
            placeholder = "0"
        )
    }
}