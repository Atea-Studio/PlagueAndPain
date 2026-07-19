package fr.ateastudio.plagueandpain.config

import xyz.xenondevs.nova.config.Configs
import xyz.xenondevs.nova.config.entry

object FeverConfig {
    private val config = Configs["plagueandpain:fever"]
    private val progressPerTickEntry = config.entry<Double>("progress_per_tick")
    private val rottenFleshChanceEntry = config.entry<Double>("rotten_flesh_chance")
    private val spiderEyeChanceEntry = config.entry<Double>("spider_eye_chance")
    private val weaknessAmplifierEntry = config.entry<Int>("weakness_amplifier")
    private val hungerAmplifierEntry = config.entry<Int>("hunger_amplifier")
    private val damageIntervalEntry = config.entry<Int>("damage_interval_ticks")
    private val damagePerPulseEntry = config.entry<Double>("damage_per_pulse")
    
    val progressPerTick: Double
        get() = progressPerTickEntry.get()
    
    val rottenFleshChance: Double
        get() = rottenFleshChanceEntry.get()
    
    val spiderEyeChance: Double
        get() = spiderEyeChanceEntry.get()
    
    val weaknessAmplifier: Int
        get() = weaknessAmplifierEntry.get()
    
    val hungerAmplifier: Int
        get() = hungerAmplifierEntry.get()
    
    val damageIntervalTicks: Int
        get() = damageIntervalEntry.get()
    
    val damagePerPulse: Double
        get() = damagePerPulseEntry.get()
}
