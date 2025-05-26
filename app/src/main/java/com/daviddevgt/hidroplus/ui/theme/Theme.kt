package com.daviddevgt.hidroplus.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val RPGColorScheme = darkColorScheme(
    primary = RPGGold,
    secondary = RPGEmerald,
    background = RPGHudGray,
    surface = RPGDarkPurple,
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

@Composable
fun HidroPlusTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = RPGColorScheme,
        typography = RPGTypography,
        content = content
    )
}
