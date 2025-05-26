package com.daviddevgt.hidroplus.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daviddevgt.hidroplus.R
import com.daviddevgt.hidroplus.ui.theme.RPGGold
import com.daviddevgt.hidroplus.ui.theme.RPGHudGray

@Composable
fun CharacterCard(
    vasos: Int,
    meta: Int,
    progress: Float,
    progressColor: Color,
    waterScale: Float,
    pulseScale: Float,
    onAddWater: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(pulseScale),
        colors = CardDefaults.cardColors(
            containerColor = RPGHudGray
        ),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(2.dp, RPGGold),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(progressColor, Color.Transparent)
                        )
                    )
                    .border(3.dp, RPGGold, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.pixel_avatar),
                    contentDescription = "Avatar",
                    modifier = Modifier.size(64.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            RPGProgressBar(
                current = vasos,
                max = meta,
                progress = progress,
                color = progressColor,
                label = "HIDRATACIÓN"
            )

            Spacer(modifier = Modifier.height(15.dp))

            Button(
                onClick = onAddWater,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = progressColor
                ),
                shape = RoundedCornerShape(14.dp),
                elevation = ButtonDefaults.buttonElevation(6.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.pixel_potion),
                    contentDescription = "Beber agua",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "BEBER POCIÓN",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }
    }
}
