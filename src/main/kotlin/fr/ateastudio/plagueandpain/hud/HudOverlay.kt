package fr.ateastudio.plagueandpain.hud

import net.kyori.adventure.text.Component
import org.bukkit.inventory.MainHand
import xyz.xenondevs.nova.ui.overlay.actionbar.ActionbarOverlay
import xyz.xenondevs.nova.util.component.adventure.font
import xyz.xenondevs.nova.util.component.adventure.move

class HudOverlay(mainHand: MainHand) : ActionbarOverlay {
    override var component: Component = getCurrentComponent()
        private set
    
    var mainHand: MainHand = mainHand
        set(value) {
            field = value
            component = getInjuryComponent()
        }
    
    private fun getCurrentComponent(): Component {
        return getComboComponent()
    }
    private fun getInjuryComponent(): Component {
        val injuryBackground = ('\uEFFE'.code).toChar().toString()
        val font = "disease:disease_icons"
        val icon = ('\uF000'.code).toChar().toString()
        
        return Component.text()
            .move(if (mainHand == MainHand.RIGHT) 98 else -122)
            .append(
                Component.text(injuryBackground)
                    .font(font)
            )
            .move(-22)
            .append(
                Component.text(icon)
                    .font(font)
            )
            .build()
    }
    
    private fun getDiseaseComponent(): Component {
        val diseaseBackground = ('\uEFFD'.code).toChar().toString()
        val font = "disease:disease_icons"
        val icon = ('\uF004'.code).toChar().toString()
        
        return Component.text()
            .move(if (mainHand == MainHand.RIGHT) 98 else -122)
            .append(
                Component.text(diseaseBackground)
                    .font(font)
            )
            .move(-22)
            .append(
                Component.text(icon)
                    .font(font)
            )
            .build()
    }
    
    private fun getComboComponent(): Component {
        val comboBackground = ('\uEFFF'.code).toChar().toString()
        val font = "disease:disease_icons"
        val diseaseIcon = ('\uF001'.code).toChar().toString()
        val injuryIcon = ('\uF000'.code).toChar().toString()
        
        return Component.text()
            .move(if (mainHand == MainHand.RIGHT) 98 else -142)
            .append(
                Component.text(comboBackground)
                    .font(font)
            )
            .move(-42)
            .append(
                Component.text(injuryIcon)
                    .font(font)
            )
            .move(2)
            .append(
                Component.text(diseaseIcon)
                    .font(font)
            )
            .build()
    }
}