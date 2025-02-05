package fr.ateastudio.plagueandpain.registry

import fr.ateastudio.plagueandpain.PlagueAndPain
import fr.ateastudio.plagueandpain.ability.DiseaseAbility
import xyz.xenondevs.nova.addon.registry.AbilityTypeRegistry
import xyz.xenondevs.nova.initialize.Init
import xyz.xenondevs.nova.initialize.InitStage

@Init(stage = InitStage.POST_PACK_PRE_WORLD)
object Abilities : AbilityTypeRegistry by PlagueAndPain.registry {
    val DISEASE = registerAbilityType("disease_ability") { DiseaseAbility(it) }
}