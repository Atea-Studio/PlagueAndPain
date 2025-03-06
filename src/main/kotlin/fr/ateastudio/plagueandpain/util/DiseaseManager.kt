package fr.ateastudio.plagueandpain.util

import fr.ateastudio.plagueandpain.Disease
import fr.ateastudio.plagueandpain.Injury
import fr.ateastudio.plagueandpain.PlagueAndPain
import fr.ateastudio.plagueandpain.util.InjuryManager.hasInjury
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType
import xyz.xenondevs.nova.config.Configs
import xyz.xenondevs.nova.config.optionalEntry
import xyz.xenondevs.nova.util.NamespacedKey
import xyz.xenondevs.nova.util.item.ItemUtils

object  DiseaseManager {
    private val KEY_DISEASE_TYPE = NamespacedKey(PlagueAndPain, "disease_type")
    private val KEY_DISEASE_PROGRESS = NamespacedKey(PlagueAndPain, "disease_progress")
    private val ITEMS_FOR_IMMUNITY = Configs["plagueandpain:config"].optionalEntry<List<String>>("injury", "armor_pieces_for_immunity")
    
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
    
    fun canGetDisease(player: Player, injuryType: Injury): Boolean {
        var hasArmorImmunity = false
        val items = ITEMS_FOR_IMMUNITY.get()?.map { ItemUtils.getItemStack(it) }
        if (items != null) {
            for (item in items) {
                hasArmorImmunity = player.inventory.armorContents.any { it?.isSimilar(item) ?: false }
                if (!hasArmorImmunity) {
                    break
                }
            }
        }
        return !hasArmorImmunity && !hasInjury(player)
    }
    
    fun clearDisease(player: Player) {
        val data = player.persistentDataContainer
        data.remove(KEY_DISEASE_TYPE)
        data.remove(KEY_DISEASE_PROGRESS)
    }
}