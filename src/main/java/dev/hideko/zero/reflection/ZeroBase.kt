package dev.hideko.zero.reflection

import org.bukkit.command.CommandExecutor
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

class ZeroBase(private val plugin: JavaPlugin) {

    fun events(events: List<Listener>): ZeroBase {
        events.forEach { event ->
            plugin.server.pluginManager.registerEvents(event, plugin)
        }
        return this
    }

    fun commands(commands: List<Pair<String, CommandExecutor>>): ZeroBase {
        commands.forEach { (name, executor) ->
            plugin.getCommand(name)?.executor = executor
        }
        return this
    }

}