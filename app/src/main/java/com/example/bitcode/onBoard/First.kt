package com.example.bitcode.onBoard

import android.os.Build
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
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

@Composable
fun SplashFirst(navController: NavController, context: ComponentActivity?) {
    Scaffold {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {

            BackHandler {

            }

            val (decorDown, decorUp, icon, text1, text2) = createRefs()

            var text1Visibility by remember { mutableStateOf(false) }
            var text2Visibility by remember { mutableStateOf(false) }
            var decor1Visibility by remember { mutableStateOf(false) }
            var decor2Visibility by remember { mutableStateOf(false) }
            var iconVisibility by remember { mutableStateOf(false) }
            var pressed by remember { mutableStateOf(false) }

            val density = LocalDensity.current.density

            var originalX by remember { mutableFloatStateOf(0f) }
            var originalY by remember { mutableFloatStateOf(0f) }

            val viewModel = ViewModelProvider(context!!)[OnBoardViewModel::class.java]

            viewModel.onBoardFirstState.observe(context) { value ->
                pressed = value
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
                    delay(3500)
                    iconVisibility = true
                    delay(1000)
                    decor1Visibility = true
                    decor2Visibility = true
                    delay(750)
                    text1Visibility = true
                    delay(350)
                    text2Visibility = true
                    delay(500)
                } else {
                    text1Visibility = false
                    delay(350)
                    text2Visibility = false
                    delay(500)
                    decor1Visibility = false
                    decor2Visibility = false
                    delay(250)
                    navController.navigate("splash_second")
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

                    Row(verticalAlignment = Alignment.CenterVertically) {

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
                    centerVerticallyTo(parent, bias = 0.65f)
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
@Preview
fun PreviewSplashFirst() {
    BitcodeTheme {
        SplashFirst(rememberNavController(), null)
    }
}