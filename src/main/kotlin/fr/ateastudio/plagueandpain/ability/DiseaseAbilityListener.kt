package fr.ateastudio.plagueandpain.ability

import fr.ateastudio.plagueandpain.registry.Abilities
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import xyz.xenondevs.nova.initialize.Init
import xyz.xenondevs.nova.initialize.InitStage
import xyz.xenondevs.nova.util.registerEvents
import xyz.xenondevs.nova.world.player.ability.AbilityManager

@Init(stage = InitStage.POST_WORLD)
object DiseaseAbilityListener: Listener {
    init {
        this.registerEvents()
    }
    
    @EventHandler
    private fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        AbilityManager.giveAbility(player, Abilities.DISEASE)
    }
    
    @EventHandler
    private fun onQuit(event: PlayerQuitEvent) {
        val player = event.player
        AbilityManager.takeAbility(player, Abilities.DISEASE)
    }
}