package com.example.bitcode.tools

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import kotlin.math.roundToInt

@Composable
fun dpToSp(dp: Dp) = with(LocalDensity.current) { dp.toSp() }

fun spToDp(sp: Float) = sp * 2.2f

fun spToDpInt(sp: Float): Int {
    return spToDp(sp).roundToInt()
}
