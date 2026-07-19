package fr.ateastudio.plagueandpain

import fr.ateastudio.plagueandpain.command.ConditionGuideCommand
import io.papermc.paper.plugin.bootstrap.BootstrapContext
import io.papermc.paper.plugin.bootstrap.PluginBootstrap
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents

@Suppress("unused", "UnstableApiUsage")
class PlagueAndPainBootstrap : PluginBootstrap {
    
    override fun bootstrap(context: BootstrapContext) {
        context.lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS,  { commands ->
            val registrar = commands.registrar()
            
            registrar.register(
                ConditionGuideCommand.buildNode(),
                "Opens the disease and injury guide",
                listOf("ppguide", "pp", "pap", "plague", "pain") // Optional base command aliases
            )
        })
    }
    
}