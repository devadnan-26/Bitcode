package com.example.bitcode.appui

import android.app.Application
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bitcode.R
import com.example.bitcode.navigation.Courses
import com.example.bitcode.navigation.Home
import com.example.bitcode.navigation.Item
import com.example.bitcode.navigation.ItemObject
import com.example.bitcode.ui.theme.BitcodeTheme
import com.example.bitcode.viewModels.PlatformViewModel
import com.jakewharton.threetenabp.AndroidThreeTen


class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}

class Platform : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Questions", "Called OnCreate")
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
            }
            BitcodeTheme {
                val navController = rememberNavController()

                Scaffold(
                    bottomBar = {

                        BottomAppBar(containerColor = colorResource(R.color.app_green)) {
                            val interactionSource = remember { MutableInteractionSource() }
                            var vHome by remember { mutableStateOf(true) }
                            var vCourses by remember { mutableStateOf(false) }
                            var vProfile by remember { mutableStateOf(false) }
                            var vBitcodeX by remember { mutableStateOf(false) }
                            val viewModel =
                                ViewModelProvider(this@Platform)[PlatformViewModel::class.java]
                            val whichChosen = when {
                                viewModel.homeChosen.value == true -> viewModel.homeChosen
                                viewModel.coursesChosen.value == true -> viewModel.coursesChosen
                                viewModel.bitcodeXChosen.value == true -> viewModel.bitcodeXChosen
                                else -> viewModel.profileChosen
                            }
                            whichChosen.observe(this@Platform) {
                                when (whichChosen) {
                                    viewModel.homeChosen -> vHome = it
                                    viewModel.coursesChosen -> vCourses = it
                                    viewModel.bitcodeXChosen -> vBitcodeX = it
                                    viewModel.profileChosen -> vProfile = it
                                }
                            }
                            ItemObject(
                                item = Item.Home, modifier = Modifier
                                    .weight(3.0f)
                                    .clickable(
                                        interactionSource = interactionSource, indication = null
                                    ) {
                                        viewModel.changeItems("Home")
                                        navController.navigate("Home")
                                    }, visible = vHome
                            )
                            ItemObject(
                                item = Item.Courses,
                                Modifier
                                    .weight(3.0f)
                                    .clickable(
                                        interactionSource = interactionSource, indication = null
                                    ) {
                                        viewModel.changeItems("Courses")
                                        navController.navigate("Courses")
                                    }, visible = vCourses
                            )
                            ItemObject(
                                item = Item.BitcodeX,
                                Modifier
                                    .weight(3.0f)
                                    .clickable(
                                        interactionSource = interactionSource, indication = null
                                    ) {
                                        viewModel.changeItems("BitcodeX")
                                    }, visible = vBitcodeX
                            )
                            ItemObject(
                                item = Item.Profile,
                                Modifier
                                    .weight(3.0f)
                                    .clickable(
                                        interactionSource = interactionSource, indication = null
                                    ) {
                                        viewModel.changeItems("Profile")
                                    }, visible = vProfile
                            )
                        }
                    },
                ) {
                    ConstraintLayout(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it)
                    ) {
                        val (icon, navHost) = createRefs()
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
                        NavHost(navController = navController,
                            startDestination = "Home",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 18.dp)
                                .constrainAs(navHost) {
                                    linkTo(icon.bottom, parent.bottom)
                                    centerHorizontallyTo(parent)
                                    height = Dimension.preferredWrapContent
                                    width = Dimension.preferredWrapContent
                                }) {
                            composable("Home") { Home(context = this@Platform, navController) }
                            composable("Courses") { Courses(context = this@Platform, navController) }
                        }
                    }
                }
            }
        }
    }

}

