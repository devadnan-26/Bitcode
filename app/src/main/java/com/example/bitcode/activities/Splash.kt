package com.example.bitcode.activities

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.RenderMode
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.bitcode.R
import com.example.bitcode.onBoard.SplashFirst
import com.example.bitcode.onBoard.SplashFourth
import com.example.bitcode.onBoard.SplashSecond
import com.example.bitcode.onBoard.SplashThird
import com.example.bitcode.tools.getScreenSize
import com.example.bitcode.ui.theme.BitcodeTheme
import kotlinx.coroutines.delay


class Splash : ComponentActivity() {


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
                window.decorView.systemUiVisibility = (
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                or View.SYSTEM_UI_FLAG_FULLSCREEN
                        )
            }
            BitcodeTheme {
                First(context = this@Splash)
            }
        }
    }
}


@Composable
fun First(context: ComponentActivity?) {

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
                Intent(context, OnBoard::class.java).also {
                    context.startActivity(it)
                    context.finish()
                }
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


