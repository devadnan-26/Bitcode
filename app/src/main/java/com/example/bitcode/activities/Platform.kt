package com.example.bitcode.activities

import android.app.Application
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bitcode.R
import com.example.bitcode.Items.Items
import com.example.bitcode.sections.bottomNavigation.BitcodeX
import com.example.bitcode.sections.bottomNavigation.Courses
import com.example.bitcode.sections.bottomNavigation.Home
import com.example.bitcode.sections.bottomNavigation.Item
import com.example.bitcode.sections.bottomNavigation.Profile
import com.example.bitcode.sections.bottomNavigation.SearchView
import com.example.bitcode.tools.dpToSp
import com.example.bitcode.ui.theme.BitcodeTheme
import com.example.bitcode.ui.theme.arvo_bold
import com.example.bitcode.viewModels.PlatformViewModel
import com.jakewharton.threetenabp.AndroidThreeTen


class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}
@Suppress("DEPRECATION")
class Platform : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Questions", "Called OnCreate")
        setContent {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val controller = window.insetsController
                // Hide both the status bar and the navigation bar
                controller?.let {
//                    it.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                    it.hide(WindowInsets.Type.systemBars())
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
                ViewPager(context = this@Platform)
            }
        }
    }

}

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

@Composable
fun ViewPager(context: ComponentActivity) {
    val navController = rememberNavController()
    var state by remember { mutableStateOf(Items.Home) }
    val viewModel = ViewModelProvider(context)[PlatformViewModel::class.java]
    viewModel.whichNavItemChosen.observe(context) {
        state = it
    }
    BackHandler {
        if (navController.currentDestination?.route == "Home") {
            context.finish()
        }
        if (navController.currentDestination != null) {
            state = Items.Home
            navController.navigate("Home")
        }
        Log.d("Navigation", state.name)
    }

    Scaffold(
        bottomBar = {
            BottomAppBar(containerColor = colorResource(R.color.app_green)) {
                val interactionSource = remember { MutableInteractionSource() }
                ItemObject(
                    item = Item.Home, modifier = Modifier
                        .weight(3.0f)
                        .clickable(
                            interactionSource = interactionSource, indication = null
                        ) {
                            if (navController.currentDestination?.route != "Home") {
                                state = Items.Home
                                navController.navigate("Home")
                            }
                        }, visible = state == Items.Home
                )
                ItemObject(
                    item = Item.Courses, modifier = Modifier
                        .weight(3.0f)
                        .clickable(
                            interactionSource = interactionSource, indication = null
                        ) {
                            if (navController.currentDestination?.route != "Courses") {
                                state = Items.Courses
                                navController.navigate("Courses")
                            }
                        }, visible = state == Items.Courses
                )
                ItemObject(
                    item = Item.BitcodeX, modifier = Modifier
                        .weight(3.0f)
                        .clickable(
                            interactionSource = interactionSource, indication = null
                        ) {
                            if (navController.currentDestination?.route != "BitcodeX") {
                                state = Items.BitcodeX
                                navController.navigate("BitcodeX")
                            }
                        }, visible = state == Items.BitcodeX
                )
                ItemObject(
                    item = Item.Profile, modifier = Modifier
                        .weight(3.0f)
                        .clickable(
                            interactionSource = interactionSource, indication = null
                        ) {
                            if (navController.currentDestination?.route != "Profile") {
                                state = Items.Profile
                                navController.navigate("Profile")
                            }
                        }, visible = state == Items.Profile
                )
                    }
            }
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            val (icon, navHost) = createRefs()
            NavHost(navController = navController,
                startDestination = "Home",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 18.dp)
                    .constrainAs(navHost) {
                        centerHorizontallyTo(parent)
                        height = Dimension.preferredWrapContent
                        width = Dimension.preferredWrapContent
                    }) {
                composable("Home") { Home(context = context, navController) }
                composable("Courses") { Courses(context = context, navController) }
                composable("Profile") { Profile(navController, context) }
                composable("BitcodeX") { BitcodeX(navController, context) }
                composable("Search") { SearchView(context = context, navController) }
            }
        }
    }
}

