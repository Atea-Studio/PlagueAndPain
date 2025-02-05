package fr.ateastudio.plagueandpain

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
import xyz.xenondevs.nova.initialize.Init
import xyz.xenondevs.nova.initialize.InitStage
import xyz.xenondevs.nova.util.playSoundNearby
import xyz.xenondevs.nova.util.registerEvents
import kotlin.random.Random

@Init(stage = InitStage.POST_WORLD)
object InjuryListener: Listener {
    init {
        this.registerEvents()
    }
    
    private const val BROKEN_LEG_FALL_DAMAGE_THRESHOLD  = 0.5
    private const val BROKEN_LEG_FALL_HEIGHT_THRESHOLD  = 3.5
    private const val BROKEN_LEG_CHANCE  = 10.0
    
    private const val OPEN_WOUND_CHANCE  = 50.0
    
    @EventHandler
    private fun onFallDamage(event: EntityDamageEvent) {
        val player = event.entity as? Player ?: return
        if (event.isCancelled || event.cause != EntityDamageEvent.DamageCause.FALL || InjuryManager.hasInjury(player)) return
        
        if (event.damage > BROKEN_LEG_FALL_DAMAGE_THRESHOLD && player.fallDistance > BROKEN_LEG_FALL_HEIGHT_THRESHOLD) {
            if (Random.nextDouble(100.0) < BROKEN_LEG_CHANCE) {
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
        
        if (event.isCancelled || player.isBlocking || !isValidWeapon || InjuryManager.hasInjury(player)) return
        
        if (Random.nextDouble(100.0) < OPEN_WOUND_CHANCE) {
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