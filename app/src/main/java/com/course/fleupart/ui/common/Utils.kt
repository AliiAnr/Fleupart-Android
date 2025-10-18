package com.course.fleupart.ui.common

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import co.yml.charts.common.extensions.isNotNull
import com.course.fleupart.data.model.remote.StoreAddress
import com.course.fleupart.ui.components.getUCropOptions
import com.course.fleupart.ui.theme.primaryLight
import com.yalantis.ucrop.UCrop
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.io.File
import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale
import kotlin.text.get

@Composable
fun NumberPicker(
    range: IntRange,
    initialValue: Int,
    modifier: Modifier = Modifier,
    onValueChange: (Int) -> Unit
) {

    val listState =
        rememberLazyListState(initialFirstVisibleItemIndex = range.indexOf(initialValue))

    val selectedValue by remember {
        derivedStateOf {
            val center = listState.layoutInfo.viewportEndOffset / 2
            val closestItem = listState.layoutInfo.visibleItemsInfo.minByOrNull {
                val itemCenter = it.offset + it.size / 2
                kotlin.math.abs(itemCenter - center)
            }
            closestItem?.let {
                range.toList().getOrNull(it.index) ?: initialValue
            } ?: initialValue
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .width(100.dp)
            .height(120.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(2.dp, primaryLight, RoundedCornerShape(16.dp))
                .background(Color(0x33F06AA8))
                .align(Alignment.Center)
        )

        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(vertical = 40.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            flingBehavior = rememberSnapFlingBehavior(listState),
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(range.toList().size) { index ->
                val value = range.toList()[index]
                val isSelected = selectedValue == value

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(40.dp)
                        .clip(RoundedCornerShape(16.dp))
                ) {
                    Text(
                        text = value.toString(),
                        fontSize = if (isSelected) 24.sp else 18.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) Color.Black else Color(0xFFC6C5C5)
                    )
                }
            }
        }
    }

    LaunchedEffect(selectedValue) {
        onValueChange(selectedValue)
    }
}



@Composable
fun ChangeStatusBarColor(
    newStatusBarColor: Color,
    isStatusBarIconsDark: Boolean
) {
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val window = (context as Activity).window
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)

        window.statusBarColor = newStatusBarColor.toArgb()
        windowInsetsController.isAppearanceLightStatusBars = isStatusBarIconsDark

        onDispose {

        }
    }
}

fun StoreAddress.toFormattedAddress(): String {
    val addressParts = mutableListOf<String>()

    if (!road.isNullOrBlank()) {
        addressParts.add(road)
    }

    if (!detail.isNullOrBlank()) {
        addressParts.add(detail)
    }

    if (!district.isNullOrBlank()) {
        addressParts.add("Kec. $district")
    }

    if (!city.isNullOrBlank()) {
        addressParts.add(city)
    }

    if (!province.isNullOrBlank()) {
        addressParts.add(province)
    }

    if (!postcode.isNullOrBlank()) {
        addressParts.add(postcode)
    }

    return addressParts.joinToString(", ")
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomBottomSheet(
    showSheet: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    icon: Int? = null,
    title: String,
    description: String,
    positiveButtonText: String,
    negativeButtonText: String,
    onPositiveClick: () -> Unit
) {
    if (showSheet) {
        val sheetState = rememberModalBottomSheetState()

        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            containerColor = Color.White,
            tonalElevation = 16.dp,
            dragHandle = null
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .height(370.dp)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                icon?.let {
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = "Dialog Icon",
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .size(170.dp)
                            .padding(bottom = 16.dp, top = 20.dp)
                    )
                }

                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = description,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(50.dp),
                        border = BorderStroke(1.dp, primaryLight),
                        modifier = Modifier
                            .width(115.dp)
                    ) {
                        Text(text = negativeButtonText, color = primaryLight)
                    }

                    Button(
                        onClick = onPositiveClick,
                        shape = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = primaryLight),
                        modifier = Modifier
                            .width(115.dp)
                    ) {
                        Text(text = positiveButtonText, color = Color.White)
                    }
                }
            }
        }
    }
}


fun formatCurrency(amount: Long): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
    return formatter.format(amount).replace(",00", "")
}

fun convertMinutesToHours(minutes: Int): String {
    val hours = minutes / 60
    val remainingMinutes = minutes % 60
    return when {
        hours > 0 && remainingMinutes > 0 -> "$hours hour(s) $remainingMinutes minute(s)"
        hours > 0 -> "$hours hour(s)"
        else -> "$remainingMinutes minute(s)"
    }
}

fun cleanupTempFiles(context: Context) {
    try {
        val cacheDir = context.cacheDir
        val tempFiles = cacheDir.listFiles()?.filter { file ->
            file.name.startsWith("temp_image_") ||
                    file.name.startsWith("cropped_camera_image_") ||
                    file.name.startsWith("cropped_gallery_image_") ||
                    file.name.startsWith("cropped_image_")
        }

        tempFiles?.forEach { file ->
            if (file.exists()) {
                val deleted = file.delete()
                // Optional: Add logging
                 Log.d("ImagePicker", "Deleted temp file: ${file.name}, success: $deleted")
            }
        }
    } catch (e: Exception) {
        // Optional: Add error logging
         Log.e("ImagePicker", "Error cleaning up temp files", e)
    }
}

 fun startCropping(
     context: Context,
     sourceUri: Uri,
     cropLauncher: ActivityResultLauncher<Intent>
) {
    val destinationUri = Uri.fromFile(
        File(
            context.cacheDir,
            "cropped_image_${System.currentTimeMillis()}.jpg"
        )
    )
    val uCrop = UCrop.of(sourceUri, destinationUri)
        .withAspectRatio(1f, 1f)
        .withMaxResultSize(1000, 1000)
        .withOptions(getUCropOptions(context))
    cropLauncher.launch(uCrop.getIntent(context))
}

fun extractOrderId(input: String): String {
    val digits = input.filter { it.isDigit() }
    return digits.toList().sorted().take(4).joinToString("")
}

fun parseDateTime(dateTimeString: String): Pair<kotlinx.datetime.LocalDate, kotlinx.datetime.LocalTime> {
    val instant = Instant.parse(dateTimeString)
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    return localDateTime.date to localDateTime.time
}

fun formatCurrencyFromString(amount: String): String {
    return try {
        val amountDouble = amount.toDoubleOrNull() ?: 0.0
        val amountLong = amountDouble.toLong()

        val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
        formatter.format(amountLong).replace(",00", "")
    } catch (e: Exception) {
        "Rp0"
    }
}


fun Modifier.loadingFx(): Modifier = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }

    val transition = rememberInfiniteTransition(label = "")

    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
        ), label = ""
    )

    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0xFFE0E0E0),
                Color(0xFFB9B2B2),
                Color(0xFFE0E0E0),
            ),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    )
        .onGloballyPositioned {
            size = it.size
        }
}

