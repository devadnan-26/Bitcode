package com.example.bitcode.sections.bottomNavigation

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.bitcode.Items.Items
import com.example.bitcode.R
import com.example.bitcode.ui.theme.arvo_bold
import com.example.bitcode.viewModels.PlatformViewModel
import com.google.ai.client.generativeai.GenerativeModel
import kotlin.math.roundToInt


@Composable
fun BitcodeX(navController: NavController, context: ComponentActivity?) {

    val API_KEY = "AIzaSyA2YbF-TYC3VIFoXCW96gqBoU-mjOTjjUM"

    val viewModel = ViewModelProvider(context!!)[PlatformViewModel::class.java]

    BackHandler {
        viewModel.changeItem(Items.Home)
        navController.navigate("Home")
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        val offsetY = remember { androidx.compose.animation.core.Animatable(0f) }
        val offsetYColumn = remember { androidx.compose.animation.core.Animatable(0f) }
        LaunchedEffect(Unit) {
            offsetY.animateTo(
                targetValue = 50f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 3000,
                        easing = LinearEasing
                    ),
                    repeatMode = RepeatMode.Reverse
                )
            )
        }
        LaunchedEffect(Unit) {
            offsetYColumn.animateTo(
                targetValue = 35f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 5000,
                        easing = LinearEasing
                    ),
                    repeatMode = RepeatMode.Reverse
                )
            )
        }
        Column(modifier = Modifier.offset { IntOffset(0, offsetYColumn.value.roundToInt()) }, horizontalAlignment = Alignment.CenterHorizontally) {
            val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.coming_soon))
            LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier.size(250.dp)
            )
            Spacer(modifier = Modifier.padding(16.dp))
            Text(
                stringResource(id = R.string.coming_soon),
                fontFamily = arvo_bold,
                fontSize = 32.sp,
                modifier = Modifier.offset { IntOffset(0, offsetY.value.roundToInt()) }
            )
        }
    }
    val model = GenerativeModel("gemini-1.5-flash", apiKey = API_KEY)

    val coroutine = rememberCoroutineScope()
//    coroutine.launch {
//        val response = model.generateContent(content {
//            text("What's in this photo?")
//            image(ingredientsBitmap)
//        })
//    }
}
