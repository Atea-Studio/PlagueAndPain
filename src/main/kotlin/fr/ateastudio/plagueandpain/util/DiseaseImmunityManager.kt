package fr.ateastudio.plagueandpain.util

import fr.ateastudio.plagueandpain.Disease
import fr.ateastudio.plagueandpain.PlagueAndPain
import fr.ateastudio.plagueandpain.config.AddonConfig
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType
import xyz.xenondevs.nova.util.NamespacedKey

object DiseaseImmunityManager {
    
    fun registerSuccessfulHeal(player: Player, disease: Disease): Int {
        val key = healCountKey(disease)
        val current = player.persistentDataContainer.get(key, PersistentDataType.INTEGER) ?: 0
        val updated = (current + 1).coerceAtLeast(0)
        player.persistentDataContainer.set(key, PersistentDataType.INTEGER, updated)
        return updated
    }
    
    fun adjustedInfectionChance(player: Player, disease: Disease, baseChance: Double): Double {
        val heals = healCount(player, disease)
        val reducedChance = baseChance - (heals * AddonConfig.diseaseImmunityChanceReductionPerHeal)
        return reducedChance.coerceIn(0.0, 100.0)
    }
    
    private fun healCount(player: Player, disease: Disease): Int {
        return player.persistentDataContainer.get(healCountKey(disease), PersistentDataType.INTEGER) ?: 0
    }
    
    private fun healCountKey(disease: Disease) = NamespacedKey(PlagueAndPain, "disease_immunity_heals_${disease.tag}")
}
