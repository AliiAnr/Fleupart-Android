package com.course.fleupart.ui.screen.dashboard.detail.finance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.course.fleupart.R
import com.course.fleupart.ui.components.BankItem
import com.course.fleupart.ui.components.CustomTextField
import com.course.fleupart.ui.components.CustomTopAppBar
import com.course.fleupart.ui.components.FakeCategory
import com.course.fleupart.ui.components.WithdrawTextField
import com.course.fleupart.ui.screen.navigation.FleupartSurface
import com.course.fleupart.ui.theme.base20
import java.text.NumberFormat
import java.util.Locale

@Composable
fun WithdrawBalance(
    modifier: Modifier = Modifier
) {

    WithdrawBalance(
        id = 0
    )
}

@Composable
private fun WithdrawBalance(
    modifier: Modifier = Modifier,
    id: Int
) {

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
                    .background(base20),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CustomTopAppBar(
                    title = "Withdraw Balance",
                    showNavigationIcon = true
                )

                ListBankAccount(
                    bankList = FakeCategory.BankList
                )

            }
        }
    }
}

@Composable
private fun ListBankAccount(
    modifier: Modifier = Modifier,
    bankList: List<BankItem>
) {
    LazyColumn (
        modifier = Modifier.fillMaxWidth()
    ) {
        item {
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
        }

        items (
            items = bankList,
        ){
            BankAccountItem(
                item = it
            )
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                //tambahkan clickable
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.add_circle),
                        contentDescription = "Navigate",
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                            .size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Add another address",
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W600,
                    )
                }
            }
        }
    }
}


@Composable
fun BankAccountItem(
    modifier: Modifier = Modifier,
    item: BankItem
) {

    //tambahkan id dari address dan tambahkan clickable
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp)
    ) {
        HorizontalDivider(color = Color.LightGray, thickness = 1.dp)
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Icon(
                painter = painterResource(R.drawable.bank),
                contentDescription = "Navigate",
                tint = Color.Unspecified,
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.padding(vertical = 7.dp)
            ) {
                Text(
                    text = item.name,
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                )
                Text(
                    text = item.bankName,
                    color = Color.Black,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W600
                )
            }

        }
    }
}

