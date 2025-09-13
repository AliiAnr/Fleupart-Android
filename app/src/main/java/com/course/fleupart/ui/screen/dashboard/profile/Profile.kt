package com.course.fleupart.ui.screen.dashboard.profile

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.course.fleupart.R
import com.course.fleupart.data.model.remote.StoreAddressResponse
import com.course.fleupart.data.model.remote.StoreDetailData
import com.course.fleupart.data.model.remote.StoreDetailResponse
import com.course.fleupart.ui.common.ResultResponse
import com.course.fleupart.ui.components.AccountList
import com.course.fleupart.ui.components.CustomButton
import com.course.fleupart.ui.components.CustomTopAppBar
import com.course.fleupart.ui.components.FakeCategory
import com.course.fleupart.ui.screen.navigation.FleupartSurface
import com.course.fleupart.ui.theme.base40
import com.course.fleupart.ui.theme.primaryLight

@Composable
fun Profile(
    modifier: Modifier,
    onProfileDetailClick: (String) -> Unit,
    profileViewModel: ProfileViewModel,
    id: Long = 0L,
) {

    var showCircularProgress by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        profileViewModel.loadInitialData()
    }

    val storeDetailState by profileViewModel.storeDetailState.collectAsStateWithLifecycle(
        initialValue = ResultResponse.None
    )

    val storeAddressState by profileViewModel.storeAddressState.collectAsStateWithLifecycle(
        initialValue = ResultResponse.None
    )

    LaunchedEffect(storeDetailState, storeAddressState) {
        // Cek apakah masih ada yang loading
        val anyLoading = storeDetailState is ResultResponse.Loading ||
                storeAddressState is ResultResponse.Loading

        // Cek apakah keduanya sudah selesai (success atau error)
        val bothCompleted = (storeDetailState is ResultResponse.Success || storeDetailState is ResultResponse.Error) &&
                (storeAddressState is ResultResponse.Success || storeAddressState is ResultResponse.Error)

        showCircularProgress = anyLoading || !bothCompleted

        // Log untuk debugging
        when {
            storeDetailState is ResultResponse.Success && storeAddressState is ResultResponse.Success -> {
                Log.e("PROFILE", "Both APIs loaded successfully")
            }
            storeDetailState is ResultResponse.Error -> {
                Log.e("PROFILE", "Store detail error: ${(storeDetailState as ResultResponse.Error).error}")
            }
            storeAddressState is ResultResponse.Error -> {
                Log.e("PROFILE", "Store address error: ${(storeAddressState as ResultResponse.Error).error}")
            }
        }
    }

    val storeDetailData = when (storeDetailState) {
        is ResultResponse.Success -> (storeDetailState as ResultResponse.Success<StoreDetailResponse>).data.data
        else -> null
    }

    val storeAddressData = when (storeAddressState) {
        is ResultResponse.Success -> (storeAddressState as ResultResponse.Success<StoreAddressResponse>).data.data
        else -> null
    }

    Profile(
        modifier = modifier,
        onProfileDetailClick = onProfileDetailClick,
        storeDetailData = storeDetailData ?: StoreDetailData(),
        showCircularProgress = showCircularProgress
    )
}

@Composable
private fun Profile(
    modifier: Modifier = Modifier,
    onProfileDetailClick: (String) -> Unit,
    storeDetailData: StoreDetailData,
    showCircularProgress: Boolean
) {

    FleupartSurface(
        modifier = modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (showCircularProgress) {
                // Show empty state when there's no data and not loading
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {}
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    item {
                        CustomTopAppBar(
                            title = "Profile",
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(4.dp))
                        Header(
                            storeData = storeDetailData
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(12.dp))
                        AccountList(
                            data = FakeCategory.accountItem,
                            onProfileDetailClick = onProfileDetailClick
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(12.dp))
                        GeneralList(
                            data = FakeCategory.generalItem,
                            onProfileDetailClick = onProfileDetailClick
                        )
                    }
                }
            }
            if (showCircularProgress) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    CircularProgressIndicator(color = primaryLight)
                }
            }
        }

    }
}

@Composable
private fun Header(
    modifier: Modifier = Modifier,
    storeData: StoreDetailData
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            modifier = modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            if (storeData.picture.isNullOrEmpty()) {
//            Log.e("WOIII KOSONG", "${storeData?.picture}")
                Image(
                    painter = painterResource(id = R.drawable.placeholder),  // Use a placeholder image
                    contentDescription = "Store Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFC8A2C8))
                )
            } else {
                Log.e("WOIII ADA", "${storeData?.picture}")
                AsyncImage(
//                    model = ImageRequest.Builder(LocalContext.current)
//                        .data(storeData?.picture)
//                        .crossfade(true)
//                        .build(),
                    model = storeData?.picture,
                    contentDescription = "Store Image",
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.placeholder),
                    error = painterResource(id = R.drawable.placeholder),
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = storeData.name ?: "No Name",
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))
            CustomButton(
                text = "Visit Store",
                onClick = {

                },
                shape = RoundedCornerShape(15.dp),
                defaultWidth = 120.dp,
                defaultHeight = 40.dp,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
            )
        }
    }
}

@Composable
fun MenuItem(
    modifier: Modifier = Modifier,
    icon: Int,
    id: Long,
    title: String,
    onProfileDetailClick: (String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp)
                .clickable {
//                    if (title != "Language") {
                    onProfileDetailClick(title)
//                    }
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = "Navigate",
                tint = Color.Black,
                modifier = Modifier
                    .size(18.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )
            Icon(
                painter = painterResource(R.drawable.back_arrow),
                contentDescription = "Navigate",
                tint = Color.Black,
                modifier = Modifier
                    .size(16.dp)
            )
        }
        HorizontalDivider(color = base40, thickness = 1.dp)
    }
}

@Composable
private fun AccountList(
    modifier: Modifier = Modifier,
    data: List<AccountList>,
    onProfileDetailClick: (String) -> Unit,
) {
    Column(

    ) {
        SectionText(
            title = "My Account",
            fontWeight = FontWeight.W700,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        data.forEach {
            MenuItem(
                icon = it.icon,
                id = 0,
                title = it.name,
                onProfileDetailClick = onProfileDetailClick
            )
        }

    }
}

@Composable
private fun GeneralList(
    modifier: Modifier = Modifier,
    data: List<AccountList>,
    onProfileDetailClick: (String) -> Unit,
) {
    Column(

    ) {
        SectionText(
            title = "General",
            fontWeight = FontWeight.W700,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        data.forEach {
            MenuItem(
                icon = it.icon,
                id = 0,
                title = it.name,
                onProfileDetailClick = onProfileDetailClick,
            )
        }

    }
}

@Composable
fun SectionText(
    modifier: Modifier = Modifier,
    title: String,
    color: Color = Color.Black,
    fontSize: TextUnit = 18.sp,
    fontWeight: FontWeight = FontWeight.Bold,
) {
    Text(
        text = title,
        fontSize = fontSize,
        fontWeight = fontWeight,
        color = color,
        modifier = Modifier.padding(horizontal = 20.dp)
    )
}