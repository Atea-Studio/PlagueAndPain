package fr.ateastudio.plagueandpain.util

import fr.ateastudio.plagueandpain.Injury
import fr.ateastudio.plagueandpain.PlagueAndPain

object InjuryManager : PlayerConditionManager<Injury>(
    addon = PlagueAndPain,
    typeKey = "injury_type",
    progressKey = "injury_progress",
    immunityConfigSection = "injury",
    resolver = Injury::fromStoredValue
)
