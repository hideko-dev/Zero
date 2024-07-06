package dev.hideko.zero.text

import dev.hideko.zero.utility.ZeroReflection
import dev.hideko.zero.utility.ZeroReflection.Companion.sendPacket
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.lang.reflect.Method

class ZeroActionBar {

    companion object {
        fun send(
            ply: Player,
            message: String
        ) {
            if (!ply.isOnline) return

            val version = ZeroReflection.getVersion()
            val old = ZeroReflection.isOld()

            try {
                var packet: Any
                val packetPlayOutChatClass = Class.forName("net.minecraft.server.$version.PacketPlayOutChat")
                if (old) {
                    val chatSerializerClass = Class.forName("net.minecraft.server.$version.ChatSerializer")
                    val iChatBaseComponentClass = Class.forName("net.minecraft.server.$version.IChatBaseComponent")
                    val m3: Method = chatSerializerClass.getDeclaredMethod("a", String::class.java)
                    val cbc = iChatBaseComponentClass.cast(
                        m3.invoke(
                            chatSerializerClass,
                            "{\"text\": \"$message\"}"
                        )
                    )
                    packet = packetPlayOutChatClass.getConstructor(
                        *arrayOf(
                            iChatBaseComponentClass,
                            Byte::class.javaPrimitiveType
                        )
                    ).newInstance(cbc, 2.toByte())
                } else {
                    val chatComponentTextClass = Class.forName("net.minecraft.server.$version.ChatComponentText")
                    val iChatBaseComponentClass = Class.forName("net.minecraft.server.$version.IChatBaseComponent")
                    try {
                        val chatMessageTypeClass = Class.forName("net.minecraft.server.$version.ChatMessageType")
                        val chatMessageTypes: Array<out Any>? = chatMessageTypeClass.getEnumConstants()
                        var chatMessageType: Any? = null
                        if (chatMessageTypes != null) {
                            for (o in chatMessageTypes) {
                                if (o.toString() == "GAME_INFO") {
                                    chatMessageType = o
                                }
                            }
                        }
                        val chatCompontentText = chatComponentTextClass.getConstructor(
                            *arrayOf<Class<*>>(
                                String::class.java
                            )
                        ).newInstance(message)
                        packet =
                            packetPlayOutChatClass.getConstructor(*arrayOf(iChatBaseComponentClass, chatMessageTypeClass))
                                .newInstance(chatCompontentText, chatMessageType)
                    } catch (e: ClassNotFoundException) {
                        val chatCompontentText = chatComponentTextClass.getConstructor(
                            *arrayOf<Class<*>>(
                                String::class.java
                            )
                        ).newInstance(message)
                        packet = packetPlayOutChatClass.getConstructor(
                            *arrayOf(
                                iChatBaseComponentClass,
                                Byte::class.javaPrimitiveType
                            )
                        ).newInstance(chatCompontentText, 2.toByte())
                    }
                }
                sendPacket(ply, packet)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun clear(player: Player) {
            send(player, "")
        }
    }
}