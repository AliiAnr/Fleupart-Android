package com.course.fleupart.ui.screen.dashboard.detail.finance

import android.graphics.Paint
import android.graphics.Typeface
import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.axis.AxisData
import co.yml.charts.axis.DataCategoryOptions
import co.yml.charts.common.model.AccessibilityConfig
import co.yml.charts.common.model.Point
import co.yml.charts.common.utils.DataUtils
import co.yml.charts.ui.barchart.BarChart
import co.yml.charts.ui.barchart.models.BarChartData
import co.yml.charts.ui.barchart.models.BarChartType
import co.yml.charts.ui.barchart.models.BarData
import co.yml.charts.ui.barchart.models.BarStyle
import co.yml.charts.ui.barchart.models.SelectionHighlightData
import co.yml.charts.ui.barchart.models.drawBarGraph
import com.course.fleupart.R
import com.course.fleupart.ui.components.CustomTopAppBar
import com.course.fleupart.ui.screen.navigation.FleupartSurface
import com.course.fleupart.ui.theme.base20
import com.course.fleupart.ui.theme.base60
import com.course.fleupart.ui.theme.primaryLight

@Composable
fun SalesReport(
    modifier: Modifier = Modifier
) {

    SalesReport(
        id = 0
    )
}

@Composable
private fun SalesReport(
    modifier: Modifier = Modifier,
    id: Int
) {

    val focusManager = LocalFocusManager.current

    FleupartSurface(
        modifier = modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    focusManager.clearFocus()
                },
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
                    title = "Balance Value",
                    showNavigationIcon = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                ScrollableTabRowDemo()
                Spacer(modifier = Modifier.height(8.dp))
                SalesPerformanceSection()
                Spacer(modifier = Modifier.height(8.dp))

//                DailyProductsViewChart(
//                    productViews = productViews,
//                    daysOfWeek = daysOfWeek,
//                    highlightIndex = 4 // Highlights Friday
//                )

//                BarchartWithSolidBars()

                val productViews = listOf(1000f, 2000f, 800f, 1200f, 1400f, 1800f, 600f)
                val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                val highlightIndex = 4 // Highlighting "Friday"

//                CustomBarChart(
//                    productViews = productViews,
//                    daysOfWeek = daysOfWeek,
//                    highlightIndex = highlightIndex
//                )

                CustomBarChart()


            }
        }
    }
}

@Composable
fun ScrollableTabRowDemo() {
    val tabs = listOf("24 Hours Ago", "7 Days Ago", "30 Days Ago", "All Time")
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 18.dp)
    ) {
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            edgePadding = 20.dp,
            indicator = {},
            divider = {},
            containerColor = Color.Transparent
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    modifier = Modifier.padding(end = if (index == tabs.lastIndex) 0.dp else 8.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .width(130.dp)
                            .clip(RoundedCornerShape(50))
                            .background(
                                if (selectedTabIndex == index) primaryLight else Color.Transparent
                            )
                            .border(
                                width = 1.dp,
                                color = if (selectedTabIndex == index) primaryLight else base60,
                                shape = RoundedCornerShape(50)
                            )
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = title,
                            color = if (selectedTabIndex == index) Color.White else primaryLight,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(14.dp))
        when (selectedTabIndex) {
            0 -> StatsSection("Last 24 Hours")
            1 -> StatsSection("Last 7 Days")
            2 -> StatsSection("Last 30 Days")
            3 -> StatsSection("All Time")
        }
    }
}

@Composable
fun StatsSection(timeRange: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item { StatCard("Sales per Order", "0") }
            item { StatCard("Sales (Rp)", "0") }
            item { StatCard("Order", "0") }
            item { StatCard("Total Visitor", "0") }
        }
    }
}

@Composable
fun StatCard(title: String, value: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .border(1.dp, primaryLight, RoundedCornerShape(8.dp))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        }
    }
}

@Composable
fun SalesPerformanceSection(
    rating: Double = 4.3,
    totalSales: Int = 80
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 18.dp)
    ) {
        Text(
            text = "Sales Performance",
            fontSize = 20.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            PerformanceItem(
                icon = R.drawable.star,
                iconColor = Color(0xFFFFD700), // Gold color
                value = "$rating/5.0",
                description = "Overall Product Ratings"
            )

            PerformanceItem(
                icon = R.drawable.bag_2,
                iconColor = Color(0xFFFFD700), // Same gold color for consistency
                value = totalSales.toString(),
                description = "Overall Product Sales"
            )
        }
    }
}

@Composable
fun PerformanceItem(
    icon: Int,
    iconColor: Color,
    value: String,
    description: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(end = 16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "Icon",
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        Text(
            text = description,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            color = Color.Black,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun CustomBarChart() {
    val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    val productView = listOf(999, 19, 1332, 666, 1332, 1665, 333)

    val barData = productView.mapIndexed { index, value ->
        BarData(
            point = Point(x = index.toFloat(), y = value.toFloat()),
            label = daysOfWeek[index],
            color = Color(0xFFE9ECF1)
        )
    }

    val xAxisData = AxisData.Builder()
        .backgroundColor(Color.White)
        .axisStepSize(25.dp)  // Smaller step size
        .steps(barData.size - 1)
        .bottomPadding(8.dp)  // Reduced padding
        .startDrawPadding(30.dp)  // Reduced padding
        .labelData { index -> daysOfWeek[index] }
        .axisLabelFontSize(12.sp)  // Smaller font size
        .build()

    val maxRange = productView.maxOrNull()?.plus(50) ?: 2000
    val yStepSize = 4

    val yAxisData = AxisData.Builder()
        .backgroundColor(Color.White)
        .steps(yStepSize)
        .labelAndAxisLinePadding(25.dp)  // Reduced padding
        .axisOffset(15.dp)  // Reduced offset
        .labelData { index -> ((index * (maxRange / yStepSize)).toInt()).toString() }
        .axisLabelFontSize(12.sp)  // Smaller font size
        .build()

    val barChartData = BarChartData(
        chartData = barData,
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        backgroundColor = Color.Transparent,
        barStyle = BarStyle(
            paddingBetweenBars = 12.dp,  // Reduced padding
            barWidth = 30.dp,  // Smaller bars
            selectionHighlightData = SelectionHighlightData(
                popUpLabel = { _, y -> "View: ${y.toInt()}" },
                highlightBarColor = primaryLight,
                highlightTextBackgroundColor = Color.Transparent,
            )
        ),
        showYAxis = true,
        showXAxis = true,
        paddingTop = 0.dp,
        paddingEnd = 0.dp// Added top padding
    )

    // Center the chart with fixed dimensions
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp)
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        BarChart(
            modifier = Modifier
                .height(290.dp)  // Fixed height
                .width(360.dp)
                .background(Color.White)
                .padding(top = 5.dp),  // Fixed width
            barChartData = barChartData,
        )
    }
}






