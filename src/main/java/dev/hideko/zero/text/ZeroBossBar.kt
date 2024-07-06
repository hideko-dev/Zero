package dev.hideko.zero.text

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.reflect.StructureModifier
import com.comphenix.protocol.wrappers.WrappedDataWatcher
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable


class ZeroBossBar {

    companion object {
        private var enderdragonId = 0
        private val plugin: Plugin = Bukkit.getPluginManager().plugins[0]
        private val toHide = HashMap<String, BukkitRunnable>()

        init {
            try {
                val field = Class.forName(
                    "net.minecraft.server." + Bukkit.getServer().javaClass.name.split("\\.".toRegex())
                        .dropLastWhile { it.isEmpty() }
                        .toTypedArray()[3] + ".Entity")
                    .getDeclaredField("entityCount")
                field.setAccessible(true)
                enderdragonId = field.getInt(null)
                field.set(null, enderdragonId + 1)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

        @JvmOverloads
        fun removeBar(player: Player, afterTicks: Int = 2) {
            if (player.hasMetadata("SeesEnderdragon") && !toHide.containsKey(player.name)) {
                val runnable: BukkitRunnable = object : BukkitRunnable() {
                    override fun run() {
                        player.removeMetadata("SeesEnderdragon", plugin)
                        sendRemovePacket(player)
                        toHide.remove(player.name)
                    }
                }
                runnable.runTaskLater(plugin, afterTicks.toLong())
                toHide[player.name] = runnable
            }
        }

        private fun sendRemovePacket(player: Player) {
            try {
                val spawnPacket = PacketContainer(PacketType.Play.Server.ENTITY_DESTROY)
                spawnPacket.integerArrays.write(0, intArrayOf(enderdragonId))
                ProtocolLibrary.getProtocolManager().sendServerPacket(player, spawnPacket, false)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

        @Throws(Exception::class)
        private fun sendSpawnPacket(player: Player, message: String, health: Float) {
            val spawnPacket: PacketContainer = PacketContainer(PacketType.Play.Server.SPAWN_ENTITY_LIVING)
            val spawnPacketModifier: StructureModifier<Any> = spawnPacket.getModifier()
            val toSpawn = player.eyeLocation.add(player.eyeLocation.direction.normalize().multiply(23))
            spawnPacketModifier.write(0, enderdragonId)
            spawnPacketModifier.write(1, 64.toByte()) // EntityID of wither
            spawnPacketModifier.write(2, toSpawn.getBlockX() * 32)
            spawnPacketModifier.write(3, toSpawn.getBlockY() * 32)
            spawnPacketModifier.write(4, toSpawn.getBlockZ() * 32)
            val watcher = WrappedDataWatcher()
            watcher.setObject(0, 32.toByte())
            watcher.setObject(2, message)
            watcher.setObject(6, health, true) // Set health
            watcher.setObject(10, message)
            watcher.setObject(20, 881)
            spawnPacket.getDataWatcherModifier().write(0, watcher)
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, spawnPacket, false)
        }

        fun setName(player: Player, message: String, health: Float) {
            try {
                if (!player.hasMetadata("SeesEnderdragon")) {
                    player.setMetadata("SeesEnderdragon", FixedMetadataValue(plugin, true))
                }
                if (toHide.containsKey(player.name)) {
                    toHide.remove(player.name)!!.cancel()
                }
                sendSpawnPacket(player, message, health)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }
}