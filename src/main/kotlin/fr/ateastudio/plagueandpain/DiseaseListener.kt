package fr.ateastudio.plagueandpain

import fr.ateastudio.plagueandpain.config.BrokenLegConfig
import fr.ateastudio.plagueandpain.config.OpenWoundConfig
import fr.ateastudio.plagueandpain.registry.Items
import fr.ateastudio.plagueandpain.util.InjuryManager
import fr.ateastudio.plagueandpain.util.getItemId
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.inventory.ItemStack
import xyz.xenondevs.nova.config.Configs
import xyz.xenondevs.nova.config.entry
import xyz.xenondevs.nova.config.optionalEntry
import xyz.xenondevs.nova.initialize.Init
import xyz.xenondevs.nova.initialize.InitStage
import xyz.xenondevs.nova.util.item.ItemUtils
import xyz.xenondevs.nova.util.item.novaItem
import xyz.xenondevs.nova.util.playSoundNearby
import xyz.xenondevs.nova.util.registerEvents
import kotlin.random.Random

@Init(stage = InitStage.POST_WORLD)
object DiseaseListener: Listener {
    init {
        this.registerEvents()
    }
    
    @EventHandler
    private fun onConsume(event: PlayerItemConsumeEvent) {
        if (event.item.novaItem == Items.MEDICINE) {
            InjuryManager.clearInjury(event.player)
        }
    }
}