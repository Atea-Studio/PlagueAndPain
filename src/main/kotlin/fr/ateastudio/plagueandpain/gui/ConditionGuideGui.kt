package fr.ateastudio.plagueandpain.gui

import fr.ateastudio.plagueandpain.Disease
import fr.ateastudio.plagueandpain.Injury
import fr.ateastudio.plagueandpain.config.AddonConfig
import fr.ateastudio.plagueandpain.config.BrokenLegConfig
import fr.ateastudio.plagueandpain.config.CoughConfig
import fr.ateastudio.plagueandpain.config.FeverConfig
import fr.ateastudio.plagueandpain.config.OpenWoundConfig
import fr.ateastudio.plagueandpain.config.PlagueConfig
import fr.ateastudio.plagueandpain.config.PneumoniaConfig
import fr.ateastudio.plagueandpain.config.RabiesConfig
import fr.ateastudio.plagueandpain.registry.GuiTextures
import fr.ateastudio.plagueandpain.registry.Items
import fr.ateastudio.plagueandpain.util.ConditionSeverity
import fr.ateastudio.plagueandpain.util.DiseaseManager
import fr.ateastudio.plagueandpain.util.InjuryManager
import io.papermc.paper.datacomponent.DataComponentTypes
import kotlin.math.roundToInt
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import xyz.xenondevs.invui.Click
import xyz.xenondevs.invui.gui.Gui
import xyz.xenondevs.invui.item.AbstractItem
import xyz.xenondevs.invui.item.ItemBuilder
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.window.Window
import xyz.xenondevs.nova.util.item.ItemUtils

internal class ConditionGuideGui(private val player: Player) {

    fun openMainWindow() {
        Window.builder()
            .setViewer(player)
            .setTitle(GuiTextures.CONDITION_GUIDE.component)
            .setUpperGui(buildMainGui())
            .build()
            .open()
    }
    
    fun openStatusWindow() {
        Window.builder()
            .setViewer(player)
            .setTitle(GuiTextures.CONDITION_STATUS.component)
            .setUpperGui(buildStatusGui())
            .build()
            .open()
    }

    private fun openDetailWindow(condition: GuideCondition) {
        Window.builder()
            .setViewer(player)
            .setTitle(GuiTextures.CONDITION_DETAIL.component)
            .setUpperGui(buildDetailGui(condition))
            .build()
            .open()
    }

    private fun buildMainGui(): Gui {
        return Gui.builder()
            .setStructure(
                "S . G . . . . . X",
                ". . . . . . . . .",
                ". . . . . . . . .",
                ". C F P . B W . .",
                ". N R . 0 . . . ."
            )
            .addIngredient('S', navStatusButton(false))
            .addIngredient('G', navGuideButton(true))
            .addIngredient('X', navExitButton())
            .addIngredient('O', staticItem(
                Material.BOOK,
                tc("menu.plagueandpain.guide.info.title", NamedTextColor.WHITE),
                listOf(
                    tc("menu.plagueandpain.guide.info.l1", NamedTextColor.GRAY),
                    tc("menu.plagueandpain.guide.info.l2", NamedTextColor.GRAY),
                    tc("menu.plagueandpain.guide.info.l3", NamedTextColor.GRAY),
                    tc("menu.plagueandpain.guide.info.l4", NamedTextColor.YELLOW)
                )
            ))
            .addIngredient('C', conditionButton(GuideCondition.COUGH))
            .addIngredient('F', conditionButton(GuideCondition.FEVER))
            .addIngredient('P', conditionButton(GuideCondition.PLAGUE))
            .addIngredient('N', conditionButton(GuideCondition.PNEUMONIA))
            .addIngredient('R', conditionButton(GuideCondition.RABIES))
            .addIngredient('B', conditionButton(GuideCondition.BROKEN_LEG))
            .addIngredient('W', conditionButton(GuideCondition.OPEN_WOUND))
            .build()
    }
    
    private fun buildStatusGui(): Gui {
        return Gui.builder()
            .setStructure(
                "S . G . . . . . X",
                ". . . . . . . . .",
                ". . s . . . t . .",
                ". . d . . . i . .",
                ". . . . . . . . ."
            )
            .addIngredient('S', navStatusButton(true))
            .addIngredient('G', navGuideButton(false))
            .addIngredient('X', navExitButton())
            .addIngredient('s', diseaseSeverityIcon())
            .addIngredient('t', injurySeverityIcon())
            .addIngredient('d', currentDiseaseButton())
            .addIngredient('i', currentInjuryButton())
            .build()
    }

    private fun buildDetailGui(condition: GuideCondition): Gui {
        return Gui.builder()
            .setStructure(
                "S . G . . . . . X",
                ". . . . . . . . .",
                ". . . . h . . . .",
                ". . p . t . o . .",
                ". . . . . . . . ."
            )
            .addIngredient('S', navStatusButton(false))
            .addIngredient('G', navGuideButton(false))
            .addIngredient('X', navExitButton())
            .addIngredient('h', staticItem(
                conditionIcon(condition),
                tc(condition.nameKey, NamedTextColor.WHITE),
                listOf(
                    tc(condition.categoryKey, NamedTextColor.GRAY),
                    tc("menu.plagueandpain.guide.detail.header.progression", NamedTextColor.YELLOW)
                )
            ))
            .addIngredient('p', staticItem(
                Material.BOOK,
                tc("menu.plagueandpain.guide.detail.progression.title", NamedTextColor.WHITE),
                progressionLore(condition)
            ))
            .addIngredient('t', treatmentItem(condition))
            .addIngredient('o', staticItem(
                Material.COMPASS,
                tc("menu.plagueandpain.guide.detail.starts.title", NamedTextColor.WHITE),
                sourceLore(condition)
            ))
            .build()
    }

    private fun navStatusButton(active: Boolean): AbstractItem {
        return object : AbstractItem() {
            override fun getItemProvider(player: Player): ItemProvider {
                return ItemBuilder(statusTabIcon(player))
                    .setName(tc("menu.plagueandpain.nav.status.title", NamedTextColor.WHITE))
                    .addLoreLines(tc("menu.plagueandpain.nav.status.l1", NamedTextColor.GRAY))
                    .clearModifiers()
            }

            override fun handleClick(clickType: ClickType, player: Player, click: Click) {
                if (!active) {
                    openStatusWindow()
                }
            }
        }
    }

    private fun navGuideButton(active: Boolean): AbstractItem {
        return object : AbstractItem() {
            override fun getItemProvider(player: Player): ItemProvider {
                @Suppress("UnstableApiUsage")
                return ItemBuilder(Material.ENCHANTED_BOOK)
                    .set(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, false)
                    .setName(tc("menu.plagueandpain.nav.guide.title", NamedTextColor.WHITE))
                    .addLoreLines(tc("menu.plagueandpain.nav.guide.l1", NamedTextColor.GRAY))
                    .clearModifiers()
            }

            override fun handleClick(clickType: ClickType, player: Player, click: Click) {
                if (!active) {
                    openMainWindow()
                }
            }
        }
    }

    private fun navExitButton(): AbstractItem {
        return object : AbstractItem() {
            override fun getItemProvider(player: Player): ItemProvider {
                return ItemBuilder(ItemUtils.getItemStack(Items.EXIT.id.toString()))
                    .setName(tc("menu.plagueandpain.nav.exit.title", NamedTextColor.RED))
                    .addLoreLines(tc("menu.plagueandpain.nav.exit.l1", NamedTextColor.GRAY))
                    .clearModifiers()
            }

            override fun handleClick(clickType: ClickType, player: Player, click: Click) {
                player.closeInventory()
            }
        }
    }

    private fun conditionButton(condition: GuideCondition): AbstractItem {
        return object : AbstractItem() {
            override fun getItemProvider(player: Player): ItemProvider {
                return ItemBuilder(conditionIcon(condition))
                    .setName(tc(condition.nameKey, NamedTextColor.WHITE))
                    .addLoreLines(
                        tc(condition.categoryKey, NamedTextColor.GRAY),
                        tc("menu.plagueandpain.guide.condition.open_details.l1", NamedTextColor.YELLOW),
                        tc("menu.plagueandpain.guide.condition.open_details.l2", NamedTextColor.YELLOW)
                    )
                    .clearModifiers()
            }

            override fun handleClick(clickType: ClickType, player: Player, click: Click) {
                openDetailWindow(condition)
            }
        }
    }
    
    private fun currentDiseaseButton(): AbstractItem {
        return object : AbstractItem() {
            override fun getItemProvider(player: Player): ItemProvider {
                val disease = DiseaseManager.getType(player)
                if (disease == null) {
                    return ItemBuilder(ItemUtils.getItemStack(Items.OK.id.toString()))
                        .setName(tc("menu.plagueandpain.status.disease.none.title", NamedTextColor.GREEN))
                        .addLoreLines(tc("menu.plagueandpain.status.disease.none.l1", NamedTextColor.GRAY))
                        .clearModifiers()
                }
                
                val progress = DiseaseManager.getProgress(player)
                val mapped = fromDisease(disease)
                return ItemBuilder(conditionIcon(mapped))
                    .setName(
                        tc(
                            "menu.plagueandpain.status.disease.active.title",
                            NamedTextColor.WHITE,
                            t(mapped.nameKey)
                        )
                    )
                    .addLoreLines(
                        tc("menu.plagueandpain.status.progress", NamedTextColor.GRAY, progressLabel(progress)),
                        tc("menu.plagueandpain.status.open_details", NamedTextColor.YELLOW)
                    )
                    .clearModifiers()
            }
        
            override fun handleClick(clickType: ClickType, player: Player, click: Click) {
                val disease = DiseaseManager.getType(player) ?: return
                openDetailWindow(fromDisease(disease))
            }
        }
    }
    
    private fun currentInjuryButton(): AbstractItem {
        return object : AbstractItem() {
            override fun getItemProvider(player: Player): ItemProvider {
                val injury = InjuryManager.getType(player)
                if (injury == null) {
                    return ItemBuilder(ItemUtils.getItemStack(Items.OK.id.toString()))
                        .setName(tc("menu.plagueandpain.status.injury.none.title", NamedTextColor.GREEN))
                        .addLoreLines(tc("menu.plagueandpain.status.injury.none.l1", NamedTextColor.GRAY))
                        .clearModifiers()
                }
                
                val progress = InjuryManager.getProgress(player)
                val mapped = fromInjury(injury)
                return ItemBuilder(conditionIcon(mapped))
                    .setName(
                        tc(
                            "menu.plagueandpain.status.injury.active.title",
                            NamedTextColor.WHITE,
                            t(mapped.nameKey)
                        )
                    )
                    .addLoreLines(
                        tc("menu.plagueandpain.status.progress", NamedTextColor.GRAY, progressLabel(progress)),
                        tc("menu.plagueandpain.status.open_details", NamedTextColor.YELLOW)
                    )
                    .clearModifiers()
            }
        
            override fun handleClick(clickType: ClickType, player: Player, click: Click) {
                val injury = InjuryManager.getType(player) ?: return
                openDetailWindow(fromInjury(injury))
            }
        }
    }
    
    private fun diseaseSeverityIcon(): AbstractItem {
        return object : AbstractItem() {
            override fun getItemProvider(player: Player): ItemProvider {
                val disease = DiseaseManager.getType(player) ?: return ItemBuilder(Material.AIR)
                val severity = ConditionSeverity.fromProgress(DiseaseManager.getProgress(player))
                return ItemBuilder(severityIcon(severity))
                    .setName(tc("menu.plagueandpain.status.severity", NamedTextColor.WHITE, severityName(severity)))
                    .addLoreLines(tc("menu.plagueandpain.status.disease.active.title", NamedTextColor.GRAY, t(fromDisease(disease).nameKey)))
                    .clearModifiers()
            }
            
            override fun handleClick(clickType: ClickType, player: Player, click: Click) {
            }
        }
    }
    
    private fun injurySeverityIcon(): AbstractItem {
        return object : AbstractItem() {
            override fun getItemProvider(player: Player): ItemProvider {
                val injury = InjuryManager.getType(player) ?: return ItemBuilder(Material.AIR)
                val severity = ConditionSeverity.fromProgress(InjuryManager.getProgress(player))
                return ItemBuilder(severityIcon(severity))
                    .setName(tc("menu.plagueandpain.status.severity", NamedTextColor.WHITE, severityName(severity)))
                    .addLoreLines(tc("menu.plagueandpain.status.injury.active.title", NamedTextColor.GRAY, t(fromInjury(injury).nameKey)))
                    .clearModifiers()
            }
            
            override fun handleClick(clickType: ClickType, player: Player, click: Click) {
            }
        }
    }

    private fun staticItem(material: Material, name: Component, lore: List<Component>): ItemStack {
        return ItemBuilder(material)
            .setName(name)
            .addLoreLines(lore)
            .clearModifiers()
            .get()
    }

    private fun staticItem(stack: ItemStack, name: Component, lore: List<Component>): ItemStack {
        return ItemBuilder(stack)
            .setName(name)
            .addLoreLines(lore)
            .clearModifiers()
            .get()
    }
    
    private fun conditionIcon(condition: GuideCondition): ItemStack {
        return ItemUtils.getItemStack(condition.iconItemId)
    }
    
    private fun statusTabIcon(player: Player): ItemStack {
        val head = ItemStack(Material.PLAYER_HEAD)
        val meta = head.itemMeta as SkullMeta
        meta.owningPlayer = player
        head.itemMeta = meta
        meta.displayName(tc("menu.plagueandpain.status.tab", NamedTextColor.WHITE))
        return head
    }

    private fun treatmentItem(condition: GuideCondition): ItemStack {
        return when (condition) {
            GuideCondition.BROKEN_LEG -> staticItem(
                ItemUtils.getItemStack("plagueandpain:bandage"),
                tc("menu.plagueandpain.guide.treatment.title", NamedTextColor.WHITE),
                listOf(
                    tc(
                        "menu.plagueandpain.guide.treatment.broken_leg.l1",
                        NamedTextColor.GRAY,
                        Component.text(AddonConfig.bandageRelief.toString())
                    ),
                    tc("menu.plagueandpain.guide.treatment.broken_leg.l2", NamedTextColor.YELLOW)
                )
            )
            GuideCondition.OPEN_WOUND -> staticItem(
                ItemUtils.getItemStack("plagueandpain:bandage"),
                tc("menu.plagueandpain.guide.treatment.title", NamedTextColor.WHITE),
                listOf(
                    tc("menu.plagueandpain.guide.treatment.open_wound.l1", NamedTextColor.GRAY)
                )
            )
            else -> staticItem(
                ItemUtils.getItemStack("plagueandpain:medicine"),
                tc("menu.plagueandpain.guide.treatment.title", NamedTextColor.WHITE),
                listOf(
                    tc(
                        "menu.plagueandpain.guide.treatment.disease.l1",
                        NamedTextColor.GRAY,
                        Component.text(AddonConfig.medicineRelief.toString())
                    ),
                    tc("menu.plagueandpain.guide.treatment.disease.l2", NamedTextColor.YELLOW)
                )
            )
        }
    }

    private fun progressionLore(condition: GuideCondition): List<Component> {
        val lines = mutableListOf<Component>()
        lines += tc("menu.plagueandpain.guide.stage.mild", NamedTextColor.GREEN)
        lines += tc("menu.plagueandpain.guide.stage.moderate", NamedTextColor.YELLOW)
        lines += tc("menu.plagueandpain.guide.stage.severe", NamedTextColor.GOLD)
        lines += tc("menu.plagueandpain.guide.stage.critical", NamedTextColor.RED)
        lines += Component.empty()

        when (condition) {
            GuideCondition.COUGH -> {
                lines += tc("menu.plagueandpain.guide.cough.progression.l1", NamedTextColor.GRAY, Component.text(CoughConfig.exposureCheckIntervalTicks.toString()))
                lines += tc(
                    "menu.plagueandpain.guide.cough.progression.l2",
                    NamedTextColor.GRAY,
                    Component.text(CoughConfig.rainExposureChance.toString())
                )
                lines += tc("menu.plagueandpain.guide.cough.progression.l3", NamedTextColor.GRAY, Component.text(CoughConfig.waterExposureChance.toString()))
                lines += tc("menu.plagueandpain.guide.cough.progression.l4", NamedTextColor.YELLOW, Component.text(CoughConfig.pneumoniaThreshold.toString()))
            }
            GuideCondition.FEVER -> {
                lines += tc("menu.plagueandpain.guide.fever.progression.l1", NamedTextColor.GRAY, Component.text(FeverConfig.progressPerTick.toString()))
                lines += tc(
                    "menu.plagueandpain.guide.fever.progression.l2",
                    NamedTextColor.GRAY,
                    Component.text(FeverConfig.rottenFleshChance.toString())
                )
                lines += tc("menu.plagueandpain.guide.fever.progression.l3", NamedTextColor.GRAY, Component.text(FeverConfig.spiderEyeChance.toString()))
                lines += tc("menu.plagueandpain.guide.fever.progression.l4", NamedTextColor.YELLOW)
            }
            GuideCondition.PLAGUE -> {
                lines += tc("menu.plagueandpain.guide.plague.progression.l1", NamedTextColor.GRAY, Component.text(PlagueConfig.progressPerTick.toString()))
                lines += tc("menu.plagueandpain.guide.plague.progression.l2", NamedTextColor.GRAY, Component.text(PlagueConfig.undeadBiteChance.toString()))
                lines += tc("menu.plagueandpain.guide.plague.progression.l3", NamedTextColor.YELLOW)
            }
            GuideCondition.PNEUMONIA -> {
                lines += tc("menu.plagueandpain.guide.pneumonia.progression.l1", NamedTextColor.GRAY, Component.text(PneumoniaConfig.progressPerTick.toString()))
                lines += tc("menu.plagueandpain.guide.pneumonia.progression.l2", NamedTextColor.GRAY)
                lines += tc("menu.plagueandpain.guide.pneumonia.progression.l3", NamedTextColor.YELLOW)
            }
            GuideCondition.RABIES -> {
                lines += tc("menu.plagueandpain.guide.rabies.progression.l1", NamedTextColor.GRAY, Component.text(RabiesConfig.progressPerTick.toString()))
                lines += tc("menu.plagueandpain.guide.rabies.progression.l2", NamedTextColor.GRAY, Component.text(RabiesConfig.animalBiteChance.toString()))
                lines += tc("menu.plagueandpain.guide.rabies.progression.l3", NamedTextColor.YELLOW)
            }
            GuideCondition.BROKEN_LEG -> {
                lines += tc(
                    "menu.plagueandpain.guide.broken_leg.progression.l1",
                    NamedTextColor.GRAY,
                    Component.text(BrokenLegConfig.fallDamageThreshold.toString())
                )
                lines += tc(
                    "menu.plagueandpain.guide.broken_leg.progression.l2",
                    NamedTextColor.GRAY,
                    Component.text(BrokenLegConfig.fallHeightThreshold.toString())
                )
                lines += tc("menu.plagueandpain.guide.broken_leg.progression.l3", NamedTextColor.GRAY, Component.text(BrokenLegConfig.progressPerTick.toString()))
                lines += tc("menu.plagueandpain.guide.broken_leg.progression.l4", NamedTextColor.YELLOW)
            }
            GuideCondition.OPEN_WOUND -> {
                lines += tc("menu.plagueandpain.guide.open_wound.progression.l1", NamedTextColor.GRAY, Component.text(OpenWoundConfig.progressPerTick.toString()))
                lines += tc("menu.plagueandpain.guide.open_wound.progression.l2", NamedTextColor.GRAY)
                lines += tc("menu.plagueandpain.guide.open_wound.progression.l3", NamedTextColor.GRAY)
                lines += tc("menu.plagueandpain.guide.open_wound.progression.l4", NamedTextColor.YELLOW, Component.text(OpenWoundConfig.bleedIntervalTicks.toString()))
                lines += tc("menu.plagueandpain.guide.open_wound.progression.l5", NamedTextColor.YELLOW, Component.text(OpenWoundConfig.infectionThreshold.toString()))
            }
        }

        return lines
    }

    private fun sourceLore(condition: GuideCondition): List<Component> {
        return when (condition) {
            GuideCondition.COUGH -> listOf(
                tc("menu.plagueandpain.guide.cough.source.l1", NamedTextColor.GRAY),
                tc("menu.plagueandpain.guide.cough.source.l2", NamedTextColor.YELLOW)
            )
            GuideCondition.FEVER -> listOf(
                tc("menu.plagueandpain.guide.fever.source.l1", NamedTextColor.GRAY),
                tc("menu.plagueandpain.guide.fever.source.l2", NamedTextColor.YELLOW)
            )
            GuideCondition.PLAGUE -> listOf(
                tc("menu.plagueandpain.guide.plague.source.l1", NamedTextColor.GRAY)
            )
            GuideCondition.PNEUMONIA -> listOf(
                tc("menu.plagueandpain.guide.pneumonia.source.l1", NamedTextColor.GRAY)
            )
            GuideCondition.RABIES -> listOf(
                tc("menu.plagueandpain.guide.rabies.source.l1", NamedTextColor.GRAY)
            )
            GuideCondition.BROKEN_LEG -> listOf(
                tc("menu.plagueandpain.guide.broken_leg.source.l1", NamedTextColor.GRAY)
            )
            GuideCondition.OPEN_WOUND -> listOf(
                tc("menu.plagueandpain.guide.open_wound.source.l1", NamedTextColor.GRAY),
                tc("menu.plagueandpain.guide.open_wound.source.l2", NamedTextColor.YELLOW)
            )
        }
    }
    
    private fun progressLabel(progress: Double): Component {
        val rounded = (progress * 10.0).roundToInt() / 10.0
        return Component.text("$rounded%")
    }
    
    private fun severityName(severity: ConditionSeverity): Component {
        return when (severity) {
            ConditionSeverity.MILD -> t("condition.plagueandpain.severity.mild")
            ConditionSeverity.MODERATE -> t("condition.plagueandpain.severity.moderate")
            ConditionSeverity.SEVERE -> t("condition.plagueandpain.severity.severe")
            ConditionSeverity.CRITICAL -> t("condition.plagueandpain.severity.critical")
        }
    }
    
    private fun severityIcon(severity: ConditionSeverity): ItemStack {
        return when (severity) {
            ConditionSeverity.MILD -> Items.SEVERITY_LOW.createItemStack()
            ConditionSeverity.MODERATE -> Items.SEVERITY_MEDIUM.createItemStack()
            ConditionSeverity.SEVERE, ConditionSeverity.CRITICAL -> Items.SEVERITY_HIGH.createItemStack()
        }
    }
    
    private fun fromDisease(disease: Disease): GuideCondition {
        return when (disease) {
            Disease.COUGH -> GuideCondition.COUGH
            Disease.FEVER -> GuideCondition.FEVER
            Disease.PLAGUE -> GuideCondition.PLAGUE
            Disease.PNEUMONIA -> GuideCondition.PNEUMONIA
            Disease.RABIES -> GuideCondition.RABIES
        }
    }
    
    private fun fromInjury(injury: Injury): GuideCondition {
        return when (injury) {
            Injury.BROKEN_LEG -> GuideCondition.BROKEN_LEG
            Injury.OPEN_WOUND -> GuideCondition.OPEN_WOUND
        }
    }
    
    private fun t(key: String, vararg args: Component): Component {
        return Component.translatable(key, *args)
    }
    
    private fun tc(key: String, color: NamedTextColor, vararg args: Component): Component {
        return t(key, *args).color(color)
    }

    enum class GuideCondition(
        val nameKey: String,
        val categoryKey: String,
        val iconItemId: String
    ) {
        COUGH("condition.plagueandpain.disease.cough", "condition.plagueandpain.category.disease", Items.COUGH_ICON.id.toString()),
        FEVER("condition.plagueandpain.disease.fever", "condition.plagueandpain.category.disease", Items.FEVER_ICON.id.toString()),
        PLAGUE("condition.plagueandpain.disease.plague", "condition.plagueandpain.category.disease", Items.PLAGUE_ICON.id.toString()),
        PNEUMONIA("condition.plagueandpain.disease.pneumonia", "condition.plagueandpain.category.disease", Items.PNEUMONIA_ICON.id.toString()),
        RABIES("condition.plagueandpain.disease.rabies", "condition.plagueandpain.category.disease", Items.RABIES_ICON.id.toString()),
        BROKEN_LEG("condition.plagueandpain.injury.broken_leg", "condition.plagueandpain.category.injury", Items.BROKEN_LEG_ICON.id.toString()),
        OPEN_WOUND("condition.plagueandpain.injury.open_wound", "condition.plagueandpain.category.injury", Items.OPEN_WOUND_ICON.id.toString());
    }

    companion object {
        fun openMain(player: Player) {
            ConditionGuideGui(player).openMainWindow()
        }
        
        fun openStatus(player: Player) {
            ConditionGuideGui(player).openStatusWindow()
        }
    }
}
