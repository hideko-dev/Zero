package dev.hideko.zero.menu

import org.bukkit.entity.Player

class ZeroInventory(private val playerEntity: Player) {
    fun getPlayer(): Player {
        return playerEntity
    }
}
