package fr.ateastudio.plagueandpain.ability

import fr.ateastudio.plagueandpain.util.DiseaseManager
import fr.ateastudio.plagueandpain.util.InjuryManager
import fr.ateastudio.plagueandpain.hud.HudOverlay
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
        val injury = InjuryManager.getInjuryType(player)
        val disease = DiseaseManager.getDiseaseType(player)
        
        if (injury != null) {
            InjuryManager.addInjuryProgress(player, injury.progressByTick)
        }
        
        if (disease != null) {
            DiseaseManager.addDiseaseProgress(player, disease.progressByTick)
        }
        
        diseaseOverlay.refresh(player.mainHand, disease, injury)
    }
    
    
}