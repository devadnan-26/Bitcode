package com.example.bitcode.sections.bottomNavigation

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bitcode.Items.Items
import com.example.bitcode.sections.profile.Dashboard
import com.example.bitcode.sections.profile.Information
import com.example.bitcode.sections.profile.Main
import com.example.bitcode.sections.profile.Settings
import com.example.bitcode.sections.profile.Support
import com.example.bitcode.ui.theme.BitcodeTheme
import com.example.bitcode.viewModels.PlatformViewModel

@Composable
fun Profile(navController: NavController, context: ComponentActivity?) {
    val viewModel = ViewModelProvider(context!!)[PlatformViewModel::class.java]
    BackHandler {
        viewModel.changeItem(Items.Home)
        navController.navigate("Home")
    }
    Scaffold {
        val profileNavController = rememberNavController()
        NavHost(
            navController = profileNavController,
            startDestination = "main",
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            composable("main") { Main(profileNavController) }
            composable("dashboard") { Dashboard(profileNavController) }
            composable("settings") { Settings(profileNavController) }
            composable("information") { Information(profileNavController) }
            composable("support") { Support(profileNavController) }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    BitcodeTheme {
        Profile(rememberNavController(), null)
    }
}
