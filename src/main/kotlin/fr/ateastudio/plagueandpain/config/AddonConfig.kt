package fr.ateastudio.plagueandpain.config

import xyz.xenondevs.nova.config.Configs
import xyz.xenondevs.nova.config.entry

object AddonConfig {
    private val config = Configs["plagueandpain:config"]
    private val bandageReliefEntry = config.entry<Double>("injury", "bandage_relief")
    private val medicineReliefEntry = config.entry<Double>("disease", "medicine_relief")
    private val diseaseImmunityChanceReductionPerHealEntry = config.entry<Double>("disease", "immunity_chance_reduction_per_heal")
    
    val bandageRelief: Double
        get() = bandageReliefEntry.get()
    
    val medicineRelief: Double
        get() = medicineReliefEntry.get()
    
    val diseaseImmunityChanceReductionPerHeal: Double
        get() = diseaseImmunityChanceReductionPerHealEntry.get()
}
