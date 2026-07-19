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
    
    val COUGH_ICON = item("cough_icon") {}
    val FEVER_ICON = item("fever_icon") {}
    val PLAGUE_ICON = item("plague_icon") {}
    val PNEUMONIA_ICON = item("pneumonia_icon") {}
    val RABIES_ICON = item("rabies_icon") {}
    val BROKEN_LEG_ICON = item("broken_leg_icon") {}
    val OPEN_WOUND_ICON = item("open_wound_icon") {}
}