package fr.ateastudio.plagueandpain.ability

import fr.ateastudio.plagueandpain.hud.HudOverlay
import org.bukkit.entity.Player
import xyz.xenondevs.nova.ui.overlay.actionbar.ActionbarOverlayManager
import xyz.xenondevs.nova.world.player.ability.Ability

class DiseaseAbility(player: Player) : Ability(player) {
    
    private val diseaseOverlay = HudOverlay(player.mainHand)
    
    init {
        ActionbarOverlayManager.registerOverlay(player, diseaseOverlay)
    }
    
    override fun handleRemove() {
        ActionbarOverlayManager.unregisterOverlay(player, diseaseOverlay)
    }
    
    override fun handleTick() {
        diseaseOverlay.mainHand = player.mainHand
    }
    
    
}