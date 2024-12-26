package com.volmit.holoui.utils;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.Collections;

public final class PacketUtils {

    public static void send(Player player, PacketWrapper<?> packet) {
        send(player, Collections.singletonList(packet));
    }

    public static void send(Player player, Collection<PacketWrapper<?>> packets) {
        send(Collections.singletonList(player), packets);
    }

    public static void send(Collection<Player> players, Collection<PacketWrapper<?>> packets) {
        var pm = PacketEvents.getAPI().getPlayerManager();
        players.forEach(player ->
                packets.forEach(packet ->
                        pm.sendPacket(player, packet)
                )
        );
    }

    public static Vector vector(Vector3d vector) {
        return new Vector(vector.getX(), vector.getY(), vector.getZ());
    }

    public static Vector3d vector3d(Vector vector) {
        return new Vector3d(vector.getX(), vector.getY(), vector.getZ());
    }
}
