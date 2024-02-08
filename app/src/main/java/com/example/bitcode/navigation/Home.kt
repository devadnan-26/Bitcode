package com.example.bitcode.navigation

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.example.bitcode.R
import com.example.bitcode.appui.dpToSp
import com.example.bitcode.ui.theme.arvo
import com.example.bitcode.ui.theme.arvo_bold
import com.example.bitcode.viewModels.PlatformViewModel
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.Reader
import java.io.StringWriter
import java.io.Writer
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale
import kotlin.math.ceil

@Composable
fun ItemObject(item: Item, modifier: Modifier, visible: Boolean) {
    Column(modifier = modifier) {
        val alpha by animateColorAsState(
            targetValue = if (visible) colorResource(R.color.app_green) else Color(
                0x75FFFFFF
            ), label = ""
        )
        val backgroundColor by animateColorAsState(
            targetValue = if (visible) Color.White else Color.Transparent, label = ""
        )
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clip(RoundedCornerShape(25.dp))
                .background(
                    backgroundColor
                )
        ) {
            Icon(
                painter = painterResource(id = item.drawable),
                contentDescription = null,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                tint = alpha
            )
        }
        Spacer(Modifier.padding(2.dp))
        AnimatedVisibility(
            visible = visible, modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = item.name,
                color = Color.White,
                fontSize = dpToSp(dp = 16.dp),
                fontFamily = arvo_bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun getDayOfWeek(dayOfMonth: Int): String {
    val calendar = Calendar.getInstance()

    // Set the day of the month
    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

    // Get the day of the week
    val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

    // Convert the day of the week to a string representation
    val daysOfWeek =
        arrayOf("", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
    return daysOfWeek[dayOfWeek]
}

@Composable
fun Home(context: ComponentActivity?, navController: NavController?) {
    BackHandler {
        context?.finish()
    }
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        val (progress, recent, calendar, toDo, statistics, missions) = createRefs()
        var showDatePicker by remember { mutableStateOf(false) }
        var calendarHeader by remember { mutableStateOf("January, 2024") }
        val currentDayOfMonth = SimpleDateFormat("d", Locale.getDefault()).format(Date())
        var currentDayOfWeek = SimpleDateFormat("EEEE", Locale.getDefault()).format(Date())
        currentDayOfWeek = currentDayOfWeek.substring(0, 3)
        Log.d("Calendar", currentDayOfWeek)
        val currentMonthAndYear =
            SimpleDateFormat("MMMM, yyyy", Locale.getDefault()).format(Date())
        var finalForm = "$currentDayOfWeek. $currentDayOfMonth $calendarHeader:"
        calendarHeader = currentMonthAndYear
        val viewModel = ViewModelProvider(context!!)[PlatformViewModel::class.java]
        viewModel.chosenDate.observe(context) {
            if (!it.isNullOrEmpty())
                finalForm = "${getDayOfWeek(it.toInt()).substring(0, 3)}. $it $calendarHeader:"
        }
        if (viewModel.numberOfTries.value!! < 1)
            viewModel.changeDate(currentDayOfMonth)
        Box(modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(20.dp))
            .defaultMinSize(minHeight = 150.dp)
            .constrainAs(statistics) {
                linkTo(recent.start, progress.end)
                top.linkTo(progress.bottom, margin = 16.dp)
                height = Dimension.preferredWrapContent
                width = Dimension.preferredWrapContent
            }
            .heightIn(0.dp, 150.dp)
            .alpha(0.90f)
            .background(Color(0xFF0053d5))) {
            Text(
                text = stringResource(id = R.string.recent_courses),
                fontFamily = arvo_bold,
                color = Color.White,
                fontSize = dpToSp(dp = 18.dp),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 12.dp, top = 8.dp)
            )
            Text(
                text = stringResource(id = R.string.android_course),
                fontFamily = arvo,
                color = Color.White,
                fontSize = dpToSp(dp = 18.dp),
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 12.dp, bottom = 24.dp)
            )
        }
        Box(modifier = Modifier
            .size(150.dp)
            .clip(RoundedCornerShape(20.dp))
            .constrainAs(progress) {
                top.linkTo(parent.top, margin = 18.dp)
                end.linkTo(parent.end, margin = 16.dp)
            }
            .background(colorResource(R.color.app_green))) {
            Text(
                text = stringResource(id = R.string.progress),
                fontFamily = arvo_bold,
                color = Color.White,
                fontSize = dpToSp(dp = 18.dp),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 8.dp, top = 8.dp)
            )
            CustomComponent(
                canvasSize = 115.dp,
                backgroundIndicatorStrokeWidth = 25f,
                foregroundIndicatorStrokeWidth = 25f,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 5.dp),
                indicatorValue = 25
            )
        }
        Box(modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(20.dp))
            .constrainAs(recent) {
                linkTo(parent.start, progress.start, startMargin = 16.dp, endMargin = 8.dp)
                linkTo(progress.top, progress.bottom)
                height = Dimension.preferredWrapContent
                width = Dimension.preferredWrapContent
            }
            .heightIn(0.dp, 150.dp)
            .alpha(0.90f)
            .paint(
                painter = painterResource(id = R.drawable.test_back),
                contentScale = ContentScale.Crop
            )) {
            Text(
                text = stringResource(id = R.string.recent_courses),
                fontFamily = arvo_bold,
                color = Color.White,
                fontSize = dpToSp(dp = 18.dp),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 12.dp, top = 8.dp)
            )
            Text(
                text = stringResource(id = R.string.android_course),
                fontFamily = arvo,
                color = Color.White,
                fontSize = dpToSp(dp = 18.dp),
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 12.dp, bottom = 24.dp)
            )
        }
        Box(modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .constrainAs(calendar) {
                top.linkTo(statistics.bottom, margin = 24.dp)
            }
            .clip(RoundedCornerShape(20.dp))
            .border(2.5.dp, Color.Black, shape = RoundedCornerShape(20.dp))
            .defaultMinSize(minHeight = 250.dp)) {
            Column(
                modifier = Modifier.paint(
                    painter = painterResource(id = R.drawable.background_supplies),
                    contentScale = ContentScale.Crop,
                    alpha = 0.3f
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(colorResource(R.color.app_green))
                ) {
                    Text(
                        text = calendarHeader,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontFamily = arvo_bold,
                        modifier = Modifier
                            .padding(vertical = 12.dp)
                            .align(Alignment.Center),
                        textAlign = TextAlign.Center,

                        )
                    Icon(
                        painter = painterResource(id = R.drawable.calendar),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 16.dp)
                            .clickable {
                                showDatePicker = true
                            }
                    )
                }
                Column {
                    val iYear = Calendar.YEAR
                    val iMonth = Calendar.MONTH
                    val iDay = 1

                    val mycal: Calendar = GregorianCalendar(iYear, iMonth, iDay)
                    val daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH) // 28
                    val numberOfRows = ceil((daysInMonth.toFloat() / 7))
                    (1..numberOfRows.toInt()).forEach { row ->
                        val datesList = mutableListOf<String>()
                        ((row - 1).times(7) + 1..row.times(7)).forEach { day ->
                            if (day <= 31)
                                datesList.add(day.toString())
                            else datesList.add("")
                        }
                        if (row == 5 || row == 1) {
                            CalendarRow(
                                dates = datesList.toList(),
                                modifier = Modifier.padding(
                                    bottom = if (row == 5) 8.dp else 0.dp,
                                    top = if (row == 5) 0.dp else 8.dp
                                ),
                                context = context
                            )
                        } else {
                            CalendarRow(
                                dates = datesList.toList(),
                                context = context
                            )
                        }
                    }
                }
            }
        }
        Text(
            text = finalForm,
            modifier = Modifier.constrainAs(toDo) {
                top.linkTo(calendar.bottom, margin = 24.dp)
                start.linkTo(calendar.start, margin = 16.dp)
            },
            fontSize = dpToSp(dp = 20.dp),
            fontFamily = arvo_bold,
            color = Color.Black
        )
        Column(
            modifier = Modifier
                .constrainAs(missions) {
                    start.linkTo(toDo.start, margin = 16.dp)
                    top.linkTo(toDo.bottom, margin = 16.dp)
                }
                .padding(bottom = 16.dp)
        ) {
            val missionList = mutableListOf<String>()
            val json: InputStream =
                LocalContext.current.resources.openRawResource(R.raw.todotest)
            val writer: Writer = StringWriter()
            val buffer = CharArray(1024)
            json.use { `is` ->
                val reader: Reader = BufferedReader(InputStreamReader(`is`, "UTF-8"))
                var n: Int
                while (reader.read(buffer).also { n = it } != -1) {
                    writer.write(buffer, 0, n)
                }
            }

            val jsonString: String = writer.toString()
            val jsonFile = JSONObject(jsonString)
            for (i in 1..jsonFile.length()) {
                missionList.add(jsonFile[i.toString()].toString())
            }
            missionList.forEach { mission ->
                Subject(mission = mission)
                Spacer(modifier = Modifier.padding(4.dp))
            }
        }
        if (showDatePicker) {
            MyDatePickerDialog(
                onDateSelected = { viewModel.changeDate(it) }
            ) { showDatePicker = false }
        }
    }
}

@Composable
@Preview
fun HomePreview() {
    Home(null, null)
}

fun convertMillisToDate(millis: Long, dateFormat: String = "d"): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = millis

    val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
    return sdf.format(calendar.time)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun MyDatePickerDialog(
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    val datePickerState = rememberDatePickerState(selectableDates = object : SelectableDates {

    })

    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""

    DatePickerDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(onClick = {
                onDateSelected(selectedDate)
                onDismiss()
            }

            ) {
                Text(text = "OK")
            }
        },
        dismissButton = {
            Button(onClick = {
                onDismiss()
            }) {
                Text(text = "Cancel")
            }
        }
    ) {
        DatePicker(
            state = datePickerState
        )
    }
}

@Composable
fun CustomComponent(
    modifier: Modifier = Modifier,
    canvasSize: Dp = 300.dp,
    indicatorValue: Int = 50,
    maxIndicatorValue: Int = 100,
    backgroundIndicatorColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
    backgroundIndicatorStrokeWidth: Float = 50f,
    foregroundIndicatorStrokeWidth: Float = 50f,
) {
    var allowedIndicatorValue by remember {
        mutableIntStateOf(maxIndicatorValue)
    }
    allowedIndicatorValue = if (indicatorValue <= maxIndicatorValue) {
        indicatorValue
    } else {
        maxIndicatorValue
    }
    var animatedIndicatorValue by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(key1 = allowedIndicatorValue) {
        animatedIndicatorValue = allowedIndicatorValue.toFloat()
    }

    val percentage = (animatedIndicatorValue / maxIndicatorValue) * 100

    val sweepAngle by animateFloatAsState(
        targetValue = (2.4 * percentage).toFloat(), animationSpec = tween(1000), label = ""
    )

    Column(
        modifier = modifier
            .size(canvasSize)
            .drawBehind {
                val componentSize = size / 1.25f
                foregroundIndicator(
                    sweepAngle = sweepAngle,
                    canvasSize = componentSize,
                    indicatorColor = Color.White,
                    indicatorStrokeWidth = foregroundIndicatorStrokeWidth,
                )
                backgroundIndicator(
                    canvasSize = componentSize,
                    indicatorColor = backgroundIndicatorColor,
                    indicatorStrokeWidth = backgroundIndicatorStrokeWidth,
                )
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmbeddedElements(indicatorValue = indicatorValue, canvasSize.value)
    }
}

fun DrawScope.foregroundIndicator(
    sweepAngle: Float, canvasSize: Size, indicatorColor: Color, indicatorStrokeWidth: Float
) {
    drawArc(
        size = size.times(0.75f),
        color = indicatorColor,
        startAngle = 180f,
        sweepAngle = sweepAngle,
        useCenter = false,
        style = Stroke(indicatorStrokeWidth, cap = StrokeCap.Round),
        topLeft = Offset(
            x = (size.width - canvasSize.width + 20) / 2f,
            y = (size.height - canvasSize.height + 20) / 2f
        )
    )
}

fun DrawScope.backgroundIndicator(
    canvasSize: Size, indicatorColor: Color, indicatorStrokeWidth: Float
) {
    drawArc(
        size = size.times(0.75f),
        color = indicatorColor,
        startAngle = 180f,
        sweepAngle = 360f,
        useCenter = false,
        style = Stroke(indicatorStrokeWidth, cap = StrokeCap.Round),
        topLeft = Offset(
            x = (size.width - canvasSize.width + 20) / 2f,
            y = (size.height - canvasSize.height + 20) / 2f
        )
    )
}

@Composable
fun CalendarRow(
    dates: List<String>,
    modifier: Modifier = Modifier,
    context: ComponentActivity
) {
    Row(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .fillMaxWidth()
            .padding(),
    ) {
        for (date in dates) {
            CalendarDate(
                date = date,
                modifier = Modifier.weight(1.0f),
                context = context,
            )
        }
    }
}

@Composable
fun CalendarDate(
    date: String,
    modifier: Modifier = Modifier,
    context: ComponentActivity

) {
    Box(
        modifier = modifier
            .padding(horizontal = 6.dp)
            .size(width = 40.dp, height = 40.dp)
            .clip(CircleShape),
        contentAlignment = Alignment.Center
    ) {
        val chosenDate = ViewModelProvider(context)[PlatformViewModel::class.java].chosenDate
        var isSelected by remember { mutableStateOf(false) }
        chosenDate.observe(context) {
            isSelected = date == it
            Log.d("Calendar", "Date is  $it")
        }
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(if (isSelected) colorResource(R.color.app_green) else Color.Transparent)
                .border(
                    if (isSelected) BorderStroke(1.5.dp, Color.Black) else BorderStroke(


                        0.dp,
                        Color.Transparent
                    ),
                    shape = CircleShape
                )
        ) {
            Text(
                text = date,
                color = Color.Black,
                fontFamily = arvo_bold,
                fontSize = dpToSp(dp = 18.dp),
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
fun EmbeddedElements(
    indicatorValue: Int, canvasSize: Float
) {
    Text(
        text = "$indicatorValue%",
        color = Color.White,
        fontSize = dpToSp(dp = (28 * canvasSize / 200).dp),
        fontFamily = arvo_bold,
        textAlign = TextAlign.Center,
    )
}

@Composable
fun Subject(mission: String) {
    Row {
        Box(
            Modifier
                .size(16.dp)
                .clip(CircleShape)
                .background(colorResource(R.color.app_green))
                .align(Alignment.CenterVertically)

        )
        Spacer(Modifier.padding(8.dp))
        Text(
            text = mission,
            color = Color.Black,
            fontFamily = arvo,
            fontSize = dpToSp(dp = 16.dp),
            textAlign = TextAlign.Start
        )
    }
}

@Composable
@Preview(showBackground = true)
fun CustomComponentPreview() {
    CustomComponent(canvasSize = 200.dp)
}

sealed class Item(val name: String, val drawable: Int) {
    object Home : Item("Home", R.drawable.home)
    object Courses : Item("Courses", R.drawable.category)
    object BitcodeX : Item(
        "BitcodeX", R.drawable.arrow
    ) // TODO: Arrow is just for testing and it will change soon

    object Profile : Item("Profile", R.drawable.account)
}