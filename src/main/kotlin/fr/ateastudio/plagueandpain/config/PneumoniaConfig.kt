package fr.ateastudio.plagueandpain.config

import xyz.xenondevs.nova.config.Configs
import xyz.xenondevs.nova.config.entry

object PneumoniaConfig {
    private val config = Configs["plagueandpain:pneumonia"]
    private val progressPerTickEntry = config.entry<Double>("progress_per_tick")
    private val slownessAmplifierEntry = config.entry<Int>("slowness_amplifier")
    private val weaknessAmplifierEntry = config.entry<Int>("weakness_amplifier")
    private val damageIntervalEntry = config.entry<Int>("damage_interval_ticks")
    private val damagePerPulseEntry = config.entry<Double>("damage_per_pulse")
    private val heatProgressMultiplierEntry = config.entry<Double>("heat_progress_multiplier")
    
    val progressPerTick: Double
        get() = progressPerTickEntry.get()
    
    val slownessAmplifier: Int
        get() = slownessAmplifierEntry.get()
    
    val weaknessAmplifier: Int
        get() = weaknessAmplifierEntry.get()
    
    val damageIntervalTicks: Int
        get() = damageIntervalEntry.get()
    
    val damagePerPulse: Double
        get() = damagePerPulseEntry.get()
    
    val heatProgressMultiplier: Double
        get() = heatProgressMultiplierEntry.get()
}
