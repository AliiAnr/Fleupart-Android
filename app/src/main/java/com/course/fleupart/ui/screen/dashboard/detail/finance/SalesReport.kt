package com.course.fleupart.ui.screen.dashboard.detail.finance

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.PlotType
import co.yml.charts.common.model.Point
import co.yml.charts.ui.barchart.BarChart
import co.yml.charts.ui.barchart.models.BarChartData
import co.yml.charts.ui.barchart.models.BarData
import co.yml.charts.ui.barchart.models.BarStyle
import co.yml.charts.ui.barchart.models.SelectionHighlightData
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.LineType
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import co.yml.charts.ui.piechart.charts.DonutPieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import com.course.fleupart.R
import com.course.fleupart.ui.components.CustomTopAppBar
import com.course.fleupart.ui.screen.navigation.FleupartSurface
import com.course.fleupart.ui.theme.base20
import com.course.fleupart.ui.theme.base60
import com.course.fleupart.ui.theme.primaryLight
import kotlin.text.toInt
import kotlin.times

data class StatsRangeData(
    val salesPerOrder: String,
    val sales: String,
    val orders: String,
    val totalVisitor: String
)

@Composable
fun SalesReport(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit
) {
    SalesReport(
        id = 0,
        onBackClick = onBackClick
    )
}

@Composable
private fun SalesReport(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    id: Int
) {
    val focusManager = LocalFocusManager.current

    val scrollState = rememberScrollState()

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
                    .background(base20)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CustomTopAppBar(
                    title = "Balance Value",
                    showNavigationIcon = true,
                    onBackClick = onBackClick
                )
                Spacer(modifier = Modifier.height(8.dp))
                ScrollableTabRowDemo()
                Spacer(modifier = Modifier.height(8.dp))
                SalesPerformanceSection()
                Spacer(modifier = Modifier.height(8.dp))
                CustomBarChart()
                Spacer(modifier = Modifier.height(8.dp))
                RevenueLineChart()
                Spacer(modifier = Modifier.height(8.dp))
                OrderStatusPieChart() // Tambahkan ini
                Spacer(modifier = Modifier.height(8.dp))
                CustomerDemographicChart() // <-- Tambahkan ini
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun ScrollableTabRowDemo() {
    val tabs = listOf("24 Hours Ago", "7 Days Ago", "30 Days Ago", "All Time")
    val statsByRange = listOf(
        StatsRangeData("125K", "10.5M", "84", "1.2K"),
        StatsRangeData("132K", "68.9M", "522", "7.4K"),
        StatsRangeData("118K", "284M", "2,410", "32K"),
        StatsRangeData("120K", "1.2B", "10,840", "150K")
    )
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
                            .background(if (selectedTabIndex == index) primaryLight else Color.Transparent)
                            .border(1.dp, if (selectedTabIndex == index) primaryLight else base60, RoundedCornerShape(50))
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
        StatsSection(statsByRange[selectedTabIndex])
    }
}
@Composable
fun StatsSection(stats: StatsRangeData) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                StatCard("Sales per Order", stats.salesPerOrder)
            }
            Box(modifier = Modifier.weight(1f)) {
                StatCard("Sales (Rp)", stats.sales)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                StatCard("Order", stats.orders)
            }
            Box(modifier = Modifier.weight(1f)) {
                StatCard("Total Visitor", stats.totalVisitor)
            }
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
                iconColor = Color(0xFFFFD700),
                value = "$rating/5.0",
                description = "Overall Product Ratings"
            )

            PerformanceItem(
                icon = R.drawable.bag_2,
                iconColor = Color(0xFFFFD700),
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
            color = Color(0xFFE9ECF1) // Warna default (abu-abu)
        )
    }

    val xAxisData = AxisData.Builder()
        .backgroundColor(Color.White)
        .axisStepSize(25.dp)
        .steps(barData.size - 1)
        .bottomPadding(8.dp)
        .startDrawPadding(30.dp)
        .labelData { index -> daysOfWeek[index] }
        .axisLabelFontSize(12.sp)
        .build()

    val maxRange = productView.maxOrNull()?.plus(50) ?: 2000
    val yStepSize = 4

    val yAxisData = AxisData.Builder()
        .backgroundColor(Color.White)
        .steps(yStepSize)
        .labelAndAxisLinePadding(25.dp)
        .axisOffset(15.dp)
        .labelData { index -> ((index * (maxRange / yStepSize)).toInt()).toString() }
        .axisLabelFontSize(12.sp)
        .build()

    val barChartData = BarChartData(
        chartData = barData,
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        backgroundColor = Color.Transparent,
        barStyle = BarStyle(
            paddingBetweenBars = 12.dp,
            barWidth = 30.dp,
            selectionHighlightData = SelectionHighlightData(

                highlightBarColor = primaryLight,

                highlightTextBackgroundColor = Color.Transparent,

                highlightTextColor = Color.Black,

                popUpLabel = { _, y -> "View: ${y.toInt()}" }
            )
        ),
        showYAxis = true,
        showXAxis = true,
        paddingTop = 0.dp,
        paddingEnd = 0.dp
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(20.dp)
    ) {
        Text(
            text = "Product Views (Weekly)",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp),
            contentAlignment = Alignment.Center
        ) {
            BarChart(
                modifier = Modifier
                    .height(290.dp)
                    .width(360.dp)
                    .background(Color.White)
                    .padding(top = 5.dp),
                barChartData = barChartData,
            )
        }
    }
}

@Composable
fun RevenueLineChart() {
    // Dummy Data: Revenue for past 6 months (in Millions)
    val months = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun")
    val revenueData = listOf(
        Point(0f, 12.5f),
        Point(1f, 15.0f),
        Point(2f, 11.2f),
        Point(3f, 18.5f),
        Point(4f, 22.0f),
        Point(5f, 20.5f)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(20.dp)
    ) {
        Text(
            text = "Income Trend (Last 6 Months)",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        ) {

            val steps = revenueData.size - 1
            val dynamicStepSize = (maxWidth - 70.dp) / steps

            val xAxisData = AxisData.Builder()
                .axisStepSize(dynamicStepSize) // Menggunakan lebar dinamis
                .backgroundColor(Color.White)
                .steps(steps)
                .labelData { index -> months.getOrElse(index) { "" } }
                .labelAndAxisLinePadding(15.dp)
                .axisLabelFontSize(12.sp)
                .build()

            val yAxisData = AxisData.Builder()
                .steps(5)
                .backgroundColor(Color.White)
                .labelAndAxisLinePadding(20.dp)
                .labelData { index ->
                    val value = index * 5
                    "${value}M"
                }
                .axisLabelFontSize(12.sp)
                .build()

            val lineChartData = LineChartData(
                linePlotData = LinePlotData(
                    lines = listOf(
                        Line(
                            dataPoints = revenueData,
                            lineStyle = LineStyle(
                                color = primaryLight,
                                lineType = LineType.SmoothCurve()
                            ),
                            intersectionPoint = IntersectionPoint(color = primaryLight),
                            selectionHighlightPoint = SelectionHighlightPoint(color = primaryLight),
                            shadowUnderLine = ShadowUnderLine(
                                alpha = 0.5f,
                                brush = Brush.verticalGradient(
                                    colors = listOf(primaryLight, Color.White)
                                )
                            )
                        )
                    )
                ),
                xAxisData = xAxisData,
                yAxisData = yAxisData,
                gridLines = GridLines(color = base20),
                backgroundColor = Color.White,
                paddingRight = 0.dp,
                containerPaddingEnd = 0.dp
            )

            LineChart(
                modifier = Modifier.fillMaxSize(),
                lineChartData = lineChartData
            )
        }

    }
}
@Composable
fun OrderStatusPieChart() {
    val completedCount = 28
    val pendingCount = 9
    val cancelledCount = 2

    val totalCount = completedCount + pendingCount + cancelledCount

    val completedColor = Color(0xFF4CAF50)
    val pendingColor = Color(0xFFFFC107)
    val cancelledColor = Color(0xFFF44336)

    val pieChartData = PieChartData(
        slices = listOf(
            PieChartData.Slice("Completed", completedCount.toFloat(), completedColor),
            PieChartData.Slice("Pending", pendingCount.toFloat(), pendingColor),
            PieChartData.Slice("Cancelled", cancelledCount.toFloat(), cancelledColor)
        ),
        plotType = PlotType.Donut
    )

    val pieChartConfig = PieChartConfig(
        isAnimationEnable = true,
        showSliceLabels = false,
        animationDuration = 1500,
        strokeWidth = 50f,
        activeSliceAlpha = 0.9f,
        isClickOnSliceEnabled = true
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(20.dp)
    ) {
        Text(
            text = "Order Status",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier.size(160.dp),
                contentAlignment = Alignment.Center
            ) {
                DonutPieChart(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(10.dp),
                    pieChartData = pieChartData,
                    pieChartConfig = pieChartConfig
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Total Order",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "$totalCount", // Menampilkan total angka
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(start = 24.dp)
            ) {
                val completedPercent = (completedCount.toFloat() / totalCount * 100).toInt()
                val pendingPercent = (pendingCount.toFloat() / totalCount * 100).toInt()
                val cancelledPercent = (cancelledCount.toFloat() / totalCount * 100).toInt()

                LegendItem(
                    color = completedColor,
                    label = "Completed: $completedCount ($completedPercent%)"
                )
                LegendItem(
                    color = pendingColor,
                    label = "Pending: $pendingCount ($pendingPercent%)"
                )
                LegendItem(
                    color = cancelledColor,
                    label = "Cancelled: $cancelledCount ($cancelledPercent%)"
                )
            }
        }
    }
}

@Composable
fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, RoundedCornerShape(4.dp))
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Black
        )
    }
}

@Composable
fun CustomerDemographicChart() {
    val newCustomers = 150
    val returningCustomers = 380
    val totalCustomers = newCustomers + returningCustomers

    val newColor = Color(0xFF29B6F6)
    val returningColor = Color(0xFF7E57C2)

    val pieChartData = PieChartData(
        slices = listOf(
            PieChartData.Slice("New", newCustomers.toFloat(), newColor),
            PieChartData.Slice("Returning", returningCustomers.toFloat(), returningColor)
        ),
        plotType = PlotType.Donut
    )

    val pieChartConfig = PieChartConfig(
        isAnimationEnable = true,
        showSliceLabels = false,
        animationDuration = 1500,
        strokeWidth = 50f,
        activeSliceAlpha = 0.9f,
        isClickOnSliceEnabled = true
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(20.dp)
    ) {
        Text(
            text = "Customer Composition",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier.size(160.dp),
                contentAlignment = Alignment.Center
            ) {
                DonutPieChart(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(10.dp),
                    pieChartData = pieChartData,
                    pieChartConfig = pieChartConfig
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Total Visitors",
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "$totalCustomers",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(start = 24.dp)
            ) {
                val newPercent = (newCustomers.toFloat() / totalCustomers * 100).toInt()
                val retPercent = (returningCustomers.toFloat() / totalCustomers * 100).toInt()

                LegendItem(
                    color = newColor,
                    label = "New: $newCustomers ($newPercent%)"
                )
                LegendItem(
                    color = returningColor,
                    label = "Returning: $returningCustomers ($retPercent%)"
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if(retPercent > 50) "High Loyalty Rate!" else "Great User Acquisition!",
                    fontSize = 12.sp,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    color = primaryLight
                )
            }
        }
    }
}
