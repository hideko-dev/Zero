package dev.hideko.zero.menu

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class ZeroMenu private constructor(private val size: Int, private val title: String) {

    private val inventory: Inventory = Bukkit.createInventory(null, size, title)
    private val slotActions: MutableMap<Int, (ZeroInventory) -> Unit> = mutableMapOf()
    private val ignoredSlots: MutableSet<Int> = mutableSetOf()

    init {
        ZeroMenuHolder[inventory] = this
    }

    fun inventory(configure: ZeroMenu.() -> Unit): ZeroMenu {
        this.configure()
        return this
    }

    fun set(slot: Int, item: ItemStack, onClick: (ZeroInventory) -> Unit) {
        inventory.setItem(slot, item)
        slotActions[slot] = onClick
    }

    fun ignore(vararg slots: Int): ZeroMenu {
        ignoredSlots.addAll(slots.toList())
        return this
    }

    fun open(player: Player) {
        player.openInventory(inventory)
    }

    fun handleInventoryClick(event: InventoryClickEvent) {
        if (event.clickedInventory == inventory) {
            if (!ignoredSlots.contains(event.slot)) {
                event.isCancelled = true
            }
            slotActions[event.slot]?.invoke(ZeroInventory(event.whoClicked as Player))
        }
    }

    companion object {
        fun create(size: Int, title: String): ZeroMenu {
            return ZeroMenu(size, title)
        }
    }
}