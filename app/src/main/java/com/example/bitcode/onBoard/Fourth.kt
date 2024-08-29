package com.example.bitcode.onBoard

import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.RenderMode
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.bitcode.R
import com.example.bitcode.activities.Questions
import com.example.bitcode.activities.Splash
import com.example.bitcode.tools.dpToSp
import com.example.bitcode.ui.theme.BitcodeTheme
import com.example.bitcode.ui.theme.arvo_bold
import com.example.bitcode.viewModels.OnBoardViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashFourth(context: ComponentActivity?) {

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

            BackHandler {

            }

            val (bicycle, next, icon, text1) = createRefs()

            var animationVisible by remember { mutableStateOf(false) }
            var iconVisible by remember { mutableStateOf(true) }
            var textVisible by remember { mutableStateOf(false) }
            var buttonVisible by remember { mutableStateOf(false) }
            var pressed by remember { mutableStateOf(false) }

            val activity = (LocalContext.current as? Splash)

            val viewModel = ViewModelProvider(context!!)[OnBoardViewModel::class.java]

            viewModel.onBoardFourthState.observe(context) { value ->
                pressed = value
            }
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
                    context.getSharedPreferences("Bitcode", Context.MODE_PRIVATE)?.edit()
                        ?.putBoolean("splash_shown", true)?.apply()
                    ContextCompat.startActivity(context, Intent(context, Questions::class.java), null)
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
fun PreviewSplashFourth() {
    BitcodeTheme {
        SplashFourth(null)
    }
}