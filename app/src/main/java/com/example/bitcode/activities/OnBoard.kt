package com.example.bitcode.activities

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
import com.example.bitcode.onBoard.SplashFirst
import com.example.bitcode.onBoard.SplashFourth
import com.example.bitcode.onBoard.SplashSecond
import com.example.bitcode.onBoard.SplashThird
import com.example.bitcode.tools.getScreenSize
import com.example.bitcode.ui.theme.BitcodeTheme
import com.example.bitcode.viewModels.OnBoardViewModel
import kotlinx.coroutines.delay

class OnBoard : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

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
                val navController = rememberNavController()

                val density = LocalDensity.current.density

                var originalX by remember { mutableFloatStateOf(0f) }
                var originalY by remember { mutableFloatStateOf(0f) }

                val viewModel = ViewModelProvider(this@OnBoard)[OnBoardViewModel::class.java]

                ConstraintLayout(modifier = Modifier
                    .onGloballyPositioned { coordinates ->

                        val xInDp = (coordinates.positionInRoot().x / density).dp
                        val yInDp = (coordinates.positionInRoot().y / density).dp

                        originalX = xInDp.value
                        originalY = yInDp.value

                        Log.d("dimension", "x: ${originalX.dp}, y: ${originalY.dp}")
                        Log.d("dimension", "coordinates: ${coordinates.size}")
                    }
                    .fillMaxSize()) {

                    val (atom, button) = createRefs()

                    val composition by rememberLottieComposition(
                        spec = LottieCompositionSpec.RawRes(
                            R.raw.atom
                        )
                    )

                    var atomVisibility by remember { mutableStateOf(false) }
                    var buttonVisibility by remember { mutableStateOf(false) }
                    var state by remember { mutableStateOf(false) }

                    LaunchedEffect(state) {
                        if (!state) {
                            delay(6500)
                            atomVisibility = true; buttonVisibility = true
                        }
                        else {
                            delay(1000)
                            atomVisibility = false; buttonVisibility = false
                        }
                    }

                    NavHost(
                        navController = navController,
                        startDestination = "splash_first",
                        enterTransition = { EnterTransition.None },
                        exitTransition = { ExitTransition.None },
                        modifier = Modifier.fillMaxSize().background(Color.Transparent)
                    )
                    {
                        composable("splash_first") { SplashFirst(navController, this@OnBoard) }
                        composable("splash_second") { SplashSecond(navController,this@OnBoard) }
                        composable("splash_third") { SplashThird(navController,this@OnBoard) }
                        composable("splash_fourth") { SplashFourth(this@OnBoard) }
                    }

                    AnimatedVisibility(
                        visible = atomVisibility,
                        modifier = Modifier.constrainAs(atom) {
                            centerHorizontallyTo(parent)
                            centerVerticallyTo(parent, bias = 0.53f)
                        },
                        enter = fadeIn(),
                        exit = fadeOut()
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
                        visible = buttonVisibility, modifier = Modifier.constrainAs(button) {
                            bottom.linkTo(parent.bottom, margin = 24.dp)
                            end.linkTo(parent.end, margin = 24.dp)
                        },
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Button(
                            onClick = {
                                if(navController.currentBackStackEntry != null)

                                    when (navController.currentDestination!!.route) {
                                        "splash_first" -> {
                                            viewModel.changeFirstState(true)
                                            Log.d("Navigation", navController.currentDestination?.route.toString())
                                        }
                                        "splash_second" -> {
                                            viewModel.changeSecondState(true)
                                            Log.d("Navigation", navController.currentDestination?.route.toString())
                                        }
                                        "splash_third" ->  {
                                            viewModel.changeThirdState(true)
                                            Log.d("Navigation", navController.currentDestination?.route.toString())
                                            state = true
                                        }
                                        "splash_fourth" -> {
                                            viewModel.changeFourthState(true)
                                            Log.d("Navigation", navController.currentDestination?.route.toString())
                                        }
                                    }
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
                                if (Build.VERSION.SDK_INT >= 28) {
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

                }
            }
        }
    }
}