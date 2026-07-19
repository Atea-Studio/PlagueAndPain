package fr.ateastudio.plagueandpain.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.DoubleArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import com.mojang.brigadier.tree.LiteralCommandNode
import fr.ateastudio.plagueandpain.Disease
import fr.ateastudio.plagueandpain.Injury
import fr.ateastudio.plagueandpain.gui.ConditionGuideGui
import fr.ateastudio.plagueandpain.util.DiseaseManager
import fr.ateastudio.plagueandpain.util.InjuryManager
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.concurrent.CompletableFuture

object ConditionGuideCommand {

    fun buildNode(): LiteralCommandNode<CommandSourceStack> {

        val executeGui: (CommandContext<CommandSourceStack>) -> Int = { ctx ->
            val sender = ctx.source.sender
            if (sender !is Player) {
                sender.sendMessage(Component.text("This command can only be used by players."))
                Command.SINGLE_SUCCESS
            } else {
                ConditionGuideGui.openMain(sender)
                Command.SINGLE_SUCCESS
            }
        }

        val executeStatus: (CommandContext<CommandSourceStack>) -> Int = { ctx ->
            val sender = ctx.source.sender
            if (sender !is Player) {
                sender.sendMessage(Component.text("This command can only be used by players."))
                Command.SINGLE_SUCCESS
            } else {
                ConditionGuideGui.openStatus(sender)
                Command.SINGLE_SUCCESS
            }
        }

        val executeAdminGiveDisease: (CommandContext<CommandSourceStack>) -> Int = { ctx ->
            val sender = ctx.source.sender
            val target = resolvePlayer(sender, StringArgumentType.getString(ctx, "player"))
            if (target == null) {
                Command.SINGLE_SUCCESS
            } else {
                val diseaseInput = StringArgumentType.getString(ctx, "disease")
                val disease = Disease.fromStoredValue(diseaseInput)
                if (disease == null) {
                    sender.sendMessage(Component.text("Unknown disease: $diseaseInput"))
                    Command.SINGLE_SUCCESS
                } else {
                    val progress = readProgress(ctx)
                    DiseaseManager.setCondition(target, disease, progress)
                    sender.sendMessage(Component.text("Applied disease ${disease.tag} to ${target.name} (${progressText(progress)})."))
                    if (sender != target) {
                        target.sendMessage(Component.text("An admin applied disease ${disease.tag} to you (${progressText(progress)})."))
                    }
                    Command.SINGLE_SUCCESS
                }
            }
        }

        val executeAdminGiveInjury: (CommandContext<CommandSourceStack>) -> Int = { ctx ->
            val sender = ctx.source.sender
            val target = resolvePlayer(sender, StringArgumentType.getString(ctx, "player"))
            if (target == null) {
                Command.SINGLE_SUCCESS
            } else {
                val injuryInput = StringArgumentType.getString(ctx, "injury")
                val injury = Injury.fromStoredValue(injuryInput)
                if (injury == null) {
                    sender.sendMessage(Component.text("Unknown injury: $injuryInput"))
                    Command.SINGLE_SUCCESS
                } else {
                    val progress = readProgress(ctx)
                    InjuryManager.setCondition(target, injury, progress)
                    sender.sendMessage(Component.text("Applied injury ${injury.tag} to ${target.name} (${progressText(progress)})."))
                    if (sender != target) {
                        target.sendMessage(Component.text("An admin applied injury ${injury.tag} to you (${progressText(progress)})."))
                    }
                    Command.SINGLE_SUCCESS
                }
            }
        }

        val executeAdminClearAll: (CommandContext<CommandSourceStack>) -> Int = { ctx ->
            val sender = ctx.source.sender
            val target = resolvePlayer(sender, StringArgumentType.getString(ctx, "player"))
            if (target == null) {
                Command.SINGLE_SUCCESS
            } else {
                DiseaseManager.clear(target)
                InjuryManager.clear(target)
                sender.sendMessage(Component.text("Cleared all conditions for ${target.name}."))
                if (sender != target) {
                    target.sendMessage(Component.text("An admin cleared all your conditions."))
                }
                Command.SINGLE_SUCCESS
            }
        }

        val executeAdminClearDisease: (CommandContext<CommandSourceStack>) -> Int = { ctx ->
            val sender = ctx.source.sender
            val target = resolvePlayer(sender, StringArgumentType.getString(ctx, "player"))
            if (target == null) {
                Command.SINGLE_SUCCESS
            } else {
                DiseaseManager.clear(target)
                sender.sendMessage(Component.text("Cleared disease for ${target.name}."))
                if (sender != target) {
                    target.sendMessage(Component.text("An admin cleared your disease."))
                }
                Command.SINGLE_SUCCESS
            }
        }

        val executeAdminClearInjury: (CommandContext<CommandSourceStack>) -> Int = { ctx ->
            val sender = ctx.source.sender
            val target = resolvePlayer(sender, StringArgumentType.getString(ctx, "player"))
            if (target == null) {
                Command.SINGLE_SUCCESS
            } else {
                InjuryManager.clear(target)
                sender.sendMessage(Component.text("Cleared injury for ${target.name}."))
                if (sender != target) {
                    target.sendMessage(Component.text("An admin cleared your injury."))
                }
                Command.SINGLE_SUCCESS
            }
        }

        return Commands.literal("plagueandpain")
            .then(
                Commands.literal("guide")
                    .requires { source -> source.sender is Player && source.sender.hasPermission("plagueandpain.use") }
                    .executes(executeGui)
            )
            .then(
                Commands.literal("conditions")
                    .requires { source -> source.sender is Player && source.sender.hasPermission("plagueandpain.use") }
                    .executes(executeGui)
            )
            .then(
                Commands.literal("status")
                    .requires { source -> source.sender is Player && source.sender.hasPermission("plagueandpain.use") }
                    .executes(executeStatus)
            )
            .then(
                Commands.literal("admin")
                    .requires { source -> source.sender.hasPermission("plagueandpain.admin") }
                    .then(
                        Commands.literal("give")
                            .then(
                                Commands.argument("player", StringArgumentType.word())
                                    .suggests { _, builder -> suggestOnlinePlayers(builder) }
                                    .then(
                                        Commands.literal("disease")
                                            .then(
                                                Commands.argument("disease", StringArgumentType.word())
                                                    .suggests { _, builder -> suggestDiseases(builder) }
                                                    .executes(executeAdminGiveDisease)
                                                    .then(
                                                        Commands.argument("progress", DoubleArgumentType.doubleArg(0.0, 100.0))
                                                            .executes(executeAdminGiveDisease)
                                                    )
                                            )
                                    )
                                    .then(
                                        Commands.literal("injury")
                                            .then(
                                                Commands.argument("injury", StringArgumentType.word())
                                                    .suggests { _, builder -> suggestInjuries(builder) }
                                                    .executes(executeAdminGiveInjury)
                                                    .then(
                                                        Commands.argument("progress", DoubleArgumentType.doubleArg(0.0, 100.0))
                                                            .executes(executeAdminGiveInjury)
                                                    )
                                            )
                                    )
                            )
                    )
                    .then(
                        Commands.literal("clear")
                            .then(
                                Commands.argument("player", StringArgumentType.word())
                                    .suggests { _, builder -> suggestOnlinePlayers(builder) }
                                    .then(Commands.literal("all").executes(executeAdminClearAll))
                                    .then(Commands.literal("disease").executes(executeAdminClearDisease))
                                    .then(Commands.literal("injury").executes(executeAdminClearInjury))
                            )
                    )
            )
            .requires { source -> source.sender.hasPermission("plagueandpain.use") || source.sender.hasPermission("plagueandpain.admin") }
            .executes(executeGui)
            .build()
    }

    private fun resolvePlayer(sender: CommandSender, playerName: String): Player? {
        val target = Bukkit.getPlayerExact(playerName)
        if (target == null) {
            sender.sendMessage(Component.text("Player not found: $playerName"))
        }
        return target
    }

    private fun readProgress(ctx: CommandContext<CommandSourceStack>): Double {
        return runCatching { DoubleArgumentType.getDouble(ctx, "progress") }.getOrDefault(0.0)
    }

    private fun suggestOnlinePlayers(builder: SuggestionsBuilder): CompletableFuture<Suggestions> {
        val remaining = builder.remaining.lowercase()
        Bukkit.getOnlinePlayers()
            .map { it.name }
            .filter { it.lowercase().startsWith(remaining) }
            .forEach { builder.suggest(it) }
        return builder.buildFuture()
    }

    private fun suggestDiseases(builder: SuggestionsBuilder): CompletableFuture<Suggestions> {
        val remaining = builder.remaining.lowercase()
        Disease.entries
            .map { it.tag }
            .filter { it.startsWith(remaining) }
            .forEach { builder.suggest(it) }
        return builder.buildFuture()
    }

    private fun suggestInjuries(builder: SuggestionsBuilder): CompletableFuture<Suggestions> {
        val remaining = builder.remaining.lowercase()
        Injury.entries
            .map { it.tag }
            .filter { it.startsWith(remaining) }
            .forEach { builder.suggest(it) }
        return builder.buildFuture()
    }

    private fun progressText(value: Double): String = "%.1f%%".format(value)
}
