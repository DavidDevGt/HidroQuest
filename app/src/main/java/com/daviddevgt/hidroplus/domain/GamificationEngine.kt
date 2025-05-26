package com.daviddevgt.hidroplus.domain

import androidx.compose.animation.core.copy
import kotlin.compareTo

data class PlayerStats(
    val level: Int,
    val exp: Int,
    val maxExp: Int,
    val hp: Int,
    val maxHp: Int,
    val attack: Int,
    val defense: Int,
    val magic: Int
)

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val icon: String,
    val rarity: AchievementRarity,
    val unlocked: Boolean = false
)

enum class AchievementRarity {
    COMMON, RARE, EPIC, LEGENDARY, MYTHIC
}
data class RangoInfo(
    val umbralVasos: Int,
    val nombreRango: String,
    val statsBase: PlayerStats
)

class GamificationEngine {

    private val rangosDelReino: List<RangoInfo> = listOf(
        RangoInfo(umbralVasos = 0, nombreRango = "🐣 Novato del Agua", statsBase = PlayerStats(1, 0, 100, 50, 50, 5, 3, 1)),
        RangoInfo(umbralVasos = 4, nombreRango = "⚔️ Guerrero Hidráulico", statsBase = PlayerStats(2, 0, 150, 75, 75, 8, 6, 3)),
        RangoInfo(umbralVasos = 8, nombreRango = "🔥 Señor de la Cascada", statsBase = PlayerStats(3, 0, 200, 100, 100, 12, 10, 5)),
        RangoInfo(umbralVasos = 12, nombreRango = "🌊 Maestro Acuático", statsBase = PlayerStats(4, 0, 300, 150, 150, 18, 15, 8)),
        RangoInfo(umbralVasos = 16, nombreRango = "💎 Poseidón Moderno", statsBase = PlayerStats(5, 0, 500, 200, 200, 25, 20, 12)),
        RangoInfo(umbralVasos = 20, nombreRango = "🌟 Dios del Agua Eterna", statsBase = PlayerStats(6, 0, 750, 300, 300, 35, 30, 18))
    )

    private val achievements = mutableListOf(
        Achievement("first_drop", "Primera Gota", "Bebe tu primer vaso de agua", "💧", AchievementRarity.COMMON),
        Achievement("daily_goal", "Misión Diaria", "Completa tu meta diaria", "🎯", AchievementRarity.COMMON),
        Achievement("overachiever", "Sobrepasando Límites", "Bebe más de tu meta diaria", "🚀", AchievementRarity.RARE),
        Achievement("hydration_master", "Maestro de la Hidratación", "Alcanza 150% de tu meta", "👑", AchievementRarity.EPIC),
        Achievement("week_warrior", "Guerrero Semanal", "7 días consecutivos", "⚔️", AchievementRarity.RARE),
        Achievement("month_legend", "Leyenda del Mes", "30 días consecutivos", "🏆", AchievementRarity.LEGENDARY),
        Achievement("combo_master", "Maestro del Combo", "Alcanza un combo de 10 vasos", "🔥", AchievementRarity.EPIC),
        Achievement("water_god", "Dios del Agua", "Alcanza el nivel máximo", "🌟", AchievementRarity.MYTHIC)
    )

    fun getPlayerStats(vasos: Int): PlayerStats {

        val rangoActual: RangoInfo = rangosDelReino.lastOrNull { rango -> vasos >= rango.umbralVasos }
            ?: rangosDelReino.first() // Si no se encuentra, toma el primer rango (Novato del Agua)

        val baseStats: PlayerStats = rangoActual.statsBase

        val expGained = vasos * 15 + (vasos / 5) * 25 // Bonus cada 5 vasos

        val currentExp = if (baseStats.maxExp > 0) expGained % baseStats.maxExp else 0

        return baseStats.copy(
            exp = currentExp,
            hp = baseStats.maxHp,
            attack = baseStats.attack + (vasos / 3),
            defense = baseStats.defense + (vasos / 4),
            magic = baseStats.magic + (vasos / 6)
        )
    }

    fun desbloquearLogro(vasos: Int, meta: Int): String {
        return when {
            vasos == 0 -> "🌟 ¡LA AVENTURA COMIENZA! Tu destino como Guerrero del Agua te espera..."
            vasos == 1 -> "💧 ¡Primera Victoria! Has bebido tu primer vial de poder. [+15 XP]"
            vasos < meta / 4 -> "✨ ¡Energía Renovada! El agua fluye por tus venas. [+${15 * vasos} XP]"
            vasos < meta / 2 -> "🧪 ¡POCIÓN DOBLE ACTIVADA! Tu poder se duplica. [COMBO x2]"
            vasos >= meta / 2 && vasos < meta -> "💥 ¡HIDRATACIÓN CRÍTICA! El poder del agua te consume. [COMBO x3]"
            vasos == meta -> "🏆 ¡MISIÓN ÉPICA COMPLETADA! Has dominado el arte de la hidratación."
            vasos > meta && vasos <= meta * 1.25 -> "🚀 ¡SOBREPASANDO LÍMITES! Tu sed de poder no conoce barreras."
            vasos > meta * 1.25 && vasos <= meta * 1.5 -> "👑 ¡MAESTRO DE LA HIDRATACIÓN! Los dioses del agua sonríen."
            vasos > meta * 1.5 -> "🌟 ¡LEYENDA VIVIENTE! Has trascendido los límites mortales."
            else -> "⚠️ ¡El desierto de la deshidratación se acerca! ¡Necesitas más PODER ACUÁTICO!"
        }
    }

    fun subirDeNivel(vasos: Int): String {
        return rangosDelReino.lastOrNull { rango -> vasos >= rango.umbralVasos }?.nombreRango
            ?: "🥤 Aprendiz en el Arte de la Hidratación"
    }

    fun gritoDeGuerraDiario(vasos: Int, record: Int, meta: Int): String {
        return when {
            vasos == 0 -> "💀 ¡La Deshidratación Acecha en las Sombras! ¡Prepárate para la batalla!"
            vasos == 1 -> "🛡️ [+15 XP] ¡Primera sangre del día! El enemigo tiembla ante tu poder."
            vasos == record -> "⚔️ ¡Igualaste tu récord legendario! Los bardos cantarán tus hazañas."
            vasos > record -> "🔥 ¡NUEVO RÉCORD ÉPICO: ${vasos} VASOS! [ACHIEVEMENT UNLOCKED: ROMPEDOR DE RÉCORDS]"
            vasos >= meta * 2 -> "🌟 ¡MODO GUERRERO ACTIVADO! Has trascendido las leyes de la hidratación."
            vasos % 5 == 0 && vasos > 0 -> "⚡ ¡COMBO LEGENDARIO DE ${vasos} TURNOS! [MULTIPLICADOR x${vasos/5}]"
            vasos % 3 == 0 && vasos > 0 -> "💫 ¡Triple Combo Activado! Tu poder se triplica por ${vasos} turnos."
            else -> "💪 Vasos conquistados: ${vasos}! Tu poder crece con cada gota. [BUILDING POWER]"
        }
    }

    fun desbloquearHabilidadSecreta(diasConsecutivos: Int): String {
        return when {
            diasConsecutivos >= 365 -> "🌟 ¡MAESTRO ETERNO DEL AGUA! Has alcanzado la inmortalidad acuática."
            diasConsecutivos >= 100 -> "💎 ¡Cristal de Hidratación Eterna! Poder infinito por 100 días."
            diasConsecutivos >= 30 -> "🔱 ¡Tridente de Poseidón! Controlas los océanos por 30 días."
            diasConsecutivos >= 14 -> "🛡️ ¡Escudo Legendario de Agua Celestial! Defensa máxima."
            diasConsecutivos >= 7 -> "⚔️ ¡Espada de los 7 Manantiales Sagrados! Ataque crítico garantizado."
            diasConsecutivos >= 3 -> "✨ ¡Aura de Guerrero Hidratado! Regeneración automática activada."
            else -> ""
        }
    }

    fun getBonusMessage(vasos: Int, meta: Int): String {
        val percentage = (vasos.toFloat() / meta.toFloat() * 100).toInt()
        return when {
            percentage >= 200 -> "🌟 MODO LEGENDARIO: +200% EXP | +50% Stats | Habilidades Especiales Desbloqueadas"
            percentage >= 150 -> "👑 MODO ÉPICO: +150% EXP | +30% Stats | Combo Infinito"
            percentage >= 125 -> "🔥 MODO HÉROE: +125% EXP | +20% Stats | Regeneración Rápida"
            percentage >= 100 -> "⚡ MODO GUERRERO: +100% EXP | +10% Stats | Escudo Activo"
            percentage >= 75 -> "💪 MODO LUCHADOR: +75% EXP | Ataque Mejorado"
            percentage >= 50 -> "🛡️ MODO DEFENSOR: +50% EXP | Defensa Mejorada"
            percentage >= 25 -> "✨ MODO APRENDIZ: +25% EXP | Regeneración Lenta"
            else -> "🥤 MODO NOVATO: EXP Base | Comenzando la aventura..."
        }
    }

    fun getRandomMotivationalQuote(): String {
        val quotes = listOf(
            "💧 'El agua es la fuerza motriz de toda la naturaleza.' - Leonardo da Vinci",
            "🌊 'Sé como el agua: fluye alrededor de los obstáculos.' - Lao Tzu",
            "💎 'En cada gota de agua existe una historia del mundo.' - Proverbio",
            "⚔️ 'El verdadero guerrero conquista sin luchar... pero siempre hidratado.'",
            "🏆 'La hidratación no es solo supervivencia, es poder.'",
            "🌟 'Cada vaso te acerca a la grandeza. ¡Sigue bebiendo!'",
            "🔥 'El fuego del guerrero se alimenta del agua de la sabiduría.'",
            "💪 'Tu cuerpo es tu templo. El agua es tu ofrenda sagrada.'"
        )
        return quotes.random()
    }

    fun checkAchievements(vasos: Int, meta: Int, diasConsecutivos: Int, record: Int): List<Achievement> {
        val unlockedAchievements = mutableListOf<Achievement>()

        achievements.forEach { achievement ->
            val shouldUnlock = when (achievement.id) {
                "first_drop" -> vasos >= 1
                "daily_goal" -> vasos >= meta
                "overachiever" -> vasos > meta
                "hydration_master" -> vasos >= meta * 1.5
                "week_warrior" -> diasConsecutivos >= 7
                "month_legend" -> diasConsecutivos >= 30
                "combo_master" -> vasos >= 10
                "water_god" -> vasos >= 20
                else -> false
            }

            if (shouldUnlock && !achievement.unlocked) {
                unlockedAchievements.add(achievement.copy(unlocked = true))
            }
        }

        return unlockedAchievements
    }

    fun getQuestOfTheDay(vasos: Int, meta: Int): String {
        val progress = vasos.toFloat() / meta.toFloat()
        return when {
            progress == 0f -> "🗡️ MISIÓN PRINCIPAL: Bebe tu primer vaso y despierta tu poder interior."
            progress < 0.25f -> "🏹 MISIÓN ACTIVA: Alcanza el 25% de tu meta para desbloquear nuevas habilidades."
            progress < 0.5f -> "🛡️ MISIÓN CRÍTICA: Llega al 50% para activar tu escudo de hidratación."
            progress < 0.75f -> "⚔️ MISIÓN ÉPICA: El 75% te otorgará el rango de Guerrero Hidráulico."
            progress < 1f -> "👑 MISIÓN LEGENDARIA: ¡Solo te falta poco para completar tu transformación!"
            progress >= 1f && progress < 1.5f -> "🌟 MISIÓN BONUS: Supera tu meta para desbloquear poderes ocultos."
            else -> "💎 MISIÓN DIVINA: Has trascendido las expectativas. Eres una leyenda viviente."
        }
    }

    fun getHealthStatus(vasos: Int, meta: Int): Pair<String, String> {
        val percentage = (vasos.toFloat() / meta.toFloat() * 100).toInt()
        return when {
            percentage >= 150 -> "🌟 SALUD ÓPTIMA" to "Tu cuerpo funciona como una máquina perfecta."
            percentage >= 100 -> "💚 PERFECTA HIDRATACIÓN" to "Estás en tu mejor forma física y mental."
            percentage >= 75 -> "💙 BUENA HIDRATACIÓN" to "Tu energía fluye libremente por todo tu ser."
            percentage >= 50 -> "💛 HIDRATACIÓN MODERADA" to "Vas por buen camino, mantén el ritmo."
            percentage >= 25 -> "🧡 HIDRATACIÓN BAJA" to "Tu cuerpo necesita más agua para funcionar bien."
            else -> "❤️ DESHIDRATACIÓN" to "¡Alerta! Tu cuerpo está pidiendo agua urgentemente."
        }
    }

    fun getDailyChallenge(day: Int): String {
        val challenges = listOf(
            "💪 Desafío del Guerrero: Bebe 2 vasos extra hoy",
            "🏃 Desafío del Velocista: Bebe un vaso cada hora",
            "🧠 Desafío Mental: Bebe antes de cada comida",
            "🌅 Desafío Matutino: Comienza el día con 2 vasos",
            "🌙 Desafío Nocturno: Termina el día hidratado",
            "🎯 Desafío de Precisión: Bebe exactamente tu meta",
            "🔥 Desafío Ardiente: Supera tu récord personal"
        )
        return challenges[day % challenges.size]
    }

    fun getWeatherBonus(weather: String = "normal"): String {
        return when (weather.toLowerCase()) {
            "hot", "calor", "soleado" -> "☀️ BONUS CALOR: +50% EXP por hidratación en clima cálido"
            "cold", "frío", "invierno" -> "❄️ BONUS FRÍO: +25% EXP por mantener la hidratación"
            "rainy", "lluvia" -> "🌧️ BONUS LLUVIA: +30% EXP inspirado por la naturaleza"
            "humid", "húmedo" -> "💨 BONUS HUMEDAD: +40% EXP por compensar la humedad"
            else -> "🌤️ BONUS ESTÁNDAR: +10% EXP por constancia diaria"
        }
    }

    fun getComboMultiplier(consecutiveGlasses: Int): Float {
        return when {
            consecutiveGlasses >= 10 -> 3.0f
            consecutiveGlasses >= 7 -> 2.5f
            consecutiveGlasses >= 5 -> 2.0f
            consecutiveGlasses >= 3 -> 1.5f
            else -> 1.0f
        }
    }

    fun generateEpicSummary(
        vasos: Int,
        meta: Int,
        diasConsecutivos: Int,
        totalVasos: Int = 0
    ): String {
        val playerStats = getPlayerStats(vasos)
        val percentage = (vasos.toFloat() / meta.toFloat() * 100).toInt()

        return buildString {
            appendLine("═══════════════════════════════════")
            appendLine("🏆    REPORTE ÉPICO DEL GUERRERO    🏆")
            appendLine("═══════════════════════════════════")
            appendLine()
            appendLine("👤 NIVEL: ${playerStats.level} | EXP: ${playerStats.exp}/${playerStats.maxExp}")
            appendLine("💧 HIDRATACIÓN HOY: $vasos/$meta vasos ($percentage%)")
            appendLine("🔥 RACHA ACTUAL: $diasConsecutivos días")
            appendLine("⚔️ PODER DE ATAQUE: ${playerStats.attack}")
            appendLine("🛡️ DEFENSA: ${playerStats.defense}")
            appendLine("✨ MAGIA: ${playerStats.magic}")
            appendLine("❤️ PUNTOS DE VIDA: ${playerStats.hp}/${playerStats.maxHp}")
            appendLine()
            appendLine("🎯 ESTADO: ${getHealthStatus(vasos, meta).first}")
            appendLine("📜 QUEST: ${getQuestOfTheDay(vasos, meta)}")
            appendLine()
            appendLine("═══════════════════════════════════")
            appendLine("  ${getRandomMotivationalQuote()}")
            appendLine("═══════════════════════════════════")
        }
    }
}