package fr.ateastudio.plagueandpain

import fr.ateastudio.plagueandpain.config.FeverConfig
import fr.ateastudio.plagueandpain.registry.Items
import fr.ateastudio.plagueandpain.service.ConditionService
import fr.ateastudio.plagueandpain.util.DiseaseManager
import fr.ateastudio.plagueandpain.util.InjuryManager
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.inventory.EquipmentSlot
import xyz.xenondevs.nova.initialize.Init
import xyz.xenondevs.nova.initialize.InitStage
import xyz.xenondevs.nova.util.item.novaItem
import xyz.xenondevs.nova.util.registerEvents

@Init(stage = InitStage.POST_WORLD)
object DiseaseListener : Listener {
    init {
        this.registerEvents()
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private fun onConsume(event: PlayerItemConsumeEvent) {
        if (event.item.novaItem == Items.MEDICINE) {
            ConditionService.useMedicine(event.player)
            return
        }
        
        when (event.item.type) {
            Material.ROTTEN_FLESH -> ConditionService.tryApplyDisease(
                player = event.player,
                disease = Disease.FEVER,
                chance = FeverConfig.rottenFleshChance,
                message = Component.translatable("message.plagueandpain.food.rotten_flesh_fever")
            )
            Material.SPIDER_EYE -> ConditionService.tryApplyDisease(
                player = event.player,
                disease = Disease.FEVER,
                chance = FeverConfig.spiderEyeChance,
                message = Component.translatable("message.plagueandpain.food.spider_eye_fever")
            )
            else -> Unit
        }
    }
    
    @EventHandler(ignoreCancelled = true)
    private fun onDamage(event: EntityDamageByEntityEvent) {
        val player = event.entity as? Player ?: return
        if (event.finalDamage <= 0.0) {
            return
        }
        
        ConditionService.handleMobDiseaseHit(player, event.damager)
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private fun onInteractEntity(event: PlayerInteractEntityEvent) {
        if (event.hand != EquipmentSlot.HAND) {
            return
        }
        
        val target = event.rightClicked as? Player ?: return
        when (event.player.inventory.itemInMainHand.novaItem) {
            Items.SYRINGE -> ConditionService.sampleBlood(event.player, target)
            Items.BLOOD_SYRINGE -> ConditionService.injectBlood(event.player, target)
            else -> Unit
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    private fun onDeath(event: PlayerDeathEvent) {
        DiseaseManager.clear(event.player)
        InjuryManager.clear(event.player)
    }
}
