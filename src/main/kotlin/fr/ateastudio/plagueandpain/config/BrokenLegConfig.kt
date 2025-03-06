package fr.ateastudio.plagueandpain.config

import xyz.xenondevs.nova.config.Configs
import xyz.xenondevs.nova.config.entry

object BrokenLegConfig {
    val Chance : Double
        get() = Configs["plagueandpain:config"].entry<Double>("injury","broken_leg", "chance").get()
    
    val FallDamageThreshold : Double
        get() = Configs["plagueandpain:config"].entry<Double>("injury","broken_leg", "fall_damage_threshold").get()
    
    val FallHeightThreshold : Double
        get() = Configs["plagueandpain:config"].entry<Double>("injury","broken_leg", "fall_height_threshold").get()
    
}