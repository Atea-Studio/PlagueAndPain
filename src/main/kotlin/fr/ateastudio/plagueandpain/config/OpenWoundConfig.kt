package fr.ateastudio.plagueandpain.config

import fr.ateastudio.plagueandpain.util.ConditionSeverity
import xyz.xenondevs.nova.config.Configs
import xyz.xenondevs.nova.config.entry

object OpenWoundConfig {
    private val config = Configs["plagueandpain:open_wound"]
    private val chanceEntry = config.entry<Double>("chance")
    private val progressPerTickEntry = config.entry<Double>("progress_per_tick")
    private val bleedIntervalEntry = config.entry<Int>("bleed_interval_ticks")
    private val mildDamageEntry = config.entry<Double>("bleed_damage", "mild")
    private val moderateDamageEntry = config.entry<Double>("bleed_damage", "moderate")
    private val severeDamageEntry = config.entry<Double>("bleed_damage", "severe")
    private val criticalDamageEntry = config.entry<Double>("bleed_damage", "critical")
    private val infectionThresholdEntry = config.entry<Double>("infection_threshold")
    private val feverInfectionChanceEntry = config.entry<Double>("fever_infection_chance")
    
    val chance: Double
        get() = chanceEntry.get()
    
    val progressPerTick: Double
        get() = progressPerTickEntry.get()
    
    val bleedIntervalTicks: Int
        get() = bleedIntervalEntry.get()
    
    val infectionThreshold: Double
        get() = infectionThresholdEntry.get()
    
    val feverInfectionChance: Double
        get() = feverInfectionChanceEntry.get()
    
    fun bleedDamage(progress: Double): Double {
        return when (ConditionSeverity.fromProgress(progress)) {
            ConditionSeverity.MILD -> mildDamageEntry.get()
            ConditionSeverity.MODERATE -> moderateDamageEntry.get()
            ConditionSeverity.SEVERE -> severeDamageEntry.get()
            ConditionSeverity.CRITICAL -> criticalDamageEntry.get()
        }
    }
}