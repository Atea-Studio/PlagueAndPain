package fr.ateastudio.plagueandpain.config

import fr.ateastudio.plagueandpain.util.ConditionSeverity
import xyz.xenondevs.nova.config.Configs
import xyz.xenondevs.nova.config.entry

object BrokenLegConfig {
    private val config = Configs["plagueandpain:broken_leg"]
    private val chanceEntry = config.entry<Double>("chance")
    private val fallDamageThresholdEntry = config.entry<Double>("fall_damage_threshold")
    private val fallHeightThresholdEntry = config.entry<Double>("fall_height_threshold")
    private val progressPerTickEntry = config.entry<Double>("progress_per_tick")
    private val mildSlownessEntry = config.entry<Int>("slowness", "mild_amplifier")
    private val moderateSlownessEntry = config.entry<Int>("slowness", "moderate_amplifier")
    private val severeSlownessEntry = config.entry<Int>("slowness", "severe_amplifier")
    private val criticalSlownessEntry = config.entry<Int>("slowness", "critical_amplifier")
    
    val chance: Double
        get() = chanceEntry.get()
    
    val fallDamageThreshold: Double
        get() = fallDamageThresholdEntry.get()
    
    val fallHeightThreshold: Double
        get() = fallHeightThresholdEntry.get()
    
    val progressPerTick: Double
        get() = progressPerTickEntry.get()
    
    fun slownessAmplifier(progress: Double): Int {
        return when (ConditionSeverity.fromProgress(progress)) {
            ConditionSeverity.MILD -> mildSlownessEntry.get()
            ConditionSeverity.MODERATE -> moderateSlownessEntry.get()
            ConditionSeverity.SEVERE -> severeSlownessEntry.get()
            ConditionSeverity.CRITICAL -> criticalSlownessEntry.get()
        }
    }
}