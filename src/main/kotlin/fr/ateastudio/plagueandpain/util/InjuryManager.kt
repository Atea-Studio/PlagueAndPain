package fr.ateastudio.plagueandpain.util

import fr.ateastudio.plagueandpain.Injury
import fr.ateastudio.plagueandpain.PlagueAndPain
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType
import xyz.xenondevs.nova.util.NamespacedKey

object  InjuryManager {
    private val KEY_INJURY_TYPE = NamespacedKey(PlagueAndPain, "injury_type")
    private val KEY_INJURY_PROGRESS = NamespacedKey(PlagueAndPain, "injury_progress")
    
    fun setInjury(player: Player, injuryType: Injury, progression: Double) {
        val data = player.persistentDataContainer
        data.set(KEY_INJURY_TYPE, PersistentDataType.STRING, injuryType.name)
        data.set(KEY_INJURY_PROGRESS, PersistentDataType.DOUBLE, progression)
    }
    
    fun getInjuryType(player: Player): Injury? {
        return Injury.fromTag(player.persistentDataContainer.get(KEY_INJURY_TYPE, PersistentDataType.STRING))
    }
    
    fun getInjuryProgress(player: Player): Double {
        return player.persistentDataContainer.get(KEY_INJURY_PROGRESS, PersistentDataType.DOUBLE) ?: 0.0
    }
    
    fun setInjuryProgress(player: Player, progress: Double) {
        val data = player.persistentDataContainer
        data.set(KEY_INJURY_PROGRESS, PersistentDataType.DOUBLE, progress)
    }
    
    fun addInjuryProgress(player: Player, progress: Double) {
        val newValue = getInjuryProgress(player) + progress
        setInjuryProgress(player, newValue)
    }
    
    fun hasInjury(player: Player): Boolean {
        return player.persistentDataContainer.has(KEY_INJURY_TYPE, PersistentDataType.STRING)
    }
    
    fun clearInjury(player: Player) {
        val data = player.persistentDataContainer
        data.remove(KEY_INJURY_TYPE)
        data.remove(KEY_INJURY_PROGRESS)
    }
}
