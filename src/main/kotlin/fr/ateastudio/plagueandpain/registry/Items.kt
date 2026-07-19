package fr.ateastudio.plagueandpain.registry

import fr.ateastudio.plagueandpain.PlagueAndPain.item
import io.papermc.paper.datacomponent.item.consumable.ItemUseAnimation
import io.papermc.paper.registry.keys.SoundEventKeys
import xyz.xenondevs.nova.initialize.Init
import xyz.xenondevs.nova.initialize.InitStage
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
    val OK = item("ok") { hidden(true) }
    val EXIT = item("exit") { hidden(true) }
    val SEVERITY_LOW = item("severity_low") { hidden(true) }
    val SEVERITY_MEDIUM = item("severity_medium") { hidden(true) }
    val SEVERITY_HIGH = item("severity_high") { hidden(true) }
}