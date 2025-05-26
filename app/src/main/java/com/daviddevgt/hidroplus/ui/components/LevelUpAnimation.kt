package com.daviddevgt.hidroplus.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daviddevgt.hidroplus.ui.theme.PixelFont
import com.daviddevgt.hidroplus.ui.theme.RPGGold
import com.daviddevgt.hidroplus.ui.theme.RPGHudGray
import kotlinx.coroutines.delay

@Composable
fun LevelUpAnimation(
    rango: String,
    onDismiss: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(3000)
        onDismiss()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(RPGHudGray.copy(alpha = 0.95f))
            .clickable { onDismiss() },
        contentAlignment = Alignment.Center
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = RPGGold
            ),
            shape = RoundedCornerShape(4.dp),
            elevation = CardDefaults.cardElevation(12.dp),
            border = BorderStroke(3.dp, Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .widthIn(min = 260.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "LEVEL UP!",
                    fontFamily = PixelFont,
                    fontSize = 24.sp,
                    color = RPGHudGray,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    rango,
                    fontFamily = PixelFont,
                    fontSize = 20.sp,
                    color = RPGHudGray,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
