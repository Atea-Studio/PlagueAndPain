package fr.ateastudio.plagueandpain.registry

import fr.ateastudio.plagueandpain.PlagueAndPain
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.ItemUseAnimation
import xyz.xenondevs.nova.addon.registry.ItemRegistry
import xyz.xenondevs.nova.initialize.Init
import xyz.xenondevs.nova.initialize.InitStage
import xyz.xenondevs.nova.world.item.behavior.Consumable

@Init(stage = InitStage.PRE_PACK)
object Items : ItemRegistry by PlagueAndPain.registry {
    val BANDAGE = item("bandage") {
        behaviors(Consumable(canAlwaysEat = true, consumeTime = 16, animation = ItemUseAnimation.BRUSH, sound = SoundEvents.ARMOR_EQUIP_LEATHER))
        maxStackSize(16)
    }
    val MEDICINE = item("medicine") {
        behaviors(Consumable(canAlwaysEat = true, consumeTime = 16, animation = ItemUseAnimation.BRUSH, sound = SoundEvents.GENERIC_EAT))
        maxStackSize(16)
    }
    val SYRINGE = item("syringe") {
        behaviors(Consumable(animation = ItemUseAnimation.BLOCK, sound = SoundEvents.BREEZE_WIND_CHARGE_BURST))
        maxStackSize(16)
    }
    val BLOOD_SYRINGE = item("blood_syringe") {
        behaviors(Consumable(animation = ItemUseAnimation.BLOCK, sound = SoundEvents.CROSSBOW_LOADING_MIDDLE))
        maxStackSize(16)
    }
    


}