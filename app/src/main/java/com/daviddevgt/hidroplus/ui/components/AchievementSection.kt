package com.daviddevgt.hidroplus.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daviddevgt.hidroplus.R
import com.daviddevgt.hidroplus.ui.theme.PixelFont
import com.daviddevgt.hidroplus.ui.theme.RPGGold
import com.daviddevgt.hidroplus.ui.theme.RPGHudGray

@Composable
fun AchievementSection(
    logro: String,
    grito: String,
    habilidad: String
) {
    val achievements = listOfNotNull(
        R.drawable.pixel_trophy to logro,
        R.drawable.pixel_sword to grito,
        if (habilidad.isNotBlank()) R.drawable.pixel_fire to habilidad else null
    )

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(achievements) { (iconRes, text) ->
            AchievementCard(iconRes, text)
        }
    }
}

@Composable
fun AchievementCard(iconRes: Int, text: String) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = RPGHudGray
        ),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.5.dp, RPGGold)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 16.dp)
                .widthIn(max = 240.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = "Achievement Icon",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = text,
                fontFamily = PixelFont,
                fontSize = 10.sp,
                color = RPGGold
            )
        }
    }
}
