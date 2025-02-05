package fr.ateastudio.plagueandpain.hud

import fr.ateastudio.plagueandpain.Disease
import fr.ateastudio.plagueandpain.Injury
import net.kyori.adventure.text.Component
import org.bukkit.inventory.MainHand
import xyz.xenondevs.nova.ui.overlay.actionbar.ActionbarOverlay
import xyz.xenondevs.nova.util.component.adventure.font
import xyz.xenondevs.nova.util.component.adventure.move

class HudOverlay(private var mainHand: MainHand, private var disease: Disease?, private var injury: Injury?) : ActionbarOverlay {
    override var component: Component = getCurrentComponent()
        private set
        
    fun refresh(mainHand: MainHand, disease: Disease?, injury: Injury?) {
        this.mainHand = mainHand
        this.disease = disease
        this.injury = injury
        component = getCurrentComponent()
    }
    
    private fun getCurrentComponent(): Component {
        return if (disease != null && injury != null) {
            getComboComponent(disease!!, injury!!)
        } else if (disease != null) {
            getDiseaseComponent(disease!!)
        } else if (injury != null) {
            getInjuryComponent(injury!!)
        } else {
            Component.empty()
        }
    }
    
    private fun getInjuryComponent(injury: Injury): Component {
        val injuryBackground = ('\uEFFE'.code).toChar().toString()
        val font = "plagueandpain:disease_icons"
        
        return Component.text()
            .move(if (mainHand == MainHand.RIGHT) 98 else -122)
            .append(
                Component.text(injuryBackground)
                    .font(font)
            )
            .move(-22)
            .append(
                Component.text(injury.char)
                    .font(font)
            )
            .build()
    }
    
    private fun getDiseaseComponent(disease: Disease): Component {
        val diseaseBackground = ('\uEFFD'.code).toChar().toString()
        val font = "plagueandpain:disease_icons"
        
        return Component.text()
            .move(if (mainHand == MainHand.RIGHT) 98 else -122)
            .append(
                Component.text(diseaseBackground)
                    .font(font)
            )
            .move(-22)
            .append(
                Component.text(disease.char)
                    .font(font)
            )
            .build()
    }
    
    private fun getComboComponent(disease: Disease, injury: Injury): Component {
        val comboBackground = ('\uEFFF'.code).toChar().toString()
        val font = "plagueandpain:disease_icons"
        
        return Component.text()
            .move(if (mainHand == MainHand.RIGHT) 98 else -142)
            .append(
                Component.text(comboBackground)
                    .font(font)
            )
            .move(-42)
            .append(
                Component.text(injury.char)
                    .font(font)
            )
            .move(2)
            .append(
                Component.text(disease.char)
                    .font(font)
            )
            .build()
    }
}