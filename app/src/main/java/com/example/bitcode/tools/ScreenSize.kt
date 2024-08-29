package com.example.bitcode.tools

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import com.example.bitcode.data.Size

@Composable
fun getScreenSize(): Size {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val screenHeight = LocalConfiguration.current.screenHeightDp
    return Size(screenWidth, screenHeight)
}