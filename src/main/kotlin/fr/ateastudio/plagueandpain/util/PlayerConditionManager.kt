package fr.ateastudio.plagueandpain.util

import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType
import xyz.xenondevs.nova.addon.Addon
import xyz.xenondevs.nova.config.Configs
import xyz.xenondevs.nova.config.optionalEntry
import xyz.xenondevs.nova.util.NamespacedKey
import xyz.xenondevs.nova.util.item.ItemUtils

interface TaggedCondition {
    val tag: String
}

abstract class PlayerConditionManager<T>(
    addon: Addon,
    typeKey: String,
    progressKey: String,
    immunityConfigSection: String,
    private val resolver: (String?) -> T?
) where T : Enum<T>, T : TaggedCondition {
    
    private val typeDataKey = NamespacedKey(addon, typeKey)
    private val progressDataKey = NamespacedKey(addon, progressKey)
    private val immunityItems = Configs["plagueandpain:config"].optionalEntry<List<String>>(immunityConfigSection, "armor_pieces_for_immunity")
    
    fun setCondition(player: Player, type: T, progress: Double = 0.0) {
        val data = player.persistentDataContainer
        data.set(typeDataKey, PersistentDataType.STRING, type.tag)
        data.set(progressDataKey, PersistentDataType.DOUBLE, progress.coerceIn(0.0, MAX_PROGRESS))
    }
    
    fun getType(player: Player): T? {
        return resolver(player.persistentDataContainer.get(typeDataKey, PersistentDataType.STRING))
    }
    
    fun getProgress(player: Player): Double {
        return player.persistentDataContainer.get(progressDataKey, PersistentDataType.DOUBLE) ?: 0.0
    }
    
    fun hasCondition(player: Player): Boolean {
        return getType(player) != null
    }
    
    fun canAcquire(player: Player): Boolean {
        return !hasCondition(player) && !hasArmorImmunity(player) && (player.gameMode == GameMode.SURVIVAL || player.gameMode == GameMode.ADVENTURE)
    }
    
    fun setProgress(player: Player, progress: Double): Double {
        val clampedProgress = progress.coerceIn(0.0, MAX_PROGRESS)
        player.persistentDataContainer.set(progressDataKey, PersistentDataType.DOUBLE, clampedProgress)
        return clampedProgress
    }
    
    fun addProgress(player: Player, progress: Double): Double {
        return setProgress(player, getProgress(player) + progress)
    }
    
    fun reduceProgress(player: Player, amount: Double): Double {
        val updatedProgress = (getProgress(player) - amount).coerceAtLeast(0.0)
        if (updatedProgress <= 0.0) {
            clear(player)
        } else {
            setProgress(player, updatedProgress)
        }
        
        return updatedProgress
    }
    
    fun clear(player: Player) {
        val data = player.persistentDataContainer
        data.remove(typeDataKey)
        data.remove(progressDataKey)
    }
    
    private fun hasArmorImmunity(player: Player): Boolean {
        val configuredItems = immunityItems.get().orEmpty()
            .mapNotNull { itemId -> runCatching { ItemUtils.getItemStack(itemId) }.getOrNull() }
        
        if (configuredItems.isEmpty()) {
            return false
        }
        
        return configuredItems.all { requiredItem ->
            player.inventory.armorContents.any { equippedItem ->
                equippedItem?.isSimilar(requiredItem) == true
            }
        }
    }
    
    private companion object {
        const val MAX_PROGRESS = 100.0
    }
}
