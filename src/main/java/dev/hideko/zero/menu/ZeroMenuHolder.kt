package dev.hideko.zero.menu

import org.bukkit.inventory.Inventory

object ZeroMenuHolder {
    private val menuMap: MutableMap<Inventory, ZeroMenu> = mutableMapOf()

    operator fun get(inventory: Inventory): ZeroMenu? {
        return menuMap[inventory]
    }

    operator fun set(inventory: Inventory, menu: ZeroMenu) {
        menuMap[inventory] = menu
    }
}