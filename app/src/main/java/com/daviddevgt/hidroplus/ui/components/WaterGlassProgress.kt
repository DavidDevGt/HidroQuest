package com.daviddevgt.hidroplus.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.daviddevgt.hidroplus.R
import com.daviddevgt.hidroplus.ui.theme.RPGGold
import com.daviddevgt.hidroplus.ui.theme.RPGHudGray

@Composable
fun WaterGlassProgress(vasos: Int, meta: Int) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(meta) { index ->
            val isFilled = index < vasos
            val isOverfilled = index < vasos && vasos > meta

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = if (isOverfilled) RPGGold else RPGHudGray
                    )
                    .border(BorderStroke(2.dp, RPGGold)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(
                        id = if (isFilled) R.drawable.pixel_glass else R.drawable.pixel_drop
                    ),
                    contentDescription = if (isFilled) "Vaso lleno" else "Vaso vacío",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
