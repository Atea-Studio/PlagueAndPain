package fr.ateastudio.plagueandpain.service

import fr.ateastudio.plagueandpain.Disease
import fr.ateastudio.plagueandpain.Injury
import fr.ateastudio.plagueandpain.PlagueAndPain
import fr.ateastudio.plagueandpain.config.AddonConfig
import fr.ateastudio.plagueandpain.config.BrokenLegConfig
import fr.ateastudio.plagueandpain.config.CoughConfig
import fr.ateastudio.plagueandpain.config.FeverConfig
import fr.ateastudio.plagueandpain.config.OpenWoundConfig
import fr.ateastudio.plagueandpain.config.PlagueConfig
import fr.ateastudio.plagueandpain.config.PneumoniaConfig
import fr.ateastudio.plagueandpain.config.RabiesConfig
import fr.ateastudio.plagueandpain.util.ConditionSeverity
import fr.ateastudio.plagueandpain.util.DiseaseManager
import fr.ateastudio.plagueandpain.util.InjuryManager
import kotlin.random.Random
import net.kyori.adventure.text.Component
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import xyz.xenondevs.nova.util.NamespacedKey
import xyz.xenondevs.nova.util.item.ItemUtils

object ConditionService {
    
    private val bloodDiseaseKey = NamespacedKey(PlagueAndPain, "blood_syringe_disease")
    
    data class ActiveConditions(
        val disease: Disease?,
        val injury: Injury?
    )
    
    fun tick(player: Player): ActiveConditions {
        tickEnvironmentalExposure(player)
        tickInjury(player)
        tickDisease(player)
        
        return ActiveConditions(
            disease = DiseaseManager.getType(player),
            injury = InjuryManager.getType(player)
        )
    }
    
    fun tryApplyDisease(player: Player, disease: Disease, chance: Double? = null, message: Component? = null, progress: Double = 0.0): Boolean {
        if (!DiseaseManager.canAcquire(player)) {
            return false
        }
        if (chance != null && !roll(chance)) {
            return false
        }
        
        DiseaseManager.setCondition(player, disease, progress)
        if (message != null) {
            player.sendMessage(message)
        }
        return true
    }
    
    fun tryApplyInjury(player: Player, injury: Injury, chance: Double? = null, message: Component? = null, progress: Double = 0.0): Boolean {
        if (!InjuryManager.canAcquire(player)) {
            return false
        }
        if (chance != null && !roll(chance)) {
            return false
        }
        
        InjuryManager.setCondition(player, injury, progress)
        if (message != null) {
            player.sendMessage(message)
        }
        return true
    }
    
    fun useBandage(player: Player): Boolean {
        val injury = InjuryManager.getType(player) ?: return false
        
        return when (injury) {
            Injury.OPEN_WOUND -> {
                InjuryManager.clear(player)
                player.sendMessage(t("message.plagueandpain.bandage.open_wound"))
                true
            }
            Injury.BROKEN_LEG -> {
                val remaining = InjuryManager.reduceProgress(player, AddonConfig.bandageRelief)
                if (remaining <= 0.0) {
                    InjuryManager.clear(player)
                    player.sendMessage(t("message.plagueandpain.bandage.broken_leg.cleared"))
                } else {
                    player.sendMessage(t("message.plagueandpain.bandage.broken_leg.relief"))
                }
                true
            }
        }
    }
    
    fun useMedicine(player: Player): Boolean {
        val disease = DiseaseManager.getType(player) ?: return false
        val remaining = DiseaseManager.reduceProgress(player, AddonConfig.medicineRelief)
        
        if (remaining <= 0.0) {
            DiseaseManager.clear(player)
            player.sendMessage(
                t("message.plagueandpain.medicine.cured", conditionName(disease))
            )
        } else {
            player.sendMessage(
                t("message.plagueandpain.medicine.relief", conditionName(disease))
            )
        }
        return true
    }
    
    fun handleMobDiseaseHit(player: Player, attacker: Entity) {
        when (attacker.type) {
            EntityType.ZOMBIE, EntityType.HUSK, EntityType.DROWNED -> {
                tryApplyDisease(
                    player = player,
                    disease = Disease.PLAGUE,
                    chance = PlagueConfig.undeadBiteChance,
                    message = t("message.plagueandpain.infection.plague")
                )
            }
            EntityType.WOLF, EntityType.FOX -> {
                tryApplyDisease(
                    player = player,
                    disease = Disease.RABIES,
                    chance = RabiesConfig.animalBiteChance,
                    message = t("message.plagueandpain.infection.rabies")
                )
            }
            else -> Unit
        }
    }
    
    fun sampleBlood(player: Player, target: Player): Boolean {
        val disease = DiseaseManager.getType(target) ?: run {
            player.sendMessage(
                t("message.plagueandpain.blood.no_disease", Component.text(target.name))
            )
            return false
        }
        
        replaceOneHeldItem(player, createBloodSyringe(disease))
        player.sendMessage(
            t("message.plagueandpain.blood.sample_collected", Component.text(target.name))
        )
        return true
    }
    
    fun injectBlood(player: Player, target: Player): Boolean {
        val disease = getStoredDisease(player.inventory.itemInMainHand) ?: run {
            player.sendMessage(t("message.plagueandpain.blood.unusable"))
            return false
        }
        
        if (!tryApplyDisease(
                target,
                disease,
                message = t(
                    "message.plagueandpain.blood.injected_by",
                    Component.text(player.name),
                    conditionName(disease)
                )
            )
        ) {
            player.sendMessage(
                t("message.plagueandpain.blood.resisted", Component.text(target.name))
            )
            return false
        }
        
        replaceOneHeldItem(player, createEmptySyringe())
        player.sendMessage(
            t("message.plagueandpain.blood.injected", Component.text(target.name))
        )
        return true
    }
    
    private fun tickEnvironmentalExposure(player: Player) {
        if (DiseaseManager.hasCondition(player)) {
            return
        }
        if (player.ticksLived % CoughConfig.exposureCheckIntervalTicks != 0) {
            return
        }
        
        val chance = when {
            player.location.block.isLiquid -> CoughConfig.waterExposureChance
            isExposedToRain(player.location) -> CoughConfig.rainExposureChance
            else -> return
        }
        
        tryApplyDisease(
            player = player,
            disease = Disease.COUGH,
            chance = chance,
            message = t("message.plagueandpain.exposure.cough")
        )
    }
    
    private fun tickInjury(player: Player) {
        val injury = InjuryManager.getType(player) ?: return
        val progress = when (injury) {
            Injury.BROKEN_LEG -> InjuryManager.addProgress(player, BrokenLegConfig.progressPerTick)
            Injury.OPEN_WOUND -> InjuryManager.addProgress(player, OpenWoundConfig.progressPerTick)
        }
        
        when (injury) {
            Injury.BROKEN_LEG -> {
                applyPotionEffect(player, PotionEffectType.SLOWNESS, BrokenLegConfig.slownessAmplifier(progress))
            }
            Injury.OPEN_WOUND -> {
                if (player.ticksLived % OpenWoundConfig.bleedIntervalTicks == 0) {
                    player.damage(OpenWoundConfig.bleedDamage(progress))
                }
                
                if (progress >= OpenWoundConfig.infectionThreshold &&
                    !DiseaseManager.hasCondition(player) &&
                    player.ticksLived % OpenWoundConfig.bleedIntervalTicks == 0
                ) {
                    tryApplyDisease(
                        player = player,
                        disease = Disease.FEVER,
                        chance = OpenWoundConfig.feverInfectionChance,
                        message = t("message.plagueandpain.injury.infection.fever"),
                        progress = 20.0
                    )
                }
            }
        }
    }
    
    private fun tickDisease(player: Player) {
        val disease = DiseaseManager.getType(player) ?: return
        val progress = when (disease) {
            Disease.COUGH -> DiseaseManager.addProgress(player, CoughConfig.progressPerTick)
            Disease.FEVER -> DiseaseManager.addProgress(player, FeverConfig.progressPerTick)
            Disease.PNEUMONIA -> DiseaseManager.addProgress(player, PneumoniaConfig.progressPerTick)
            Disease.PLAGUE -> DiseaseManager.addProgress(player, PlagueConfig.progressPerTick)
            Disease.RABIES -> DiseaseManager.addProgress(player, RabiesConfig.progressPerTick)
        }
        
        when (disease) {
            Disease.COUGH -> {
                applyPotionEffect(player, PotionEffectType.SLOWNESS, CoughConfig.slownessAmplifier)
                if (progress >= CoughConfig.pneumoniaThreshold) {
                    DiseaseManager.setCondition(player, Disease.PNEUMONIA, CoughConfig.pneumoniaStartingProgress)
                    player.sendMessage(t("message.plagueandpain.disease.progressed.pneumonia"))
                }
            }
            Disease.FEVER -> {
                applyPotionEffect(player, PotionEffectType.WEAKNESS, FeverConfig.weaknessAmplifier)
                applyPotionEffect(player, PotionEffectType.HUNGER, FeverConfig.hungerAmplifier)
                if (ConditionSeverity.fromProgress(progress) >= ConditionSeverity.SEVERE &&
                    player.ticksLived % FeverConfig.damageIntervalTicks == 0
                ) {
                    player.damage(FeverConfig.damagePerPulse)
                }
            }
            Disease.PNEUMONIA -> {
                applyPotionEffect(player, PotionEffectType.SLOWNESS, PneumoniaConfig.slownessAmplifier)
                applyPotionEffect(player, PotionEffectType.WEAKNESS, PneumoniaConfig.weaknessAmplifier)
                if (player.ticksLived % PneumoniaConfig.damageIntervalTicks == 0) {
                    player.damage(PneumoniaConfig.damagePerPulse)
                }
            }
            Disease.PLAGUE -> {
                applyPotionEffect(player, PotionEffectType.POISON, PlagueConfig.poisonAmplifier)
                applyPotionEffect(player, PotionEffectType.WEAKNESS, PlagueConfig.weaknessAmplifier)
                if (player.ticksLived % PlagueConfig.damageIntervalTicks == 0) {
                    player.damage(PlagueConfig.damagePerPulse)
                }
            }
            Disease.RABIES -> {
                applyPotionEffect(player, PotionEffectType.NAUSEA, RabiesConfig.nauseaAmplifier)
                applyPotionEffect(player, PotionEffectType.WEAKNESS, RabiesConfig.weaknessAmplifier)
                if (player.ticksLived % RabiesConfig.damageIntervalTicks == 0) {
                    player.damage(RabiesConfig.damagePerPulse)
                }
            }
        }
    }
    
    private fun createBloodSyringe(disease: Disease): ItemStack {
        val item = ItemUtils.getItemStack("plagueandpain:blood_syringe")
        val meta = item.itemMeta
        meta.persistentDataContainer.set(bloodDiseaseKey, PersistentDataType.STRING, disease.tag)
        item.itemMeta = meta
        return item
    }
    
    private fun createEmptySyringe(): ItemStack {
        return ItemUtils.getItemStack("plagueandpain:syringe")
    }
    
    private fun getStoredDisease(item: ItemStack): Disease? {
        val storedValue = item.itemMeta.persistentDataContainer.get(bloodDiseaseKey, PersistentDataType.STRING)
        return Disease.fromStoredValue(storedValue)
    }
    
    private fun replaceOneHeldItem(player: Player, replacement: ItemStack) {
        val heldItem = player.inventory.itemInMainHand
        if (heldItem.amount <= 1) {
            player.inventory.setItemInMainHand(replacement)
            return
        }
        
        heldItem.amount -= 1
        player.inventory.setItemInMainHand(heldItem)
        val leftovers = player.inventory.addItem(replacement)
        leftovers.values.forEach { overflow ->
            player.world.dropItemNaturally(player.location, overflow)
        }
    }
    
    private fun applyPotionEffect(player: Player, type: PotionEffectType, amplifier: Int) {
        if (amplifier < 0) {
            return
        }
        
        player.addPotionEffect(PotionEffect(type, 40, amplifier, false, false, false), true)
    }
    
    private fun isExposedToRain(location: Location): Boolean {
        val world = location.world
        if (!world.hasStorm()) {
            return false
        }
        
        return world.getHighestBlockYAt(location) <= location.blockY
    }
    
    private fun roll(chance: Double): Boolean {
        return Random.nextDouble(100.0) < chance
    }
    
    private fun conditionName(disease: Disease): Component {
        return Component.translatable(disease.translationKey)
    }
    
    private fun t(key: String, vararg args: Component): Component {
        return Component.translatable(key, *args)
    }
}
