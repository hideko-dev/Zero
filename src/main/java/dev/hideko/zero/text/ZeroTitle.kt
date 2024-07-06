package dev.hideko.zero.text

import dev.hideko.zero.reflection.ZeroReflection.Companion.getNMSClass
import dev.hideko.zero.reflection.ZeroReflection.Companion.sendPacket
import org.bukkit.entity.Player
import java.lang.reflect.Constructor

class ZeroTitle {

    companion object {
        fun send(
            ply: Player,
            title: String?,
            subtitle: String?,
            fadeIn: Int = 10,
            stay: Int = 30,
            fadeOut: Int = 10
        ) {

            if (!ply.isOnline) return

            try {
                var e: Any
                var chatTitle: Any
                var chatSubtitle: Any
                var subtitleConstructor: Constructor<*>
                var titlePacket: Any
                var subtitlePacket: Any

                if (title != null) {
                    e = getNMSClass("PacketPlayOutTitle")!!.declaredClasses[0].getField("TIMES").get(null)
                    chatTitle = getNMSClass("IChatBaseComponent")!!.declaredClasses[0].getMethod(
                        "a", String::class.java
                    ).invoke(null, "{\"text\":\"$title\"}")
                    subtitleConstructor = getNMSClass("PacketPlayOutTitle")!!.getConstructor(
                        getNMSClass("PacketPlayOutTitle")!!.declaredClasses[0],
                        getNMSClass("IChatBaseComponent"),
                        Int::class.javaPrimitiveType,
                        Int::class.javaPrimitiveType,
                        Int::class.javaPrimitiveType
                    )
                    titlePacket = subtitleConstructor.newInstance(e, chatTitle, fadeIn, stay, fadeOut)
                    sendPacket(ply, titlePacket)
                    e = getNMSClass("PacketPlayOutTitle")!!.declaredClasses[0].getField("TITLE").get(null)
                    chatTitle = getNMSClass("IChatBaseComponent")!!.declaredClasses[0].getMethod(
                        "a", String::class.java
                    ).invoke(null, "{\"text\":\"$title\"}")
                    subtitleConstructor = getNMSClass("PacketPlayOutTitle")!!.getConstructor(
                        getNMSClass("PacketPlayOutTitle")!!.declaredClasses[0],
                        getNMSClass("IChatBaseComponent")
                    )
                    titlePacket = subtitleConstructor.newInstance(e, chatTitle)
                    sendPacket(ply, titlePacket)
                }

                if (subtitle != null) {
                    e = getNMSClass("PacketPlayOutTitle")!!.declaredClasses[0].getField("TIMES").get(null)
                    chatSubtitle = getNMSClass("IChatBaseComponent")!!.declaredClasses[0].getMethod(
                        "a", String::class.java
                    ).invoke(null, "{\"text\":\"$subtitle\"}")
                    subtitleConstructor = getNMSClass("PacketPlayOutTitle")!!.getConstructor(
                        getNMSClass("PacketPlayOutTitle")!!.declaredClasses[0],
                        getNMSClass("IChatBaseComponent"),
                        Int::class.javaPrimitiveType,
                        Int::class.javaPrimitiveType,
                        Int::class.javaPrimitiveType
                    )
                    subtitlePacket = subtitleConstructor.newInstance(e, chatSubtitle, fadeIn, stay, fadeOut)
                    sendPacket(ply, subtitlePacket)

                    e = getNMSClass("PacketPlayOutTitle")!!.declaredClasses[0].getField("SUBTITLE").get(null)
                    chatSubtitle = getNMSClass("IChatBaseComponent")!!.declaredClasses[0].getMethod(
                        "a", String::class.java
                    ).invoke(null, "{\"text\":\"$subtitle\"}")
                    subtitleConstructor = getNMSClass("PacketPlayOutTitle")!!.getConstructor(
                        getNMSClass("PacketPlayOutTitle")!!.declaredClasses[0],
                        getNMSClass("IChatBaseComponent"),
                        Int::class.javaPrimitiveType,
                        Int::class.javaPrimitiveType,
                        Int::class.javaPrimitiveType
                    )
                    subtitlePacket = subtitleConstructor.newInstance(e, chatSubtitle, fadeIn, stay, fadeOut)
                    sendPacket(ply, subtitlePacket)
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
        }

        fun clear(ply: Player) {
            send(ply, "", "", 0, 0, 0)
        }
    }
}