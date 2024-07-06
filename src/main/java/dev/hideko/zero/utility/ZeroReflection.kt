package dev.hideko.zero.utility

import org.bukkit.Bukkit
import org.bukkit.entity.Player

class ZeroReflection {

    companion object {

        fun getNMSClass(name: String): Class<*>? {
            val version =
                Bukkit.getServer().javaClass.getPackage().name.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()[3]
            try {
                return Class.forName("net.minecraft.server.$version.$name")
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
                return null
            }
        }

        fun sendPacket(player: Player, packet: Any?) {
            try {
                val handle = player.javaClass.getMethod("getHandle").invoke(player)
                val playerConnection = handle.javaClass.getField("playerConnection")[handle]
                playerConnection.javaClass.getMethod("sendPacket", getNMSClass("Packet"))
                    .invoke(playerConnection, packet)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        fun getVersion(): String {
            val currentVersion = Bukkit.getServer().javaClass.getPackage().name
            val version = currentVersion.substring(currentVersion.lastIndexOf(".") + 1)
            return version
        }

        fun isOld(): Boolean {
            return getVersion().equals("v1_8_R1", true) || getVersion().startsWith("v1_7_")
        }

    }
}