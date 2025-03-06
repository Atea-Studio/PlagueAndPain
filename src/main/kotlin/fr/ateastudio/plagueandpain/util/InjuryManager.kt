package fr.ateastudio.plagueandpain.util

import fr.ateastudio.plagueandpain.Injury
import fr.ateastudio.plagueandpain.PlagueAndPain
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType
import xyz.xenondevs.nova.config.Configs
import xyz.xenondevs.nova.config.optionalEntry
import xyz.xenondevs.nova.util.NamespacedKey
import xyz.xenondevs.nova.util.item.ItemUtils

object  InjuryManager {
    private val KEY_INJURY_TYPE = NamespacedKey(PlagueAndPain, "injury_type")
    private val KEY_INJURY_PROGRESS = NamespacedKey(PlagueAndPain, "injury_progress")
    private val ITEMS_FOR_IMMUNITY = Configs["plagueandpain:config"].optionalEntry<List<String>>("injury", "armor_pieces_for_immunity")
    
    fun setInjury(player: Player, injuryType: Injury, progression: Double) : Boolean {
        val data = player.persistentDataContainer
        data.set(KEY_INJURY_TYPE, PersistentDataType.STRING, injuryType.name)
        data.set(KEY_INJURY_PROGRESS, PersistentDataType.DOUBLE, progression)
        return true
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
    
    fun canGetInjury(player: Player, injuryType: Injury): Boolean {
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
    
    fun clearInjury(player: Player) {
        val data = player.persistentDataContainer
        data.remove(KEY_INJURY_TYPE)
        data.remove(KEY_INJURY_PROGRESS)
    }
}
