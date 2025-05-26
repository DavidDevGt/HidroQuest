package com.daviddevgt.hidroplus.ui.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daviddevgt.hidroplus.R
import com.daviddevgt.hidroplus.ui.theme.RPGGold
import com.daviddevgt.hidroplus.ui.theme.RPGHudGray
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun RPGWaterButton(onClick: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "ButtonEffects")

    // Animación del color de fondo de la poción
    val potionGlow by infiniteTransition.animateColor(
        initialValue = Color(0xFF4FC3F7),
        targetValue = Color(0xFF81D4FA),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "PotionGlow"
    )

    // Brillo del borde dorado
    val borderGlow by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "BorderGlow"
    )

    // Efecto de partículas mágicas
    val magicParticles by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing)
        ),
        label = "MagicParticles"
    )

    // Escala para efecto de presión
    var isPressed by remember { mutableStateOf(false) }
    val buttonScale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "ButtonScale"
    )

    Button(
        onClick = {
            isPressed = true
            onClick()
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .scale(buttonScale)
            .drawBehind {
                drawMagicalEffects(borderGlow, magicParticles, potionGlow)
            }
            .clip(RoundedCornerShape(16.dp))
            .border(
                3.dp,
                RPGGold.copy(alpha = borderGlow),
                RoundedCornerShape(16.dp)
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = RPGHudGray.copy(alpha = 0.9f)
        ),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 6.dp,
            pressedElevation = 2.dp
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Icono de poción con efectos
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .drawBehind {
                        // Aura mágica alrededor de la poción
                        drawCircle(
                            color = potionGlow.copy(alpha = 0.4f),
                            radius = 16.dp.toPx(),
                            center = center
                        )
                        drawCircle(
                            color = potionGlow.copy(alpha = 0.2f),
                            radius = 20.dp.toPx(),
                            center = center
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.pixel_potion),
                    contentDescription = "Poción Mágica",
                    modifier = Modifier.size(24.dp),
                    tint = potionGlow
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Texto con efectos RPG
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "⚡ BEBER POCIÓN ⚡",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = RPGGold,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp,
                        letterSpacing = 1.sp
                    )
                )
                Text(
                    text = "de hidratación",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = potionGlow,
                        fontWeight = FontWeight.Medium,
                        fontSize = 10.sp,
                        letterSpacing = 0.5.sp
                    )
                )
            }
        }
    }

    // Reset del estado de presión
    LaunchedEffect(isPressed) {
        if (isPressed) {
            kotlinx.coroutines.delay(150)
            isPressed = false
        }
    }
}

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
    val infiniteTransition = rememberInfiniteTransition(label = "CardEffects")

    // Brillo sutil del borde de la card
    val cardGlow by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "CardGlow"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(pulseScale)
            .drawBehind {
                drawRect(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            RPGGold.copy(alpha = 0.05f),
                            Color.Transparent
                        ),
                        center = Offset(size.width / 2, size.height / 2),
                        radius = size.width / 2
                    )
                )
            },
        colors = CardDefaults.cardColors(
            containerColor = RPGHudGray.copy(alpha = 0.95f)
        ),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(3.dp, RPGGold.copy(alpha = cardGlow)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .drawBehind {
                        drawCircle(
                            color = progressColor.copy(alpha = 0.3f),
                            radius = 40.dp.toPx(),
                            center = center
                        )
                        drawCircle(
                            color = RPGGold.copy(alpha = 0.2f),
                            radius = 36.dp.toPx(),
                            center = center,
                            style = Stroke(width = 2.dp.toPx())
                        )
                    }
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.3f),
                                Color.Black.copy(alpha = 0.1f)
                            )
                        )
                    )
                    .border(3.dp, RPGGold.copy(alpha = cardGlow), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.pixel_avatar),
                    contentDescription = "Guerrero Hidratado",
                    modifier = Modifier.size(45.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            RPGProgressBar(
                current = vasos,
                max = meta,
                progress = progress,
                color = progressColor,
                label = "HIDRATACIÓN"
            )

            Spacer(modifier = Modifier.height(16.dp))

            RPGWaterButton(onClick = onAddWater)
        }
    }
}

fun DrawScope.drawMagicalEffects(borderGlow: Float, particleRotation: Float, potionColor: Color) {
    val width = size.width
    val height = size.height

    // Efectos de partículas mágicas flotantes
    for (i in 0..6) {
        val angle = (particleRotation + i * 51.4f) * (Math.PI / 180f)
        val radius = (width / 3) * (0.7f + (i % 3) * 0.1f)
        val x = (width / 2) + cos(angle) * radius
        val y = (height / 2) + sin(angle) * radius

        val particleSize = 1.5f + (i % 3) * 0.8f
        val alpha = (borderGlow * 0.6f) * ((i % 2) + 1) * 0.5f

        drawCircle(
            color = potionColor.copy(alpha = alpha),
            radius = particleSize,
            center = Offset(x.toFloat(), y.toFloat())
        )
    }

    // Líneas de energía en las esquinas
    val cornerLength = 15.dp.toPx()
    val strokeWidth = 1.5.dp.toPx()
    val cornerAlpha = borderGlow * 0.8f

    // Esquinas superiores
    drawLine(
        color = RPGGold.copy(alpha = cornerAlpha),
        start = Offset(8.dp.toPx(), 8.dp.toPx()),
        end = Offset(8.dp.toPx() + cornerLength, 8.dp.toPx()),
        strokeWidth = strokeWidth
    )
    drawLine(
        color = RPGGold.copy(alpha = cornerAlpha),
        start = Offset(8.dp.toPx(), 8.dp.toPx()),
        end = Offset(8.dp.toPx(), 8.dp.toPx() + cornerLength),
        strokeWidth = strokeWidth
    )

    drawLine(
        color = RPGGold.copy(alpha = cornerAlpha),
        start = Offset(width - 8.dp.toPx(), 8.dp.toPx()),
        end = Offset(width - 8.dp.toPx() - cornerLength, 8.dp.toPx()),
        strokeWidth = strokeWidth
    )
    drawLine(
        color = RPGGold.copy(alpha = cornerAlpha),
        start = Offset(width - 8.dp.toPx(), 8.dp.toPx()),
        end = Offset(width - 8.dp.toPx(), 8.dp.toPx() + cornerLength),
        strokeWidth = strokeWidth
    )

    // Brillo central sutil
    drawCircle(
        color = potionColor.copy(alpha = 0.1f),
        radius = width / 4,
        center = Offset(width / 2, height / 2)
    )
}