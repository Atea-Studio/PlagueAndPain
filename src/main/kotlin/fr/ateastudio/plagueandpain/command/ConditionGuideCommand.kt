package fr.ateastudio.plagueandpain.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.tree.LiteralCommandNode
import fr.ateastudio.plagueandpain.gui.ConditionGuideGui
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import org.bukkit.entity.Player

object ConditionGuideCommand {
    
    /**
     * Builds the Brigadier command node tree.
     * Replace "conditionguide" with the actual root command name you intend to use.
     */
    fun buildNode(): LiteralCommandNode<CommandSourceStack> {
        
        // Extracted execution logic to maintain DRY principles across the command branches.
        val executeGui: (CommandContext<CommandSourceStack>) -> Int = { ctx ->
            // We can safely cast to Player here because of the .requires() predicate below.
            val player = ctx.source.sender as Player
            ConditionGuideGui.openMain(player)
            Command.SINGLE_SUCCESS
        }

        val executeStatus: (CommandContext<CommandSourceStack>) -> Int = { ctx ->
            val player = ctx.source.sender as Player
            ConditionGuideGui.openStatus(player)
            Command.SINGLE_SUCCESS
        }
        
        return Commands.literal("plagueandpain")
            // 1. Pre-execution filtering: Only players can see and execute this command.
            // The console will not even see it in tab-completions.
            .requires { source -> source.sender is Player }
            
            // 2. Base execution: /plagueandpain
            .executes(executeGui)
            
            // 3. Subcommand: /plagueandpain guide
            .then(Commands.literal("guide").executes(executeGui))
            
            // 4. Subcommand: /plagueandpain conditions
            .then(Commands.literal("conditions").executes(executeGui))
            .then(Commands.literal("status").executes(executeStatus))
            .build()
    }
}