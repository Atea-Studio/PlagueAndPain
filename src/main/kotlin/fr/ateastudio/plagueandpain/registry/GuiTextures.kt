package fr.ateastudio.plagueandpain.registry

import fr.ateastudio.plagueandpain.PlagueAndPain
import xyz.xenondevs.nova.initialize.Init
import xyz.xenondevs.nova.initialize.InitStage
import xyz.xenondevs.nova.ui.overlay.guitexture.GuiTexture

@Suppress("unused", "UnstableApiUsage")
@Init(stage = InitStage.PRE_PACK)
object GuiTextures {

    val CONDITION_GUIDE: GuiTexture = PlagueAndPain.guiTexture("condition_guide") {
        path("gui/condition_guide")
        inventoryLabel(false)
    }
    
    val CONDITION_STATUS: GuiTexture = PlagueAndPain.guiTexture("condition_status") {
        path("gui/condition_status")
        inventoryLabel(false)
    }
    
    val CONDITION_DETAIL: GuiTexture = PlagueAndPain.guiTexture("condition_detail") {
        path("gui/condition_detail")
        inventoryLabel(false)
    }

}
