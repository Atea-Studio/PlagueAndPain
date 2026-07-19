package fr.ateastudio.plagueandpain.ability

import fr.ateastudio.plagueandpain.hud.HudOverlay
import fr.ateastudio.plagueandpain.service.ConditionService
import org.bukkit.entity.Player
import xyz.xenondevs.nova.ui.overlay.actionbar.ActionbarOverlayManager
import xyz.xenondevs.nova.world.player.ability.Ability

class DiseaseAndInjuryAbility(player: Player) : Ability(player) {
    
    private val diseaseOverlay = HudOverlay(player.mainHand, null, null)
    
    init {
        ActionbarOverlayManager.registerOverlay(player, diseaseOverlay)
    }
    
    override fun handleRemove() {
        ActionbarOverlayManager.unregisterOverlay(player, diseaseOverlay)
    }
    
    override fun handleTick() {
        val conditions = ConditionService.tick(player)
        diseaseOverlay.refresh(player.mainHand, conditions.disease, conditions.injury)
    }
}
