package fr.ateastudio.plagueandpain

import fr.ateastudio.plagueandpain.config.BrokenLegConfig
import fr.ateastudio.plagueandpain.config.OpenWoundConfig
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
import xyz.xenondevs.nova.util.playSoundNearby
import xyz.xenondevs.nova.util.registerEvents
import kotlin.random.Random

@Init(stage = InitStage.POST_WORLD)
object InjuryListener: Listener {
    init {
        this.registerEvents()
    }
    
    @EventHandler
    private fun onFallDamage(event: EntityDamageEvent) {
        val player = event.entity as? Player ?: return
        if (event.isCancelled || event.cause != EntityDamageEvent.DamageCause.FALL || !InjuryManager.canGetInjury(player, Injury.BROKEN_LEG)) return
        
        if (event.damage > BrokenLegConfig.FallDamageThreshold && player.fallDistance > BrokenLegConfig.FallHeightThreshold) {
            if (Random.nextDouble(100.0) < BrokenLegConfig.Chance) {
                player.sendMessage("You broke your leg!") //TODO replace with translation
                player.location.playSoundNearby(Sound.ENTITY_PLAYER_BIG_FALL, 1.0F, 2.0F)
                InjuryManager.setInjury(player, Injury.BROKEN_LEG,0.0)
            }
        }
    }
    
    @EventHandler
    private fun onCutDamage(event: EntityDamageByEntityEvent) {
        val player = event.entity as? Player ?: return
        val attacker = event.damager as? LivingEntity ?: return
        val weapon = attacker.equipment?.itemInMainHand ?: return
        val isValidWeapon = weapon.getItemId().endsWith("_sword", true) ||
            weapon.getItemId().endsWith("_axe", true) ||
            weapon.getItemId().endsWith("_katana", true) ||
            weapon.getItemId().endsWith("_knife", true)
        
        if (event.isCancelled || player.isBlocking || !isValidWeapon || !InjuryManager.canGetInjury(player, Injury.OPEN_WOUND)) return
        
        if (Random.nextDouble(100.0) < OpenWoundConfig.Chance) {
            player.sendMessage("You have an open wound!") //TODO replace with translation
            player.location.playSoundNearby(Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1.0F, 2.0F)
            InjuryManager.setInjury(player, Injury.OPEN_WOUND,0.0)
        }
    }
    
    @EventHandler
    private fun onConsume(event: PlayerItemConsumeEvent) {
        //TODO change with bandage
        if (event.item.type == Material.MILK_BUCKET) {
            InjuryManager.clearInjury(event.player)
        }
    }
}