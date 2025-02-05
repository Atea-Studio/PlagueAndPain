package fr.ateastudio.plagueandpain.util

import fr.ateastudio.plagueandpain.Disease
import fr.ateastudio.plagueandpain.PlagueAndPain
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType
import xyz.xenondevs.nova.util.NamespacedKey

object  DiseaseManager {
    private val KEY_DISEASE_TYPE = NamespacedKey(PlagueAndPain, "disease_type")
    private val KEY_DISEASE_PROGRESS = NamespacedKey(PlagueAndPain, "disease_progress")
    
    fun setDisease(player: Player, diseaseType: Disease, progression: Double) {
        val data = player.persistentDataContainer
        data.set(KEY_DISEASE_TYPE, PersistentDataType.STRING, diseaseType.name)
        data.set(KEY_DISEASE_PROGRESS, PersistentDataType.DOUBLE, progression)
    }
    
    fun getDiseaseType(player: Player): Disease? {
        return Disease.fromTag(player.persistentDataContainer.get(KEY_DISEASE_TYPE, PersistentDataType.STRING))
    }
    
    fun getDiseaseProgress(player: Player): Double {
        return player.persistentDataContainer.get(KEY_DISEASE_PROGRESS, PersistentDataType.DOUBLE) ?: 0.0
    }
    
    fun setDiseaseProgress(player: Player, progress: Double) {
        val data = player.persistentDataContainer
        data.set(KEY_DISEASE_PROGRESS, PersistentDataType.DOUBLE, progress)
    }
    
    fun addDiseaseProgress(player: Player, progress: Double) {
        val newValue = getDiseaseProgress(player) + progress
        setDiseaseProgress(player, newValue)
    }
    
    fun hasDisease(player: Player): Boolean {
        return player.persistentDataContainer.has(KEY_DISEASE_TYPE, PersistentDataType.STRING)
    }
    
    fun clearDisease(player: Player) {
        val data = player.persistentDataContainer
        data.remove(KEY_DISEASE_TYPE)
        data.remove(KEY_DISEASE_PROGRESS)
    }
}