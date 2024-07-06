package dev.hideko.zero

import dev.hideko.zero.menu.ZeroMenuEvent
import org.bukkit.plugin.java.JavaPlugin

object Zero {

    private lateinit var plugin: JavaPlugin

    fun init(plugin: JavaPlugin) {
        this.plugin = plugin
        plugin.server.pluginManager.registerEvents(ZeroMenuEvent(), plugin)
    }

    fun plugin(): JavaPlugin {
        return plugin
    }

}