package com.example.bitcode.appui

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.airbnb.lottie.RenderMode
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.bitcode.R
import com.example.bitcode.ui.theme.BitcodeTheme
import com.example.bitcode.ui.theme.arvo_bold
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin


class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Bitcode", "OnCreate Created")
        setContent {

            if (SDK_INT >= Build.VERSION_CODES.R) {
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
            BitcodeTheme {

                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "splash_screen",
                    enterTransition = { fadeIn() },
                    exitTransition = { fadeOut() }
                )
                {
                    composable("splash_screen") { First(this@MainActivity, navController) }
                    composable("splash_first") { SplashFirst(navController) }
                    composable("splash_second") { SplashSecond(navController) }
                    composable("splash_third") { SplashThird(navController) }
                    composable("splash_fourth") { SplashFourth(this@MainActivity) }
                }
            }
        }
    }
}

@Composable
fun dpToSp(dp: Dp) = with(LocalDensity.current) { dp.toSp() }

fun spToDp(sp: Float) = sp * 2.2f

fun spToDpInt(sp: Float): Int {
    return spToDp(sp).roundToInt()
}

@Composable
fun First(context: ComponentActivity?, navController: NavController?) {

    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.splash_screen))
    var collapse by remember { mutableStateOf(false) }
    var move by remember { mutableStateOf(false) }
    val transition = updateTransition(collapse, label = "expandTransition")

    val size by transition.animateDp(
        transitionSpec = { tween(durationMillis = 500) },
        label = "sizeTransition"
    ) { collapsed ->
        if (!collapsed) (getScreenSize().width - 20).dp else 265.dp
    }

    val offsetTransition = updateTransition(move, label = "offsetTransition")

    val offset by offsetTransition.animateDp(
        transitionSpec = { tween(durationMillis = 500) },
        label = "offsetAnimation"
    ) { expanded ->
        if (expanded) (-(getScreenSize().height / 2).dp + 32.dp) else 0.dp
    }

    LaunchedEffect(Unit) {
        delay(5000)
        collapse = true
        move = true
        delay(1500)

        val skipSplash = context?.getSharedPreferences("Bitcode", Context.MODE_PRIVATE)
            ?.getBoolean("splash_shown", false)

        val skipQuestions = context?.getSharedPreferences("Bitcode", Context.MODE_PRIVATE)
            ?.getBoolean("questions_shown", false)

        when {
            !skipSplash!! -> {
                navController?.navigate("splash_first")
            }

            (!skipQuestions!!) -> {
                startActivity(context, Intent(context, Questions::class.java), null).also {
                    context.finish().also {
                        Log.d("finished", "activity finished")
                    }
                }
            }

            (skipQuestions) -> {
                startActivity(context, Intent(context, Platform::class.java), null).also {
                    context.finish().also {
                        Log.d("finished", "activity finished")
                    }
                }
            }
        }
    }
    Scaffold(modifier = Modifier.fillMaxSize()) {

        Box(modifier = Modifier.fillMaxSize()) {
            LottieAnimation(
                modifier = Modifier
                    .padding(it)
                    .align(Alignment.Center)
                    .size(size)
                    .offset(y = offset),
                composition = composition,
                iterations = 1,
                renderMode = RenderMode.HARDWARE
            )
        }
    }
}

@Composable
fun SplashFirst(navController: NavController?) {

    Scaffold {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {

            val (decorDown, decorUp, atom, next, icon, text1, text2) = createRefs()

            var text1Visibility by remember { mutableStateOf(false) }
            var text2Visibility by remember { mutableStateOf(false) }
            var atomVisibility by remember { mutableStateOf(false) }
            var decor1Visibility by remember { mutableStateOf(false) }
            var decor2Visibility by remember { mutableStateOf(false) }
            var buttonVisibility by remember { mutableStateOf(false) }
            var iconVisibility by remember { mutableStateOf(false) }
            var pressed by remember { mutableStateOf(false) }

            val density = LocalDensity.current.density

            var originalX by remember { mutableFloatStateOf(0f) }
            var originalY by remember { mutableFloatStateOf(0f) }

            val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.atom))

            AnimatedVisibility(
                visible = atomVisibility,
                modifier = Modifier.constrainAs(atom) {
                    centerHorizontallyTo(parent)
                    centerVerticallyTo(parent, bias = 0.53f)
                },
                enter = fadeIn(),
            ) {

                LottieAnimation(
                    modifier = Modifier
                        .size(
                            (356.68 * getScreenSize().width / 430).dp,
                            ((385.12 * getScreenSize().height / 932)).dp
                        )

                        .onGloballyPositioned { coordinates ->
                            val xInDp = (coordinates.positionInRoot().x / density).dp
                            val yInDp = (coordinates.positionInRoot().y / density).dp
                            originalX = xInDp.value
                            originalY = yInDp.value
                            Log.d("dimension", "x: ${originalX.dp}, y: ${originalY.dp}")
                        }
                        .alpha(0.5f),
                    composition = composition,
                    iterations = LottieConstants.IterateForever,
                )
            }

            AnimatedVisibility(
                visible = iconVisibility, modifier = Modifier
                    .constrainAs(icon) {
                        centerHorizontallyTo(parent)
                        top.linkTo(parent.top, margin = 16.dp)
                    },
                enter = fadeIn(),
                exit = fadeOut()
            ) {

                Row(modifier = Modifier
                    .onGloballyPositioned { coordinates ->
                        val xInDp = (coordinates.positionInRoot().x / density).dp
                        val yInDp = (coordinates.positionInRoot().y / density).dp
                        originalX = xInDp.value
                        originalY = yInDp.value
                        Log.d("dimension", "x: ${originalX.dp}, y: ${originalY.dp}")
                        Log.d("dimension", "coordinates: ${coordinates.size}")
                    }) {
                    Image(
                        painter = painterResource(id = R.drawable.bitcode_icon),
                        contentDescription = null,
                        alignment = Alignment.Center,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .size(72.dp)
                    )
                    Spacer(modifier = Modifier.padding(end = 8.dp))
                    Image(
                        painter = painterResource(id = R.drawable.bitcode),
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .size(115.09.dp, 23.35.dp)
                    )
                }
            }
            AnimatedVisibility(
                visible = decor2Visibility,
                modifier = Modifier.constrainAs(decorDown) {
                    start.linkTo(parent.start)
                    centerVerticallyTo(parent, bias = 0.9f)
                },
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.decor_down),
                    contentDescription = null,
                    modifier = Modifier
                        .size(
                            (135.65 * getScreenSize().width / 430).dp,
                            ((151.86 * getScreenSize().height / 932) + 30).dp
                        )
                )
            }

            AnimatedVisibility(
                visible = decor1Visibility,
                modifier = Modifier.constrainAs(decorUp) {
                    centerVerticallyTo(parent, bias = 0.13f)
                    end.linkTo(parent.end)
                },
                enter = fadeIn(),
                exit = fadeOut()
            ) {

                Image(
                    painter = painterResource(id = R.drawable.decor_up),
                    contentDescription = null,
                    modifier = Modifier
                        .size(
                            ((155.74 * getScreenSize().width / 430)).dp,
                            ((165.92 * getScreenSize().height / 932) + 30).dp
                        )
                )

            }

            LaunchedEffect(pressed) {
                if (!pressed) {
                    delay(2500)
                    iconVisibility = true
                    delay(1000)
                    decor1Visibility = true
                    decor2Visibility = true
                    delay(1000)
                    atomVisibility = true
                    delay(750)
                    text1Visibility = true
                    delay(350)
                    text2Visibility = true
                    delay(1000)
                    buttonVisibility = true
                    delay(500)
                } else {
                    text1Visibility = false
                    delay(350)
                    text2Visibility = false
                    delay(500)
                    decor1Visibility = false
                    decor2Visibility = false
                    delay(250)
                    navController?.navigate("splash_second")
                }
            }
            AnimatedVisibility(
                visible = buttonVisibility, modifier = Modifier.constrainAs(next) {
                    bottom.linkTo(parent.bottom, margin = 24.dp)
                    end.linkTo(parent.end, margin = 24.dp)
                },
                enter = fadeIn()
            ) {
                Button(
                    onClick = {
                        pressed = true
                    },
                    shape = RoundedCornerShape(22.dp),
                    colors = ButtonDefaults.buttonColors(
                        colorResource(R.color.app_green)
                    ),
                    modifier = Modifier
                        .defaultMinSize(minWidth = 96.dp, minHeight = 48.dp)
                        .height(48.dp)
                ) {
                    val imageLoaderRunning = ImageLoader.Builder(LocalContext.current).components {
                        if (SDK_INT >= 28) {
                            add(ImageDecoderDecoder.Factory())
                        } else {
                            add(GifDecoder.Factory())
                        }
                    }.build()

                    Image(
                        painter = rememberAsyncImagePainter(R.drawable.running, imageLoaderRunning),
                        contentScale = ContentScale.Fit,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp)
                    )

                    Spacer(modifier = Modifier.padding(4.dp))

                    Icon(
                        painter = painterResource(id = R.drawable.arrow),
                        contentDescription = null,
                    )
                }
            }

            AnimatedVisibility(
                visible = text1Visibility,
                modifier = Modifier.constrainAs(text1) {
                    start.linkTo(parent.start, margin = 12.dp)
                    centerVerticallyTo(parent, bias = 0.30f)
                },
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column {

                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                SpanStyle(
                                    fontFamily = arvo_bold,
                                    fontSize = dpToSp(dp = 38.dp),
                                    color = Color.Black
                                )
                            ) {
                                append(stringResource(id = R.string.text_splash_first_1_1))
                            }
                        },
                        lineHeight = dpToSp(dp = 56.dp),
                    )

                    Spacer(modifier = Modifier.padding(4.dp))

                    Row {

                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    SpanStyle(
                                        fontFamily = arvo_bold,
                                        fontSize = dpToSp(dp = 38.dp),
                                        color = Color.Black
                                    )
                                ) {
                                    append(stringResource(id = R.string.text_splash_first_1_2))
                                }
                            },
                            lineHeight = dpToSp(dp = 56.dp)
                        )

                        Spacer(modifier = Modifier.padding(4.dp))

                        Text(text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontFamily = arvo_bold,
                                    fontSize = dpToSp(dp = 38.dp),
                                    brush = Brush.linearGradient(
                                        listOf(
                                            Color(0xFF1FBBA8),
                                            Color(0xFF65BD4D)
                                        )
                                    )
                                )
                            ) {
                                append(stringResource(id = R.string.text_splash_first_1_3))
                            }
                        })
                    }
                }
            }

            AnimatedVisibility(
                visible = text2Visibility,
                modifier = Modifier.constrainAs(text2) {
                    linkTo(text1.bottom, decorDown.top, bias = 0.85f)
                    start.linkTo(parent.start, margin = 12.dp)
                },
                enter = fadeIn(),
                exit = fadeOut()
            ) {

                Text(
                    text = stringResource(id = R.string.text_splash_first_2),
                    fontFamily = arvo_bold,
                    fontSize = dpToSp(dp = 38.dp),
                    lineHeight = dpToSp(dp = 56.dp)
                )
            }

        }
    }
}

@Composable
fun SplashSecond(navController: NavController?) {

    Scaffold {

        ConstraintLayout(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {

            val (icon, next, text1, text2, atom) = createRefs()
            val infiniteTransitionJava = rememberInfiniteTransition(label = "")

            var text1Visibility by remember { mutableStateOf(false) }
            var text2Visibility by remember { mutableStateOf(false) }
            var pressed by remember { mutableStateOf(false) }

            val animatedProgressJava by infiniteTransitionJava.animateFloat(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(20000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                ), label = ""
            )

            val infiniteTransitionPython = rememberInfiniteTransition(label = "")
            val animatedProgressPython by infiniteTransitionPython.animateFloat(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(17000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                ), label = ""
            )

            LaunchedEffect(pressed) {
                if (!pressed) {
                    delay(1000)
                    text1Visibility = true
                    delay(350)
                    text2Visibility = true
                    delay(1000)
                } else {
                    text1Visibility = false
                    delay(350)
                    text2Visibility = false
                    delay(250)
                    navController?.navigate("splash_third")
                }
            }

            with(LocalDensity.current) {

                val radiusXJava = 200.dp.toPx()
                val radiusYJava = 150.dp.toPx()

                val centerXJava = ((LocalConfiguration.current.screenWidthDp / 2) - 150).dp.toPx()
                val centerYJava = ((LocalConfiguration.current.screenHeightDp / 2) - 60).dp.toPx()

                val angleJava = animatedProgressJava * 2 * Math.PI.toFloat()

                val offsetXJava = radiusXJava * -cos(angleJava)
                val offsetYJava = radiusYJava * sin(angleJava)

                val finalXJava = centerXJava + offsetXJava
                val finalYJava = centerYJava + offsetYJava

                val radiusXPython = 200.dp.toPx()
                val radiusYPython = 100.dp.toPx()

                val centerXPython = ((LocalConfiguration.current.screenWidthDp / 2) + 50).dp.toPx()
                val centerYPython = ((LocalConfiguration.current.screenHeightDp / 2) + 25).dp.toPx()

                val anglePython = animatedProgressPython * 2 * Math.PI.toFloat()

                val offsetXPython = radiusXPython * cos(anglePython)
                val offsetYPython = radiusYPython * sin(anglePython)

                val finalXPython = centerXPython + offsetXPython
                val finalYPython = centerYPython + offsetYPython

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

                val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.atom))


                LottieAnimation(
                    modifier = Modifier
                        .size(
                            (356.68 * getScreenSize().width / 430).dp,
                            ((385.12 * getScreenSize().height / 932)).dp
                        )
                        .constrainAs(atom) {
                            centerHorizontallyTo(parent)
                            centerVerticallyTo(parent, bias = 0.53f)
                        }
                        .alpha(0.5f),
                    composition = composition,
                    iterations = LottieConstants.IterateForever,
                )

                Button(onClick = { pressed = true },
                    shape = RoundedCornerShape(22.dp),
                    colors = ButtonDefaults.buttonColors(
                        colorResource(R.color.app_green)
                    ),
                    modifier = Modifier
                        .constrainAs(next) {
                            bottom.linkTo(parent.bottom, margin = 24.dp)
                            end.linkTo(parent.end, margin = 24.dp)
                        }
                        .defaultMinSize(minWidth = 96.dp, minHeight = 48.dp)
                        .height(48.dp)) {
                    val imageLoaderRunning = ImageLoader.Builder(LocalContext.current).components {
                        if (SDK_INT >= 28) {
                            add(ImageDecoderDecoder.Factory())
                        } else {
                            add(GifDecoder.Factory())
                        }
                    }.build()

                    Image(
                        painter = rememberAsyncImagePainter(R.drawable.running, imageLoaderRunning),
                        contentScale = ContentScale.Fit,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp)
                    )

                    Spacer(modifier = Modifier.padding(4.dp))

                    Icon(
                        painter = painterResource(id = R.drawable.arrow),
                        contentDescription = null,
                    )

                }

                Image(
                    painter = painterResource(id = R.drawable.java),
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .graphicsLayer {
                            translationX = finalXJava
                            translationY = finalYJava
                        }
                )

                Image(
                    painter = painterResource(id = R.drawable.python),
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .graphicsLayer {
                            translationX = finalXPython
                            translationY = finalYPython
                        }
                )

                AnimatedVisibility(
                    visible = text1Visibility,
                    modifier = Modifier.constrainAs(text1) {
                        linkTo(icon.bottom, parent.bottom, bias = 0.1f)
                        start.linkTo(parent.start, margin = 12.dp)
                    },
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {

                    Column {

                        Row {

                            Text(text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        fontFamily = arvo_bold,
                                        fontSize = dpToSp(dp = 38.dp),
                                    )
                                ) {
                                    append(stringResource(id = R.string.text_splash_second_1_1))
                                }
                            })

                            Spacer(modifier = Modifier.padding(4.dp))

                            Text(text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        fontFamily = arvo_bold,
                                        fontSize = dpToSp(dp = 38.dp),
                                        brush = Brush.linearGradient(
                                            listOf(
                                                Color(0xFF1FBBA8),
                                                Color(0xFF65BD4D)
                                            )
                                        )
                                    )
                                ) {
                                    append(stringResource(id = R.string.text_splash_second_1_2))
                                }
                            })

                        }

                        Text(
                            text = stringResource(id = R.string.text_splash_second_1_3),
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = dpToSp(dp = 38.dp),
                                fontFamily = arvo_bold
                            ),
                            lineHeight = dpToSp(dp = 56.dp)
                        )

                    }
                }

                AnimatedVisibility(
                    visible = text2Visibility,
                    modifier = Modifier.constrainAs(text2) {
                        centerVerticallyTo(parent, bias = 0.75f)
                        start.linkTo(parent.start, margin = 12.dp)
                    },
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {

                    Column {

                        Row {

                            Text(text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        fontFamily = arvo_bold,
                                        fontSize = dpToSp(dp = 38.dp),
                                    )
                                ) {
                                    append(stringResource(id = R.string.text_splash_second_2_1))
                                }
                            })

                            Spacer(modifier = Modifier.padding(4.dp))

                            Text(text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        fontFamily = arvo_bold,
                                        fontSize = dpToSp(dp = 38.dp),
                                        brush = Brush.horizontalGradient(
                                            colorStops = arrayOf(
                                                0.5f to Color(0xFF3673A5),
                                                0.5f to Color(0xFFFFD342)
                                            ),
                                        )
                                    )
                                ) {
                                    append(stringResource(id = R.string.text_splash_second_2_2))
                                }
                            })

                        }

                        Row {

                            Text(text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        fontFamily = arvo_bold,
                                        fontSize = dpToSp(dp = 38.dp),
                                    )
                                ) {
                                    append(stringResource(id = R.string.text_splash_second_2_3))
                                }
                            })

                            Spacer(modifier = Modifier.padding(4.dp))

                            Text(text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        fontFamily = arvo_bold,
                                        fontSize = dpToSp(dp = 38.dp),
                                        brush = Brush.verticalGradient(
                                            colorStops = arrayOf(
                                                0.48f to Color(0xFFF8981D),
                                                0.48f to Color(0xFF5382A1)
                                            ),
                                        )
                                    )
                                ) {
                                    append(stringResource(id = R.string.text_splash_second_2_4))
                                }
                                withStyle(
                                    style = SpanStyle(
                                        fontFamily = arvo_bold,
                                        fontSize = dpToSp(dp = 38.dp),
                                    )
                                ) {
                                    append(stringResource(id = R.string.text_splash_second_2_5))
                                }
                            })

                        }

                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        color = Color.Black,
                                        fontSize = dpToSp(dp = 38.dp),
                                        fontFamily = arvo_bold
                                    )
                                ) {
                                    append(stringResource(id = R.string.text_splash_second_2_6))
                                }
                            },
                            lineHeight = dpToSp(dp = 56.dp)
                        )

                    }
                }
            }
        }
    }
}

@Composable
fun SplashThird(navController: NavController?) {

    Scaffold {

        ConstraintLayout(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {

            val (icon, next, text1, text2, atom) = createRefs()

            var text1Visibility by remember { mutableStateOf(false) }
            var text2Visibility by remember { mutableStateOf(false) }
            var buttonVisibility by remember { mutableStateOf(true) }
            var atomVisibility by remember { mutableStateOf(true) }
            var pressed by remember { mutableStateOf(false) }

            LaunchedEffect(pressed) {
                if (!pressed) {
                    delay(1000)
                    text1Visibility = true
                    delay(350)
                    text2Visibility = true
                }
                else {
                    text1Visibility = false
                    delay(350)
                    text2Visibility = false
                    delay(750)
                    buttonVisibility = false
                    delay(1000)
                    atomVisibility = false
                    delay(1000)
                    navController?.navigate("splash_fourth")
                }
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

            val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.atom))

            AnimatedVisibility(
                visible = atomVisibility,
                exit = fadeOut(),
                modifier= Modifier.constrainAs(atom) {
                    centerHorizontallyTo(parent)
                    centerVerticallyTo(parent, bias = 0.53f)
            }) {
                LottieAnimation(
                    modifier = Modifier
                        .size(
                            (356.68 * getScreenSize().width / 430).dp,
                            ((385.12 * getScreenSize().height / 932)).dp
                        )
                        .alpha(0.5f),
                    composition = composition,
                    iterations = LottieConstants.IterateForever,
                )
            }


            AnimatedVisibility(visible = buttonVisibility, modifier = Modifier.constrainAs(next) {
                bottom.linkTo(parent.bottom, margin = 24.dp)
                end.linkTo(parent.end, margin = 24.dp)
            }, enter = EnterTransition.None, exit = fadeOut()) {

                Button(
                    onClick = { pressed = true },
                    shape = RoundedCornerShape(22.dp),
                    colors = ButtonDefaults.buttonColors(
                        colorResource(R.color.app_green)
                    ),
                    modifier = Modifier

                        .defaultMinSize(minWidth = 96.dp, minHeight = 48.dp)
                        .height(48.dp)
                ) {

                    val imageLoaderRunning = ImageLoader.Builder(LocalContext.current).components {
                        if (SDK_INT >= 28) {
                            add(ImageDecoderDecoder.Factory())
                        } else {
                            add(GifDecoder.Factory())
                        }
                    }.build()

                    Image(
                        painter = rememberAsyncImagePainter(R.drawable.running, imageLoaderRunning),
                        contentScale = ContentScale.Fit,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp)
                    )

                    Spacer(modifier = Modifier.padding(4.dp))

                    Icon(
                        painter = painterResource(id = R.drawable.arrow),
                        contentDescription = null,
                    )

                }
            }

            AnimatedVisibility(
                visible = text1Visibility, modifier = Modifier.constrainAs(text1) {
                    linkTo(icon.bottom, parent.bottom, bias = 0.15f)
                    start.linkTo(parent.start, margin = 12.dp)
                },
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column {

                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontFamily = arvo_bold,
                                    fontSize = dpToSp(dp = 38.dp),
                                )
                            ) {
                                append(stringResource(id = R.string.text_splash_third_1_1))
                            }
                        },
                        lineHeight = dpToSp(dp = 56.dp)
                    )

                    Spacer(modifier = Modifier.padding(4.dp))

                    Row {

                        Text(
                            text = stringResource(id = R.string.text_splash_third_1_2),
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = dpToSp(dp = 38.dp),
                                fontFamily = arvo_bold
                            ),
                            lineHeight = dpToSp(dp = 56.dp)
                        )

                        Spacer(modifier = Modifier.padding(4.dp))

                        Text(text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontFamily = arvo_bold,
                                    fontSize = dpToSp(dp = 38.dp),
                                    brush = Brush.linearGradient(
                                        listOf(
                                            Color(0xFF1FBBA8),
                                            Color(0xFF65BD4D)
                                        )
                                    )
                                )
                            ) {
                                append(stringResource(id = R.string.text_splash_third_1_3))
                            }
                        })
                    }
                }
            }

            AnimatedVisibility(
                visible = text2Visibility, modifier = Modifier.constrainAs(text2) {
                    centerVerticallyTo(parent, bias = 0.75f)
                    start.linkTo(parent.start, margin = 12.dp)
                },
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column {

                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontFamily = arvo_bold,
                                    fontSize = dpToSp(dp = 38.dp),
                                )
                            ) {
                                append(stringResource(id = R.string.text_splash_third_2_1))
                            }
                        },
                        lineHeight = dpToSp(dp = 56.dp)
                    )

                    Spacer(modifier = Modifier.padding(4.dp))

                    Image(
                        painter = painterResource(id = R.drawable.digital),
                        contentDescription = null,
                        modifier = Modifier
                            .size(
                                spToDpInt(84f).dp,
                                spToDpInt(24f).dp
                            )
                            .align(Alignment.Start)
                    )

                    Spacer(modifier = Modifier.padding(4.dp))

                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontFamily = arvo_bold,
                                    fontSize = dpToSp(dp = 38.dp),
                                )
                            ) {
                                append(stringResource(id = R.string.text_splash_third_2_2))
                            }
                        },
                        lineHeight = dpToSp(dp = 56.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SplashFourth(context: Context?) {

    Scaffold(modifier = Modifier.fillMaxSize()) {

        var backgroundVisible by remember { mutableStateOf(false) }

        AnimatedVisibility(visible = backgroundVisible, enter = fadeIn(), exit = fadeOut()) {
            Image(
                painterResource(id = R.drawable.programming_langs_icons_back),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.1f
            )
        }

        ConstraintLayout(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {

            val (bicycle, next, icon, text1) = createRefs()

            var animationVisible by remember { mutableStateOf(false) }
            var iconVisible by remember { mutableStateOf(true) }
            var textVisible by remember { mutableStateOf(false) }
            var buttonVisible by remember { mutableStateOf(false) }
            var pressed by remember { mutableStateOf(false) }

            val activity = (LocalContext.current as? MainActivity)

            LaunchedEffect(pressed) {
                if (!pressed) {
                    delay(1000)
                    backgroundVisible = true
                    delay(1000)
                    animationVisible = true
                    delay(200)
                    textVisible = true
                    delay(1000)
                    buttonVisible = true
                }
                else {
                    delay(500)
                    animationVisible = false
                    delay(500)
                    textVisible = false
                    delay(500)
                    buttonVisible = false
                    delay(1000)
                    backgroundVisible = false
                    delay(1000)
                    iconVisible = false
                    delay(1000)
                    context?.getSharedPreferences("Bitcode", Context.MODE_PRIVATE)?.edit()
                        ?.putBoolean("splash_shown", true)?.apply()
                    startActivity(context!!, Intent(context, Questions::class.java), null)
                    activity?.finish()
                }
            }

            AnimatedVisibility(visible = iconVisible,
                exit = fadeOut(),
                modifier = Modifier.constrainAs(icon) {
                    centerHorizontallyTo(parent)
                    top.linkTo(parent.top, margin = 16.dp)
            }) {
                Row {

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
            }

            val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.coding))

            AnimatedVisibility(
                visible = animationVisible,
                modifier = Modifier.constrainAs(bicycle) {
                    centerHorizontallyTo(parent)
                    centerVerticallyTo(parent, bias = 0.45f)
                },
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                LottieAnimation(
                    composition = composition,
                    iterations = LottieConstants.IterateForever,
                    renderMode = RenderMode.HARDWARE
                )
            }

            AnimatedVisibility(
                visible = buttonVisible, modifier = Modifier
                    .constrainAs(next) {
                        centerHorizontallyTo(parent)
                        centerVerticallyTo(parent, bias = 0.9f)
                    },
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Button(
                    onClick = { pressed = true },
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        colorResource(R.color.app_green)
                    ),
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .wrapContentWidth()
                ) {
                    Text(
                        text = stringResource(id = R.string.get_started),
                        fontFamily = arvo_bold,
                        fontSize = dpToSp(dp = 18.dp),
                        color = Color.White,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
            AnimatedVisibility(
                visible = textVisible, modifier = Modifier.constrainAs(text1) {
                    centerHorizontallyTo(parent)
                    centerVerticallyTo(parent, bias = 0.75f)
                },
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(
                    text = stringResource(id = R.string.text_splash_fourth_1_1),
                    fontSize = dpToSp(dp = 32.dp),
                    fontFamily = arvo_bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,

                    lineHeight = dpToSp(dp = 36.dp)
                )
            }
        }
    }
}

@Composable
@Preview
fun PreviewSplashFirst() {
    BitcodeTheme {
        SplashFirst(null)
    }
}

@Composable
@Preview
fun PreviewSplashSecond() {
    BitcodeTheme {
        SplashSecond(null)
    }
}

@Composable
@Preview
fun PreviewSplashThird() {
    BitcodeTheme {
        SplashThird(null)
    }
}

@Composable
@Preview
fun PreviewSplashFourth() {
    BitcodeTheme {
        SplashFourth(null)
    }
}

@Composable
fun getScreenSize(): Size {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val screenHeight = LocalConfiguration.current.screenHeightDp
    return Size(screenWidth, screenHeight)
}

data class Size(val width: Int, val height: Int)