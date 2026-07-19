package fr.ateastudio.plagueandpain

import fr.ateastudio.plagueandpain.config.BrokenLegConfig
import fr.ateastudio.plagueandpain.config.OpenWoundConfig
import fr.ateastudio.plagueandpain.registry.Items
import fr.ateastudio.plagueandpain.service.ConditionService
import fr.ateastudio.plagueandpain.util.getItemId
import org.bukkit.Sound
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import xyz.xenondevs.nova.initialize.Init
import xyz.xenondevs.nova.initialize.InitStage
import xyz.xenondevs.nova.util.item.novaItem
import xyz.xenondevs.nova.util.playSoundNearby
import xyz.xenondevs.nova.util.registerEvents

@Init(stage = InitStage.POST_WORLD)
object InjuryListener : Listener {
    init {
        this.registerEvents()
    }
    
    @EventHandler(ignoreCancelled = true)
    private fun onFallDamage(event: EntityDamageEvent) {
        val player = event.entity as? Player ?: return
        if (event.cause != EntityDamageEvent.DamageCause.FALL) {
            return
        }
        
        if (event.finalDamage > BrokenLegConfig.fallDamageThreshold &&
            player.fallDistance > BrokenLegConfig.fallHeightThreshold &&
            ConditionService.tryApplyInjury(
                player = player,
                injury = Injury.BROKEN_LEG,
                chance = BrokenLegConfig.chance,
                message = "You broke your leg."
            )
        ) {
            player.location.playSoundNearby(Sound.ENTITY_PLAYER_BIG_FALL, 1.0F, 2.0F)
        }
    }
    
    @EventHandler(ignoreCancelled = true)
    private fun onCutDamage(event: EntityDamageByEntityEvent) {
        val player = event.entity as? Player ?: return
        val attacker = event.damager as? LivingEntity ?: return
        val weapon = attacker.equipment?.itemInMainHand ?: return
        val isValidWeapon = weapon.getItemId().endsWith("_sword", true) ||
            weapon.getItemId().endsWith("_axe", true) ||
            weapon.getItemId().endsWith("_katana", true) ||
            weapon.getItemId().endsWith("_knife", true)
        
        if (player.isBlocking || !isValidWeapon || event.finalDamage <= 0.0) {
            return
        }
        
        if (ConditionService.tryApplyInjury(
                player = player,
                injury = Injury.OPEN_WOUND,
                chance = OpenWoundConfig.chance,
                message = "You suffered an open wound."
            )
        ) {
            player.location.playSoundNearby(Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1.0F, 2.0F)
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private fun onConsume(event: PlayerItemConsumeEvent) {
        if (event.item.novaItem == Items.BANDAGE) {
            ConditionService.useBandage(event.player)
        }
    }
}
