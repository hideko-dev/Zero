package dev.hideko.zero

import dev.hideko.zero.Zero
import dev.hideko.zero.menu.ZeroMenu
import dev.hideko.zero.text.ZeroActionBar
import dev.hideko.zero.text.ZeroTitle
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

class Test: JavaPlugin() {

    override fun onEnable() {
        Zero.init(this)
    }

    fun openMenu(ply: Player) {
        ZeroMenu.create(9)
            .setTitle("Hello World")
            .inventory {
                set(0, ItemStack(Material.GRASS)) { event ->
                    event.player.sendMessage("You Clicked!!")
                }
                set(1, ItemStack(Material.WOOD)) { event ->
                    event.player.sendMessage("You Clicked!!")
                }
            }
            .ignore(0)
            .open(ply)

        ZeroTitle.send(ply, "§e§lA", "§9§le", 10, 80, 10)
        ZeroActionBar.send(ply, "§b§lHello §e§lWorld!")
    }

}