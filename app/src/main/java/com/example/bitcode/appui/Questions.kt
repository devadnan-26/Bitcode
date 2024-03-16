package com.example.bitcode.appui

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat.getString
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.RenderMode
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.bitcode.R
import com.example.bitcode.TinyDB
import com.example.bitcode.ui.theme.BitcodeTheme
import com.example.bitcode.ui.theme.arvo
import com.example.bitcode.viewModels.PlatformViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject

class Questions : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BitcodeTheme {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    val controller = window.insetsController

                    // Hide both the status bar and the navigation bar
                    controller?.let {
                        it.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                        it.systemBarsBehavior =
                            WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                    }
                } else {
                    WindowCompat.setDecorFitsSystemWindows(window, false)
                    window.setFlags(
                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                    )
                }
                val questionOneOne = listOf(
                    stringResource(id = R.string.answer_1_1_1),
                    stringResource(id = R.string.answer_1_1_2),
                    stringResource(id = R.string.answer_1_1_3)
                )
                val questionOneTwo = listOf(
                    stringResource(id = R.string.answer_1_2_1),
                    stringResource(id = R.string.answer_1_2_2),
                    stringResource(id = R.string.answer_1_2_3)
                )
                val questionOneThree = listOf(
                    stringResource(id = R.string.answer_1_3_1),
                    stringResource(id = R.string.answer_1_3_2),
                    stringResource(id = R.string.answer_1_3_3)
                )
                val questionTwoOne = listOf(
                    stringResource(id = R.string.answer_2_1_1),
                    stringResource(id = R.string.answer_2_1_2),
                    stringResource(id = R.string.answer_2_1_3)
                )
                val questionTwoTwo = listOf(
                    stringResource(id = R.string.answer_2_2_1),
                    stringResource(id = R.string.answer_2_2_2),
                    stringResource(id = R.string.answer_2_2_3)
                )
                val questionThreeOne = listOf(
                    stringResource(id = R.string.answer_3_1_1),
                    stringResource(id = R.string.answer_3_1_2),
                    stringResource(id = R.string.answer_3_1_3)
                )
                val questionThreeTwo = listOf(
                    stringResource(id = R.string.answer_3_2_1),
                    stringResource(id = R.string.answer_3_2_2),
                    stringResource(id = R.string.answer_3_2_3)
                )
                val questionFourOne = listOf(
                    stringResource(id = R.string.answer_4_1_1),
                    stringResource(id = R.string.answer_4_1_2),
                    stringResource(id = R.string.answer_4_1_3)
                )
                val questionFourTwo = listOf(
                    stringResource(id = R.string.answer_4_2_1),
                    stringResource(id = R.string.answer_4_2_2),
                    stringResource(id = R.string.answer_4_2_3)
                )
                val questionFiveOne = listOf(
                    stringResource(id = R.string.answer_5_1_1),
                    stringResource(id = R.string.answer_5_1_2),
                    stringResource(id = R.string.answer_5_1_3)
                )
                val questionFiveTwo = listOf(
                    stringResource(id = R.string.answer_5_2_1),
                    stringResource(id = R.string.answer_5_2_2),
                    stringResource(id = R.string.answer_5_2_3)
                )
                val questionSixOne = listOf(
                    stringResource(id = R.string.answer_6_1_1),
                    stringResource(id = R.string.answer_6_1_2),
                    stringResource(id = R.string.answer_6_1_3)
                )
                val questionSixTwo = listOf(
                    stringResource(id = R.string.answer_6_2_1),
                    stringResource(id = R.string.answer_6_2_2),
                    stringResource(id = R.string.answer_6_2_3)
                )
                val questionsList = listOf(
                    null,
                    Choices(stringResource(id = R.string.question_1_1), questionOneOne),
                    Choices(stringResource(id = R.string.question_1_2), questionOneTwo),
                    Choices(stringResource(id = R.string.question_1_3), questionOneThree),
                    null,
                    Choices(stringResource(id = R.string.question_2_1), questionTwoOne),
                    Choices(stringResource(id = R.string.question_2_2), questionTwoTwo),
                    null,
                    Choices(stringResource(id = R.string.question_3_1), questionThreeOne),
                    Choices(stringResource(id = R.string.question_3_2), questionThreeTwo),
                    null,
                    Choices(stringResource(id = R.string.question_4_1), questionFourOne),
                    Choices(stringResource(id = R.string.question_4_2), questionFourTwo),
                    null,
                    Choices(stringResource(id = R.string.question_5_1), questionFiveOne),
                    Choices(stringResource(id = R.string.question_5_2), questionFiveTwo),
                    null,
                    Choices(stringResource(id = R.string.question_6_1), questionSixOne),
                    Choices(stringResource(id = R.string.question_6_2), questionSixTwo),
                    null
                )
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "start") {
                    composable("start") { Start(navController = navController) }
                    composable("questions") { Question(questionsList, this@Questions) }
                }
            }
        }
    }
}

@Composable
fun Start(navController: NavController?) {
    Scaffold {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            val (startMessage, button, icon, quiz) = createRefs()
            Row(modifier = Modifier.constrainAs(icon) {
                centerHorizontallyTo(parent)
                top.linkTo(parent.top, margin = 16.dp)
            }) {
                Image(
                    painter = painterResource(id = R.drawable.bitcode_icon),
                    contentDescription = null,
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .size(72.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Image(
                    painter = painterResource(id = R.drawable.bitcode),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .size(115.09.dp, 23.35.dp)
                )
            }
            val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.get_started))

            LottieAnimation(
                modifier = Modifier
                    .size(350.dp)
                    .constrainAs(quiz) {
                        centerHorizontallyTo(parent)
                        centerVerticallyTo(parent, 0.35f)
                    },
                composition = composition,
                iterations = 1,
                renderMode = RenderMode.HARDWARE
            )
            Text(
                text = stringResource(id = R.string.start_message),
                fontFamily = FontFamily(Font(R.font.arvo_bold)),
                fontSize = 24.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
                lineHeight = 48.sp,
                modifier = Modifier.constrainAs(startMessage) {
                    centerHorizontallyTo(parent)
                    bottom.linkTo(button.top, margin = 48.dp)
                }
            )
            Button(
                onClick = {
                    navController?.navigate("questions")
                }, shape = RoundedCornerShape(50.dp), colors = ButtonDefaults.buttonColors(
                    colorResource(R.color.app_green)
                ), modifier = Modifier
                    .constrainAs(button) {
                        centerHorizontallyTo(parent)
                        bottom.linkTo(parent.bottom, margin = 36.dp)
                    }
                    .padding(horizontal = 24.dp)
                    .wrapContentWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.get_started),
                    fontFamily = FontFamily(Font(R.font.arvo_bold)),
                    fontSize = dpToSp(dp = 18.dp),
                    color = Color.White,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun Question(
    list: List<Choices?>?, context: ComponentActivity?
) {
    val viewModel = context?.let { ViewModelProvider(it) }?.get(PlatformViewModel::class.java)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) {
        ConstraintLayout(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            val json = JSONObject()
            var letsGoButtonText by remember {
                mutableStateOf(
                    context?.let { it1 ->
                        getString(
                            it1,
                            R.string.lets_go
                        )
                    }
                )
            }
            val (question, fieldTitle, answer, buttonLetsGo, icon, buttonSubmit, ok, okMessage) = createRefs()
            val fields = listOf(
                stringResource(id = R.string.field1),
                stringResource(id = R.string.field2),
                stringResource(id = R.string.field3),
                stringResource(id = R.string.field4),
                stringResource(id = R.string.field5),
                stringResource(id = R.string.field6),
                ""
            )
            var fieldsIterator by remember { mutableIntStateOf(0) }
            var i by remember { mutableIntStateOf(0) }
            val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.ok_animation))
            var showAnimation by remember { mutableStateOf(false) }
            var showOkText by remember { mutableStateOf(false) }
            AnimatedVisibility(visible = showOkText, modifier = Modifier.constrainAs(okMessage) {
                centerVerticallyTo(parent, bias = 0.75f)
                centerHorizontallyTo(parent)
            }) {
                Text(
                    text = stringResource(id = R.string.ok_message),
                    fontSize = 22.sp,
                    fontFamily = FontFamily(Font(R.font.arvo_bold)),
                    color = Color.Black,
                    lineHeight = 36.sp,
                    textAlign = TextAlign.Center
                )
            }
            AnimatedVisibility(visible = showAnimation, modifier = Modifier.constrainAs(ok) {
                centerHorizontallyTo(parent)
                centerVerticallyTo(parent, bias = 0.35f)
            }) {
                LottieAnimation(
                    modifier = Modifier
                        .padding(72.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    composition = composition,
                    iterations = 1,
                )
            }
            Row(modifier = Modifier.constrainAs(icon) {
                centerHorizontallyTo(parent)
                top.linkTo(parent.top, margin = 16.dp)
            }) {
                Image(
                    painter = painterResource(id = R.drawable.bitcode_icon),
                    contentDescription = null,
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .size(72.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Image(
                    painter = painterResource(id = R.drawable.bitcode),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .size(115.09.dp, 23.35.dp)
                )
            }
            AnimatedVisibility(visible = (list?.get(i) == null),
                modifier = Modifier
                    .wrapContentSize()
                    .constrainAs(fieldTitle) {
                        centerVerticallyTo(parent, bias = 0.5f)
                        centerHorizontallyTo(parent)
                    }) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                color = Color.Black,
                                fontFamily = FontFamily(Font(R.font.arvo_bold)),
                                fontSize = 36.sp
                            )
                        ) {
                            append(fields[fieldsIterator])
                        }
                    }, lineHeight = 60.sp, textAlign = TextAlign.Center
                )
            }
            AnimatedVisibility(visible = (list?.get(i) != null),
                modifier = Modifier
                    .wrapContentSize()
                    .heightIn(0.dp, 220.dp)
                    .constrainAs(answer) {
                        bottom.linkTo(buttonSubmit.top, margin = 48.dp)
                        centerHorizontallyTo(parent)
                    }
                    .verticalScroll(rememberScrollState())) {
                Column(modifier = Modifier.wrapContentSize()) {
                    val letters = listOf("A", "B", "C")
                    var iterator = 0
                    list?.get(i)?.answers?.forEach { answer ->
                        if (viewModel != null) {
                            Choice(
                                choice = answer,
                                modifier = Modifier,
                                letter = letters[iterator],
                                viewModel,
                                context
                            )
                        }
                        if (iterator < 2) iterator++
                    }
                }
            }
            AnimatedVisibility(visible = (list?.get(i) != null),
                modifier = Modifier.constrainAs(buttonSubmit) {
                    centerHorizontallyTo(parent)
                    bottom.linkTo(parent.bottom, margin = 36.dp)
                }) {
                Button(
                    onClick = {

                        val jsonObject = JSONObject()
                        val query = list?.get(i)?.question
                        val chosenAnswer = viewModel?.whichChosen()
                            ?.let { it1 -> list?.get(i)?.answers?.get(it1) }
                        if (chosenAnswer == null) {
                            scope.launch {
                                snackbarHostState.showSnackbar("Snackbar")
                            }
                            return@Button
                        }
                        if (i < 19) i++
                        jsonObject.put("question", query)
                        jsonObject.put("answer", chosenAnswer)
                        Log.d("JSON", "Object $jsonObject")
                        viewModel.put(jsonObject)
                        Log.d("JSON", "Array ${viewModel.array.value}")
                        viewModel.reset()
                        if (i == 19) {
                            showAnimation = true; showOkText = true
                        }
                    }, shape = RoundedCornerShape(50.dp), colors = ButtonDefaults.buttonColors(
                        colorResource(R.color.app_green)
                    ), modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .wrapContentWidth()
                ) {
                    Text(
                        text = stringResource(id = R.string.submit),
                        fontFamily = FontFamily(Font(R.font.arvo_bold)),
                        fontSize = dpToSp(dp = 18.dp),
                        color = Color.White,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
            AnimatedVisibility(visible = if (fieldsIterator != 6) (list?.get(i) == null) else i == 19,
                modifier = Modifier.constrainAs(buttonLetsGo) {
                    centerHorizontallyTo(parent)
                    bottom.linkTo(parent.bottom, margin = 36.dp)
                }) {
                Button(
                    onClick = {
                        if (fieldsIterator < 6) fieldsIterator++; if (i < 19) i++
                        if (fieldsIterator == 6) {
                            if (letsGoButtonText != context?.let { it1 -> getString(it1, R.string.get_started) })
                                letsGoButtonText = context?.let { it1 -> getString(it1, R.string.get_started) }
                            else {
                                if (context != null) {
                                    TinyDB(context).putObject("JSON-Answers", json)
                                }
                                context?.getSharedPreferences("Bitcode", Context.MODE_PRIVATE)?.edit()
                                    ?.putBoolean("questions_shown", true)?.apply()
                                context?.startActivity(Intent(context, Platform::class.java))
                                context?.finish()
                            }

                        } else {
                            if (fieldsIterator != 0) {
                                json.put(fields[(fieldsIterator - 1)], viewModel?.array?.value)
                                Log.d("JSON", json.toString())
                                viewModel?.resetArray()
                            }
                        }
                    }, shape = RoundedCornerShape(50.dp), colors = ButtonDefaults.buttonColors(
                        colorResource(R.color.app_green)
                    ), modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .wrapContentWidth()
                ) {
                    letsGoButtonText?.let { it1 ->
                        Text(
                            text = it1,
                            fontFamily = FontFamily(Font(R.font.arvo_bold)),
                            fontSize = dpToSp(dp = 18.dp),
                            color = Color.White,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
            AnimatedVisibility(visible = (list?.get(i) != null),
                modifier = Modifier
                    .wrapContentSize()
                    .constrainAs(question) {
                        linkTo(icon.bottom, answer.top)
                        centerHorizontallyTo(parent)
                    }) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                color = Color.Black,
                                fontFamily = FontFamily(Font(R.font.arvo_bold)),
                                fontSize = 28.sp
                            )
                        ) {
                            append(list?.get(i)?.question)
                        }
                    },
                    textAlign = TextAlign.Center,
                    lineHeight = 40.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun Choice(
    choice: String?,
    modifier: Modifier,
    letter: String,
    viewModel: PlatformViewModel,
    context: ComponentActivity?
) {
    var clicked by remember { mutableStateOf(false) }
    val backgroundColor = if (clicked) Color(88, 188, 101) else Color.White
    val isChosen = when (letter) {
        "A" -> viewModel.aChosen
        "B" -> viewModel.bChosen
        else -> viewModel.cChosen
    }
    isChosen.observe(context!!) { clicked = it }
    Box(modifier = modifier
        .padding(horizontal = 24.dp)
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(vertical = 8.dp)
        .clip(RoundedCornerShape(12.dp))
        .border(3.dp, Color.Black, RoundedCornerShape(15.dp))
        .background(color = backgroundColor)
        .clickable { viewModel.change(letter, !clicked) }) {
        Row(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(32.dp)
                    .border(2.dp, Color.Black, CircleShape)
                    .background(Color.White)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = letter,
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Black,
                    fontFamily = FontFamily(Font(R.font.arvo_bold)),
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.padding(4.dp))
            if (choice != null) {
                Text(
                    text = choice,
                    modifier = Modifier.align(Alignment.CenterVertically),
                    color = Color.Black,
                    fontFamily = arvo,
                    textAlign = TextAlign.Center
                )
            }
        }
    }

}

@Composable
@Preview
fun QuestionPreview() {
    BitcodeTheme {
        Question(list = null, null)
    }
}

@Composable
@Preview
fun StartPreview() {
    BitcodeTheme {
        Start(navController = null)
    }
}

data class Choices(val question: String?, val answers: List<String?>)