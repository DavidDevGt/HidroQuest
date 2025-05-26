package com.daviddevgt.hidroplus.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.daviddevgt.hidroplus.R

val PixelFont = FontFamily(
    Font(R.font.pixel_font)
)

val RPGTypography = Typography(
    headlineSmall = TextStyle(
        fontFamily = PixelFont,
        fontSize = 20.sp
    ),
    titleLarge = TextStyle(
        fontFamily = PixelFont,
        fontSize = 18.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = PixelFont,
        fontSize = 14.sp
    )
)
