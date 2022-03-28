package studio.archetype.hologui2.utils;

import net.minecraft.network.protocol.Packet;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

public final class PacketUtils {

    public static void send(Player p, Packet<?> packet) {
        if(!p.isOnline())
            return;
        ((CraftPlayer)p).getHandle().connection.send(packet);
    }
}
