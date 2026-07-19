package fr.ateastudio.plagueandpain.config

import xyz.xenondevs.nova.config.Configs
import xyz.xenondevs.nova.config.entry

object PlagueConfig {
    private val config = Configs["plagueandpain:plague"]
    private val progressPerTickEntry = config.entry<Double>("progress_per_tick")
    private val undeadBiteChanceEntry = config.entry<Double>("undead_bite_chance")
    private val poisonAmplifierEntry = config.entry<Int>("poison_amplifier")
    private val weaknessAmplifierEntry = config.entry<Int>("weakness_amplifier")
    private val damageIntervalEntry = config.entry<Int>("damage_interval_ticks")
    private val damagePerPulseEntry = config.entry<Double>("damage_per_pulse")
    
    val progressPerTick: Double
        get() = progressPerTickEntry.get()
    
    val undeadBiteChance: Double
        get() = undeadBiteChanceEntry.get()
    
    val poisonAmplifier: Int
        get() = poisonAmplifierEntry.get()
    
    val weaknessAmplifier: Int
        get() = weaknessAmplifierEntry.get()
    
    val damageIntervalTicks: Int
        get() = damageIntervalEntry.get()
    
    val damagePerPulse: Double
        get() = damagePerPulseEntry.get()
}
