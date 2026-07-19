package fr.ateastudio.plagueandpain.config

import xyz.xenondevs.nova.config.Configs
import xyz.xenondevs.nova.config.entry

object RabiesConfig {
    private val config = Configs["plagueandpain:rabies"]
    private val progressPerTickEntry = config.entry<Double>("progress_per_tick")
    private val animalBiteChanceEntry = config.entry<Double>("animal_bite_chance")
    private val nauseaAmplifierEntry = config.entry<Int>("nausea_amplifier")
    private val weaknessAmplifierEntry = config.entry<Int>("weakness_amplifier")
    private val damageIntervalEntry = config.entry<Int>("damage_interval_ticks")
    private val damagePerPulseEntry = config.entry<Double>("damage_per_pulse")
    
    val progressPerTick: Double
        get() = progressPerTickEntry.get()
    
    val animalBiteChance: Double
        get() = animalBiteChanceEntry.get()
    
    val nauseaAmplifier: Int
        get() = nauseaAmplifierEntry.get()
    
    val weaknessAmplifier: Int
        get() = weaknessAmplifierEntry.get()
    
    val damageIntervalTicks: Int
        get() = damageIntervalEntry.get()
    
    val damagePerPulse: Double
        get() = damagePerPulseEntry.get()
}
