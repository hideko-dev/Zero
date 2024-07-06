package dev.hideko.zero.menu

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

class ZeroMenuEvent: Listener {

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val inventory = event.inventory
        val menu = ZeroMenuHolder[inventory]
        menu?.handleInventoryClick(event)
    }

}