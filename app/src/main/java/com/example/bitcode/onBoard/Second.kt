package com.example.bitcode.onBoard

import android.os.Build
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.bitcode.R
import com.example.bitcode.tools.dpToSp
import com.example.bitcode.tools.getScreenSize
import com.example.bitcode.ui.theme.BitcodeTheme
import com.example.bitcode.ui.theme.arvo_bold
import com.example.bitcode.viewModels.OnBoardViewModel
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SplashSecond(navController: NavController, context: ComponentActivity?) {

    Scaffold {

        ConstraintLayout(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {

            BackHandler {

            }

            val (icon, text1, text2) = createRefs()
            val infiniteTransitionJava = rememberInfiniteTransition(label = "")

            var text1Visibility by remember { mutableStateOf(false) }
            var text2Visibility by remember { mutableStateOf(false) }

            var pressed by remember { mutableStateOf(false) }

            val viewModel = ViewModelProvider(context!!)[OnBoardViewModel::class.java]

            viewModel.onBoardSecondState.observe(context) { value ->
                pressed = value
            }

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
                    navController.navigate("splash_third")
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
@Preview
fun PreviewSplashSecond() {
    BitcodeTheme {
        SplashSecond(rememberNavController(), null)
    }
}