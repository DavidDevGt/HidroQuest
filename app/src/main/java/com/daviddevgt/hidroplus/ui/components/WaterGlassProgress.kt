package com.daviddevgt.hidroplus.ui.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daviddevgt.hidroplus.R
import com.daviddevgt.hidroplus.ui.theme.RPGGold
import com.daviddevgt.hidroplus.ui.theme.RPGHudGray
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun WaterGlassProgress(vasos: Int, meta: Int) {
    val infiniteTransition = rememberInfiniteTransition(label = "GlassProgressEffects")

    // Animación de brillo general
    val generalGlow by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "GeneralGlow"
    )

    // Animación de partículas flotantes
    val particleFlow by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing)
        ),
        label = "ParticleFlow"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .drawBehind {
                drawProgressBackground(generalGlow, particleFlow)
            },
        colors = CardDefaults.cardColors(
            containerColor = RPGHudGray.copy(alpha = 0.3f)
        ),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(2.dp, RPGGold.copy(alpha = 0.8f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título del progreso
            Text(
                text = "🏺 PROGRESO DE HIDRATACIÓN 🏺",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = RPGGold.copy(alpha = generalGlow),
                    fontSize = 12.sp,
                    letterSpacing = 1.sp
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(List(meta) { it }) { index, _ ->
                    RPGWaterGlass(
                        index = index,
                        isFilled = index < vasos,
                        isOverfilled = vasos > meta && index < vasos,
                        isNext = index == vasos && vasos < meta,
                        generalGlow = generalGlow
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Indicador de progreso textual
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Meta: $meta",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = RPGGold.copy(alpha = 0.8f),
                        fontSize = 10.sp
                    )
                )
                Text(
                    text = "$vasos / $meta",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = when {
                            vasos >= meta -> Color(0xFF4CAF50) // Verde completado
                            vasos >= meta * 0.7f -> Color(0xFFFF9800) // Naranja cerca
                            else -> Color(0xFF2196F3) // Azul en progreso
                        },
                        fontSize = 12.sp
                    )
                )
                Text(
                    text = "${((vasos.toFloat() / meta) * 100).toInt()}%",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = RPGGold.copy(alpha = 0.8f),
                        fontSize = 10.sp
                    )
                )
            }
        }
    }
}

@Composable
fun RPGWaterGlass(
    index: Int,
    isFilled: Boolean,
    isOverfilled: Boolean,
    isNext: Boolean,
    generalGlow: Float
) {
    val infiniteTransition = rememberInfiniteTransition(label = "GlassEffects_$index")

    // Animaciones específicas para cada estado
    val fillGlow by infiniteTransition.animateColor(
        initialValue = if (isFilled) Color(0xFF4FC3F7) else Color(0xFF37474F),
        targetValue = if (isFilled) Color(0xFF81D4FA) else Color(0xFF546E7A),
        animationSpec = infiniteRepeatable(
            animation = tween(1500 + index * 200, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "FillGlow"
    )

    val nextGlassGlow by infiniteTransition.animateFloat(
        initialValue = if (isNext) 0.5f else 1f,
        targetValue = if (isNext) 1f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "NextGlow"
    )

    val overflowEffect by infiniteTransition.animateColor(
        initialValue = if (isOverfilled) Color(0xFFFFD700) else Color.Transparent,
        targetValue = if (isOverfilled) Color(0xFFFFA000) else Color.Transparent,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "OverflowEffect"
    )

    val glassScale by infiniteTransition.animateFloat(
        initialValue = if (isNext) 0.95f else 1f,
        targetValue = if (isNext) 1.05f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "GlassScale"
    )

    Box(
        modifier = Modifier
            .size(48.dp)
            .scale(glassScale)
            .drawBehind {
                drawGlassEffects(
                    isFilled = isFilled,
                    isOverfilled = isOverfilled,
                    isNext = isNext,
                    fillColor = fillGlow,
                    overflowColor = overflowEffect,
                    nextGlow = nextGlassGlow,
                    generalGlow = generalGlow
                )
            }
            .clip(RoundedCornerShape(12.dp))
            .background(
                brush = when {
                    isOverfilled -> Brush.radialGradient(
                        colors = listOf(
                            overflowEffect.copy(alpha = 0.4f),
                            RPGHudGray.copy(alpha = 0.8f)
                        )
                    )
                    isFilled -> Brush.radialGradient(
                        colors = listOf(
                            fillGlow.copy(alpha = 0.3f),
                            RPGHudGray.copy(alpha = 0.9f)
                        )
                    )
                    isNext -> Brush.radialGradient(
                        colors = listOf(
                            RPGGold.copy(alpha = 0.2f),
                            RPGHudGray.copy(alpha = 0.7f)
                        )
                    )
                    else -> Brush.radialGradient(
                        colors = listOf(
                            RPGHudGray.copy(alpha = 0.6f),
                            RPGHudGray.copy(alpha = 0.8f)
                        )
                    )
                }
            )
            .border(
                width = when {
                    isOverfilled -> 3.dp
                    isFilled -> 2.dp
                    isNext -> 2.dp
                    else -> 1.dp
                },
                color = when {
                    isOverfilled -> overflowEffect
                    isFilled -> fillGlow.copy(alpha = 0.8f)
                    isNext -> RPGGold.copy(alpha = nextGlassGlow)
                    else -> RPGGold.copy(alpha = 0.4f)
                },
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        // Icono del vaso/gota con efectos
        Image(
            painter = painterResource(
                id = when {
                    isFilled -> R.drawable.pixel_glass
                    isNext -> R.drawable.pixel_drop // Gota brillante para el siguiente
                    else -> R.drawable.pixel_drop
                }
            ),
            contentDescription = when {
                isFilled -> "Vaso completado"
                isNext -> "Próximo vaso"
                else -> "Vaso pendiente"
            },
            modifier = Modifier.size(28.dp),
            colorFilter = ColorFilter.tint(
                when {
                    isOverfilled -> overflowEffect
                    isFilled -> fillGlow
                    isNext -> RPGGold.copy(alpha = nextGlassGlow)
                    else -> Color(0xFF78909C).copy(alpha = 0.6f)
                }
            )
        )

        // Efectos especiales para vasos llenos
        if (isFilled && !isOverfilled) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .offset(x = 12.dp, y = (-12).dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(fillGlow.copy(alpha = 0.8f))
            )
        }

        // Efectos especiales para overflow
        if (isOverfilled) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .offset(x = 14.dp, y = (-14).dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(overflowEffect.copy(alpha = 0.9f))
            )
        }
    }
}

fun DrawScope.drawProgressBackground(generalGlow: Float, particleFlow: Float) {
    val width = size.width
    val height = size.height

    // Fondo con gradiente sutil
    drawRect(
        brush = Brush.horizontalGradient(
            colors = listOf(
                Color.Transparent,
                RPGGold.copy(alpha = 0.05f * generalGlow),
                Color.Transparent
            )
        )
    )

    // Partículas flotantes de agua
    for (i in 0..4) {
        val progress = (particleFlow + i * 72f) % 360f
        val x = (progress / 360f) * width
        val y = height / 2 + sin((progress * 2) * Math.PI / 180) * (height / 8)

        drawCircle(
            color = Color(0xFF4FC3F7).copy(alpha = generalGlow * 0.3f),
            radius = 2f + (i % 2),
            center = Offset(x, y.toFloat())
        )
    }
}

fun DrawScope.drawGlassEffects(
    isFilled: Boolean,
    isOverfilled: Boolean,
    isNext: Boolean,
    fillColor: Color,
    overflowColor: Color,
    nextGlow: Float,
    generalGlow: Float
) {
    val centerX = size.width / 2
    val centerY = size.height / 2

    if (isFilled) {
        // Efecto de ondas de agua para vasos llenos
        for (i in 1..3) {
            drawCircle(
                color = fillColor.copy(alpha = 0.1f * generalGlow),
                radius = 8f * i,
                center = Offset(centerX, centerY),
                style = Stroke(width = 1f)
            )
        }
    }

    if (isOverfilled) {
        // Efecto de desbordamiento brillante
        drawCircle(
            color = overflowColor.copy(alpha = 0.4f),
            radius = size.width / 2 + 4f,
            center = Offset(centerX, centerY)
        )
    }

    if (isNext) {
        // Efecto pulsante para el siguiente vaso
        drawCircle(
            color = RPGGold.copy(alpha = 0.2f * nextGlow),
            radius = size.width / 2 + 2f,
            center = Offset(centerX, centerY),
            style = Stroke(width = 2f)
        )
    }
}