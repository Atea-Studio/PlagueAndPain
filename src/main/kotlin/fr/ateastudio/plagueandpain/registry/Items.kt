package fr.ateastudio.plagueandpain.registry

import fr.ateastudio.plagueandpain.PlagueAndPain.item
import io.papermc.paper.datacomponent.item.consumable.ItemUseAnimation
import io.papermc.paper.registry.keys.SoundEventKeys
import xyz.xenondevs.nova.initialize.Init
import xyz.xenondevs.nova.initialize.InitStage
import xyz.xenondevs.nova.world.item.NovaItem
import xyz.xenondevs.nova.world.item.behavior.Consumable


@Suppress("unused", "UnstableApiUsage")
@Init(stage = InitStage.PRE_PACK)
object Items {
    
    val BANDAGE = item("bandage") {
        behaviors(Consumable(canAlwaysEat = true, consumeTime = 16, animation = ItemUseAnimation.BRUSH, sound = SoundEventKeys.ITEM_ARMOR_EQUIP_GENERIC))
        maxStackSize(16)
    }
    val MEDICINE = item("medicine") {
        behaviors(Consumable(canAlwaysEat = true, consumeTime = 16, animation = ItemUseAnimation.BRUSH, sound = SoundEventKeys.ENTITY_GENERIC_EAT))
        maxStackSize(16)
    }
    val SYRINGE = item("syringe") {
        maxStackSize(16)
    }
    val BLOOD_SYRINGE = item("blood_syringe") {
        maxStackSize(16)
    }
    
    val COUGH_ICON = item("cough_icon") { hidden(true) }
    val FEVER_ICON = item("fever_icon") { hidden(true) }
    val PLAGUE_ICON = item("plague_icon") { hidden(true) }
    val PNEUMONIA_ICON = item("pneumonia_icon") { hidden(true) }
    val RABIES_ICON = item("rabies_icon") { hidden(true) }
    val BROKEN_LEG_ICON = item("broken_leg_icon") { hidden(true) }
    val OPEN_WOUND_ICON = item("open_wound_icon") { hidden(true) }
    val SEVERITY_LOW = guiItem("severity_low")
    val SEVERITY_MEDIUM = guiItem("severity_medium")
    val SEVERITY_HIGH = guiItem("severity_high")
    val CHECK_COLOR = guiItem("check_color")
    val CLOSE = guiItem("close")
    val CLOSE_COLOR = guiItem("close_color")
    val COINS = guiItem("coins")
    val EXCLAMATION = guiItem("exclamation")
    val EXCLAMATION_COLOR = guiItem("exclamation_color")
    val LOCK_CLOSED = guiItem("lock_closed")
    val LOCK_OPEN = guiItem("lock_open")
    val MINUS = guiItem("minus")
    val MINUS_COLOR = guiItem("minus_color")
    val PLUS = guiItem("plus")
    val PLUS_COLOR = guiItem("plus_color")
    val QUESTION = guiItem("question")
    val QUESTION_COLOR = guiItem("question_color")
    val REFRESH = guiItem("refresh")
    val SEARCH = guiItem("search")
    val SETTINGS = guiItem("settings")
    val ARROW_DOWN = guiItem("arrow_down")
    val ARROW_LEFT = guiItem("arrow_left")
    val ARROW_RIGHT = guiItem("arrow_right")
    val ARROW_UP = guiItem("arrow_up")
    val CHECK = guiItem("check")
    
    
    
    private fun guiItem(
        name: String,
        localizedName: String? = null,
        stretched: Boolean = false,
        background: Boolean = false
    ): NovaItem = item("gui/opaque/$name") {
        if (localizedName == null) {
            name(null)
        } else localizedName(localizedName)
        hidden(true)
        modelDefinition  {
            model = buildModel {
                createGuiModel(background, stretched, "item/gui/$name")
            }
        }
    }
}