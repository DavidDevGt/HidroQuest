package com.daviddevgt.hidroplus.ui


import androidx.compose.animation.core.*
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Size
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import com.daviddevgt.hidroplus.domain.GamificationEngine

//Componentes
import com.daviddevgt.hidroplus.ui.components.RPGHeader
import com.daviddevgt.hidroplus.ui.components.CharacterCard
import com.daviddevgt.hidroplus.ui.components.PlayerStats
import com.daviddevgt.hidroplus.ui.components.AchievementSection
import com.daviddevgt.hidroplus.ui.components.WaterGlassProgress
import com.daviddevgt.hidroplus.ui.components.LevelUpAnimation

import com.daviddevgt.hidroplus.ui.components.SettingsDialog
import com.daviddevgt.hidroplus.ui.components.SettingsData
import com.daviddevgt.hidroplus.ui.components.RPGSettingsButton

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    meta: Int = 7,
    vasosIniciales: Int = 0,
    diasConsecutivos: Int = 1,
    record: Int = 10
) {
    val engine = remember { GamificationEngine() }
    var vasos by remember { mutableStateOf(vasosIniciales) }
    var showLevelUp by remember { mutableStateOf(false) }
    var lastLevel by remember { mutableStateOf("") }
    var showSettings by remember { mutableStateOf(false) }
    var settings by remember { mutableStateOf(SettingsData(metaDiaria = meta)) }
    val metaActual = settings.metaDiaria

    val logro = engine.desbloquearLogro(vasos, meta)
    val rango = engine.subirDeNivel(vasos)
    val grito = engine.gritoDeGuerraDiario(vasos, record, meta)
    val habilidad = engine.desbloquearHabilidadSecreta(diasConsecutivos)

    // Detectar level up
    LaunchedEffect(rango) {
        if (lastLevel.isNotEmpty() && lastLevel != rango) {
            showLevelUp = true
        }
        lastLevel = rango
    }

    val pulseAnimation = rememberInfiniteTransition(label = "pulse")
    val pulseScale by pulseAnimation.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse scale"
    )

    val rotateAnimation = rememberInfiniteTransition(label = "rotate")
    val rotation by rotateAnimation.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing)
        ),
        label = "rotation"
    )

    val waterScale by animateFloatAsState(
        targetValue = if (vasos > vasosIniciales) 1.2f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "water scale"
    )

    val progress = (vasos.toFloat() / meta.toFloat()).coerceIn(0f, 1f)
    val progressColor by animateColorAsState(
        targetValue = when {
            progress >= 1f -> Color(0xFF4CAF50)
            progress >= 0.75f -> Color(0xFF2196F3)
            progress >= 0.5f -> Color(0xFF03A9F4)
            progress >= 0.25f -> Color(0xFF00BCD4)
            else -> Color(0xFFE0E0E0)
        },
        label = "progress color"
    )

    // Estado del scroll
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF0D47A1),
                        Color(0xFF1565C0),
                        Color(0xFF1976D2)
                    ),
                    radius = 1200f
                )
            )
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
        ) {
            drawRPGPixelSky()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
                .padding(bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RPGHeader()

            CharacterCard(
                vasos = vasos,
                meta = meta,
                progress = progress,
                progressColor = progressColor,
                waterScale = waterScale,
                pulseScale = pulseScale,
                onAddWater = {
                    vasos++
                    if (vasos % 2 == 0) {
                        // Trigger haptic feedback or sound effect
                    }
                }
            )

            // Stats del jugador
            PlayerStats(
                rango = rango,
                diasConsecutivos = diasConsecutivos,
                record = record,
                vasos = vasos
            )

            WaterGlassProgress(vasos = vasos, meta = meta)

            AchievementSection(
                logro = logro,
                grito = grito,
                habilidad = habilidad
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            RPGSettingsButton(
                onClick = { showSettings = true }
            )
        }


        if (showLevelUp) {
            LevelUpAnimation(
                rango = rango,
                onDismiss = { showLevelUp = false }
            )
        }

        SettingsDialog(
            isVisible = showSettings,
            settings = settings,
            onDismiss = { showSettings = false },
            onSettingsChanged = { newSettings ->
                settings = newSettings
            }
        )
    }
}

private fun DrawScope.drawRPGPixelSky() {
    drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(
                Color(0xFF070D24),  // Azul más oscuro
                Color(0xFF1A1E3A), // Azul intermedio
                Color(0xFF2A2D4D)   // Azul claro
            ),
            startY = 0f,
            endY = size.height * 0.8f
        ),
        size = size
    )

    val starPattern = listOf(
        Triple(0.15f, 0.2f, 1),
        Triple(0.4f, 0.35f, 2),
        Triple(0.7f, 0.15f, 3),
        Triple(0.85f, 0.45f, 2),
        Triple(0.25f, 0.65f, 1),
        Triple(0.55f, 0.75f, 1),
        Triple(0.65f, 0.55f, 2)
    )

    starPattern.forEach { (xPos, yPos, starSize) ->
        drawRect(
            color = Color(0xFFFFF9E7),
            topLeft = Offset(size.width * xPos, size.height * yPos),
            size = Size(starSize.toFloat(), starSize.toFloat())
        )
    }

    val moonCenter = Offset(size.width - 64f, 64f)
    drawCircle(
        color = Color(0xFFF4F1DE),
        radius = 24f,
        center = moonCenter
    )
    listOf(
        Offset(moonCenter.x - 8f, moonCenter.y - 4f) to 2f,
        Offset(moonCenter.x + 6f, moonCenter.y + 8f) to 3f,
        Offset(moonCenter.x + 2f, moonCenter.y - 10f) to 1f
    ).forEach { (pos, size) ->
        drawRect(
            color = Color(0xFFDED9C4),
            topLeft = pos,
            size = Size(size, size)
        )
    }

    val mountainColor = Color(0xFF2D3548)
    drawPath(
        path = Path().apply {
            moveTo(-50f, size.height)
            lineTo(size.width * 0.3f, size.height * 0.4f)
            lineTo(size.width + 50f, size.height)
            close()
        },
        color = mountainColor
    )

    drawPath(
        path = Path().apply {
            moveTo(size.width * 0.4f, size.height)
            lineTo(size.width * 0.6f, size.height * 0.5f)
            lineTo(size.width * 0.8f, size.height)
            close()
        },
        color = mountainColor.copy(alpha = 0.8f)
    )

    listOf(
        Triple(0.2f, 0.3f, 3),
        Triple(0.5f, 0.4f, 2),
        Triple(0.7f, 0.25f, 4)
    ).forEach { (x, y, s) ->
        val cloudSize = s * 8f
        drawRect(
            color = Color(0xFFE8F0F7).copy(alpha = 0.9f),
            topLeft = Offset(size.width * x, size.height * y),
            size = Size(cloudSize, cloudSize * 0.5f)
        )
        drawRect(
            color = Color(0xFFE8F0F7).copy(alpha = 0.9f),
            topLeft = Offset(size.width * x + 8f, size.height * y - 4f),
            size = Size(cloudSize * 0.7f, cloudSize * 0.4f)
        )
    }
}