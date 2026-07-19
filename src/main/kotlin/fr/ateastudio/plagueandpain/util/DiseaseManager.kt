package fr.ateastudio.plagueandpain.util

import fr.ateastudio.plagueandpain.Disease
import fr.ateastudio.plagueandpain.PlagueAndPain

object DiseaseManager : PlayerConditionManager<Disease>(
    addon = PlagueAndPain,
    typeKey = "disease_type",
    progressKey = "disease_progress",
    immunityConfigSection = "disease",
    resolver = Disease::fromStoredValue
)