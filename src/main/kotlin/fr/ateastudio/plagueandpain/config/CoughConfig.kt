package fr.ateastudio.plagueandpain.config

import xyz.xenondevs.nova.config.Configs
import xyz.xenondevs.nova.config.entry

object CoughConfig {
    private val config = Configs["plagueandpain:cough"]
    private val progressPerTickEntry = config.entry<Double>("progress_per_tick")
    private val exposureCheckIntervalEntry = config.entry<Int>("exposure_check_interval_ticks")
    private val rainExposureChanceEntry = config.entry<Double>("rain_exposure_chance")
    private val waterExposureChanceEntry = config.entry<Double>("water_exposure_chance")
    private val slownessAmplifierEntry = config.entry<Int>("slowness_amplifier")
    private val pneumoniaThresholdEntry = config.entry<Double>("pneumonia_threshold")
    private val pneumoniaStartingProgressEntry = config.entry<Double>("pneumonia_starting_progress")
    
    val progressPerTick: Double
        get() = progressPerTickEntry.get()
    
    val exposureCheckIntervalTicks: Int
        get() = exposureCheckIntervalEntry.get()
    
    val rainExposureChance: Double
        get() = rainExposureChanceEntry.get()
    
    val waterExposureChance: Double
        get() = waterExposureChanceEntry.get()
    
    val slownessAmplifier: Int
        get() = slownessAmplifierEntry.get()
    
    val pneumoniaThreshold: Double
        get() = pneumoniaThresholdEntry.get()
    
    val pneumoniaStartingProgress: Double
        get() = pneumoniaStartingProgressEntry.get()
}
