package fr.ateastudio.plagueandpain.config

import xyz.xenondevs.nova.config.Configs
import xyz.xenondevs.nova.config.entry

object OpenWoundConfig {
    val Chance : Double
        get() = Configs["plagueandpain:config"].entry<Double>("injury","open_wound", "chance").get()
    
}