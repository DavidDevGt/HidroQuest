package com.daviddevgt.hidroplus.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// Asegúrate de tener esta importación si la usas, aunque en el código actual no parece ser necesaria directamente aquí.
// import androidx.compose.ui.unit.em
import com.daviddevgt.hidroplus.R
import com.daviddevgt.hidroplus.ui.theme.RPGGold
import com.daviddevgt.hidroplus.ui.theme.RPGHudGray

@Composable
fun PlayerStats(
    rango: String,
    diasConsecutivos: Int,
    record: Int,
    vasos: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = RPGHudGray
        ),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(2.dp, RPGGold)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp) // Padding general dentro de la Card
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "ESTADÍSTICAS",
                style = MaterialTheme.typography.titleLarge.copy(color = RPGGold),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Primera fila de estadísticas
            Row(
                modifier = Modifier.fillMaxWidth(),
                // MODIFICACIÓN: Reducir el espaciado para dar más ancho a los ítems
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    // MODIFICACIÓN: Pasar el 'rango' completo.
                    // StatItem se encargará de la elipsis si es necesario.
                    StatItem(R.drawable.pixel_medal, "NIVEL", rango)
                }
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    StatItem(R.drawable.pixel_fire, "RACHA", "$diasConsecutivos días")
                }
            }

            Spacer(modifier = Modifier.height(16.dp)) // Espacio entre las dos filas

            // Segunda fila de estadísticas
            Row(
                modifier = Modifier.fillMaxWidth(),
                // MODIFICACIÓN: Reducir el espaciado para dar más ancho a los ítems
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    StatItem(R.drawable.pixel_trophy, "RÉCORD", "$record vasos")
                }
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    StatItem(R.drawable.pixel_heart, "HP", "${vasos * 10}")
                }
            }
        }
    }
}

@Composable
fun StatItem(iconRes: Int, label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 2.dp) // Mantenemos un pequeño padding
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = label,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 10.sp, color = RPGGold),
            textAlign = TextAlign.Center,
            maxLines = 2, // Permitimos hasta 2 líneas para la etiqueta por si acaso
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge.copy(color = RPGGold, fontSize = 12.sp),
            textAlign = TextAlign.Center,
            maxLines = 2, // El Text se encargará de ajustar a 2 líneas
            overflow = TextOverflow.Ellipsis, // y aplicar elipsis si el 'value' completo no cabe
            lineHeight = 14.sp // Puedes ajustar esto o incluso quitarlo para usar el default
        )
    }
}