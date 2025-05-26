package com.daviddevgt.hidroplus.ui.components

import com.daviddevgt.hidroplus.ui.theme.RPGHudGray
import com.daviddevgt.hidroplus.ui.theme.RPGGold
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Tab

data class SettingsData(
    val metaDiaria: Int = 8,
    val recordatoriosHabilitados: Boolean = true,
    val intervaloRecordatorio: Int = 60, // minutos
    val sonidoHabilitado: Boolean = true,
    val vibracionHabilitada: Boolean = true,
    val modoOscuro: Boolean = false,
    val unidadMedida: String = "Vasos", // "Vasos", "ml", "L"
    val capacidadVaso: Int = 250, // ml
    val horaInicio: String = "07:00",
    val horaFin: String = "22:00",
    val animacionesHabilitadas: Boolean = true,
    val notificacionesMotivacionales: Boolean = true
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsDialog(
    isVisible: Boolean,
    settings: SettingsData = SettingsData(),
    onDismiss: () -> Unit,
    onSettingsChanged: (SettingsData) -> Unit
) {
    var currentSettings by remember { mutableStateOf(settings) }
    var selectedTab by remember { mutableStateOf(0) }

    val pulseAnimation = rememberInfiniteTransition(label = "pulse")
    val pulseScale by pulseAnimation.animateFloat(
        initialValue = 1f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse scale"
    )

    if (isVisible) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.7f))
                    .clickable { onDismiss() },
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .fillMaxHeight(0.9f)
                        .clickable(enabled = false) { }
                        .scale(pulseScale),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF1A1E3A)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Header con decoración RPG
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(
                                            Color(0xFF2A2D4D),
                                            Color(0xFF1A1E3A),
                                            Color(0xFF2A2D4D)
                                        )
                                    )
                                )
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                drawRPGHeader()
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "⚙️ CONFIGURACIÓN",
                                    color = Color(0xFFF4F1DE),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                IconButton(
                                    onClick = onDismiss,
                                    modifier = Modifier
                                        .background(
                                            Color(0xFFE74C3C).copy(alpha = 0.8f),
                                            RoundedCornerShape(8.dp)
                                        )
                                        .size(40.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "Cerrar",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }

                        // Tabs
                        TabRow(
                            selectedTabIndex = selectedTab,
                            containerColor = Color(0xFF2A2D4D),
                            contentColor = Color(0xFFF4F1DE),
                            indicator = { tabPositions ->
                                TabRowDefaults.Indicator(
                                    Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                                    color = Color(0xFF4CAF50)
                                )
                            }
                        ) {
                            Tab(
                                selected = selectedTab == 0,
                                onClick = { selectedTab = 0 },
                                text = { Text("General") }
                            )
                            Tab(
                                selected = selectedTab == 1,
                                onClick = { selectedTab = 1 },
                                text = { Text("Notificaciones") }
                            )
                            Tab(
                                selected = selectedTab == 2,
                                onClick = { selectedTab = 2 },
                                text = { Text("Personalización") }
                            )
                        }

                        // Contenido con scroll
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            when (selectedTab) {
                                0 -> GeneralSettings(
                                    settings = currentSettings,
                                    onSettingsChanged = { currentSettings = it }
                                )
                                1 -> NotificationSettings(
                                    settings = currentSettings,
                                    onSettingsChanged = { currentSettings = it }
                                )
                                2 -> PersonalizationSettings(
                                    settings = currentSettings,
                                    onSettingsChanged = { currentSettings = it }
                                )
                            }

                            // Botones de acción
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        currentSettings = SettingsData() // Reset a defaults
                                    },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = Color(0xFFF39C12)
                                    ),
                                    border = BorderStroke(1.dp, Color(0xFFF39C12))
                                ) {
                                    Icon(Icons.Default.Refresh, contentDescription = null)
                                    Spacer(Modifier.width(8.dp))
                                    Text("Resetear")
                                }

                                Button(
                                    onClick = {
                                        onSettingsChanged(currentSettings)
                                        onDismiss()
                                    },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF4CAF50)
                                    )
                                ) {
                                    Icon(Icons.Default.Check, contentDescription = null)
                                    Spacer(Modifier.width(8.dp))
                                    Text("Guardar")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GeneralSettings(
    settings: SettingsData,
    onSettingsChanged: (SettingsData) -> Unit
) {
    SettingsSection(title = "🎯 META DIARIA") {
        SettingsSlider(
            label = "Vasos por día",
            value = settings.metaDiaria,
            range = 4..15,
            onValueChange = { onSettingsChanged(settings.copy(metaDiaria = it)) },
            suffix = " vasos"
        )
    }

    SettingsSection(title = "🥤 UNIDADES") {
        SettingsDropdown(
            label = "Unidad de medida",
            value = settings.unidadMedida,
            options = listOf("Vasos", "ml", "L"),
            onValueChange = { onSettingsChanged(settings.copy(unidadMedida = it)) }
        )

        if (settings.unidadMedida == "Vasos") {
            SettingsSlider(
                label = "Capacidad del vaso",
                value = settings.capacidadVaso,
                range = 150..500,
                step = 25,
                onValueChange = { onSettingsChanged(settings.copy(capacidadVaso = it)) },
                suffix = " ml"
            )
        }
    }

    SettingsSection(title = "⏰ HORARIOS") {
        SettingsTimePicker(
            label = "Hora de inicio",
            value = settings.horaInicio,
            onValueChange = { onSettingsChanged(settings.copy(horaInicio = it)) }
        )

        SettingsTimePicker(
            label = "Hora de fin",
            value = settings.horaFin,
            onValueChange = { onSettingsChanged(settings.copy(horaFin = it)) }
        )
    }
}

@Composable
private fun NotificationSettings(
    settings: SettingsData,
    onSettingsChanged: (SettingsData) -> Unit
) {
    SettingsSection(title = "🔔 RECORDATORIOS") {
        SettingsSwitch(
            label = "Recordatorios habilitados",
            checked = settings.recordatoriosHabilitados,
            onCheckedChange = { onSettingsChanged(settings.copy(recordatoriosHabilitados = it)) }
        )

        if (settings.recordatoriosHabilitados) {
            SettingsSlider(
                label = "Intervalo de recordatorio",
                value = settings.intervaloRecordatorio,
                range = 15..240,
                step = 15,
                onValueChange = { onSettingsChanged(settings.copy(intervaloRecordatorio = it)) },
                suffix = " minutos"
            )
        }
    }

    SettingsSection(title = "🔊 EFECTOS") {
        SettingsSwitch(
            label = "Sonido habilitado",
            checked = settings.sonidoHabilitado,
            onCheckedChange = { onSettingsChanged(settings.copy(sonidoHabilitado = it)) }
        )

        SettingsSwitch(
            label = "Vibración habilitada",
            checked = settings.vibracionHabilitada,
            onCheckedChange = { onSettingsChanged(settings.copy(vibracionHabilitada = it)) }
        )
    }

    SettingsSection(title = "💪 MOTIVACIÓN") {
        SettingsSwitch(
            label = "Notificaciones motivacionales",
            checked = settings.notificacionesMotivacionales,
            onCheckedChange = { onSettingsChanged(settings.copy(notificacionesMotivacionales = it)) }
        )
    }
}

@Composable
private fun PersonalizationSettings(
    settings: SettingsData,
    onSettingsChanged: (SettingsData) -> Unit
) {
    SettingsSection(title = "🎨 APARIENCIA") {
        SettingsSwitch(
            label = "Modo oscuro",
            checked = settings.modoOscuro,
            onCheckedChange = { onSettingsChanged(settings.copy(modoOscuro = it)) }
        )

        SettingsSwitch(
            label = "Animaciones habilitadas",
            checked = settings.animacionesHabilitadas,
            onCheckedChange = { onSettingsChanged(settings.copy(animacionesHabilitadas = it)) }
        )
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2A2D4D).copy(alpha = 0.8f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                color = Color(0xFFF4F1DE),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            content()
        }
    }
}

@Composable
private fun SettingsSwitch(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = Color(0xFFE0E0E0),
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color(0xFF4CAF50),
                checkedTrackColor = Color(0xFF4CAF50).copy(alpha = 0.5f),
                uncheckedThumbColor = Color(0xFF757575),
                uncheckedTrackColor = Color(0xFF424242)
            )
        )
    }
}

@Composable
private fun SettingsSlider(
    label: String,
    value: Int,
    range: IntRange,
    step: Int = 1,
    onValueChange: (Int) -> Unit,
    suffix: String = ""
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                color = Color(0xFFE0E0E0)
            )
            Text(
                text = "$value$suffix",
                color = Color(0xFF4CAF50),
                fontWeight = FontWeight.Bold
            )
        }

        Slider(
            value = value.toFloat(),
            onValueChange = { onValueChange(it.toInt()) },
            valueRange = range.first.toFloat()..range.last.toFloat(),
            steps = (range.last - range.first) / step - 1,
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFF4CAF50),
                activeTrackColor = Color(0xFF4CAF50),
                inactiveTrackColor = Color(0xFF424242)
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsDropdown(
    label: String,
    value: String,
    options: List<String>,
    onValueChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(
            text = label,
            color = Color(0xFFE0E0E0),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = { },
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color(0xFFE0E0E0),
                    unfocusedTextColor = Color(0xFFE0E0E0),
                    focusedBorderColor = Color(0xFF4CAF50),
                    unfocusedBorderColor = Color(0xFF757575)
                ),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(Color(0xFF1A1E3A))
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                option,
                                color = Color(0xFFE0E0E0)
                            )
                        },
                        onClick = {
                            onValueChange(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsTimePicker(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    // Simplified time picker - in a real app you'd use a proper time picker
    Column {
        Text(
            text = label,
            color = Color(0xFFE0E0E0),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text("HH:MM", color = Color(0xFF757575)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color(0xFFE0E0E0),
                unfocusedTextColor = Color(0xFFE0E0E0),
                focusedBorderColor = Color(0xFF4CAF50),
                unfocusedBorderColor = Color(0xFF757575)
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

private fun DrawScope.drawRPGHeader() {
    // Decoración de header estilo RPG
    val headerHeight = size.height
    val segmentWidth = size.width / 8

    for (i in 0..7) {
        val alpha = if (i % 2 == 0) 0.3f else 0.1f
        drawRect(
            color = Color(0xFF4CAF50).copy(alpha = alpha),
            topLeft = Offset(i * segmentWidth, 0f),
            size = androidx.compose.ui.geometry.Size(segmentWidth, headerHeight)
        )
    }

    // Líneas decorativas
    drawRect(
        color = Color(0xFF4CAF50),
        topLeft = Offset(0f, headerHeight - 4f),
        size = androidx.compose.ui.geometry.Size(size.width, 2f)
    )
}

/**
 * Un botón 2D-pixel RGP para abrir/cerrar ajustes.
 * Úsalo en HomeScreen o donde necesites.
 */
@Composable
fun RPGSettingsButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulseButton")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scaleAnimation"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .scale(pulseScale)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = RPGHudGray),
            border = BorderStroke(3.dp, RPGGold),
            shape = RoundedCornerShape(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp, horizontal = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Abrir configuración",
                        tint = RPGGold,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Ajustes",
                        color = RPGGold,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}