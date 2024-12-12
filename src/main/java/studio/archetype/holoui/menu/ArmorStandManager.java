package studio.archetype.holoui.menu;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import studio.archetype.holoui.utils.ArmorStand;
import studio.archetype.holoui.utils.PacketUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ArmorStandManager {

    private static final Map<UUID, ArmorStand> armorStands = new HashMap<>();
    private static final Map<UUID, Player> playerVisibility = new HashMap<>();

    public static UUID add(ArmorStand stand) {
        UUID uuid = UUID.randomUUID();
        armorStands.put(uuid, stand);
        return uuid;
    }

    public static void spawn(UUID uuid, Player p) {
        if(!armorStands.containsKey(uuid))
            return;

        PacketUtils.send(p, armorStands.get(uuid).spawn());
        playerVisibility.put(uuid, p);
    }

    public static void despawn(UUID uuid) {
        if(!armorStands.containsKey(uuid) || !playerVisibility.containsKey(uuid))
            return;
        PacketUtils.send(playerVisibility.remove(uuid), armorStands.get(uuid).remove());
    }

    public static void delete(UUID uuid) {
        if(!armorStands.containsKey(uuid))
            return;

        despawn(uuid);
        armorStands.remove(uuid);
        playerVisibility.remove(uuid);
    }

    public static Vector location(UUID uuid) {
        if(!armorStands.containsKey(uuid))
            return new Vector();

        return PacketUtils.vector(armorStands.get(uuid).location());
    }

    public static void goTo(UUID uuid, Location loc) {
        if(!armorStands.containsKey(uuid))
            return;
        ArmorStand stand = armorStands.get(uuid);
        PacketUtils.send(playerVisibility.get(uuid), stand.goTo(loc));
    }

    public static void move(UUID uuid, Vector offset) {
        if(!armorStands.containsKey(uuid))
            return;
        ArmorStand stand = armorStands.get(uuid);
        PacketUtils.send(playerVisibility.get(uuid), stand.move(offset));
    }

    public static void changeName(UUID uuid, Component name) {
        if(!armorStands.containsKey(uuid))
            return;
        var packet = armorStands.get(uuid)
                .displayName(name)
                .dataPacket();
        PacketUtils.send(playerVisibility.get(uuid), packet);
    }

    public static void rotate(UUID uuid, float yaw) {
        if(!armorStands.containsKey(uuid))
            return;
        ArmorStand stand = armorStands.get(uuid);
        PacketUtils.send(playerVisibility.get(uuid), stand.rotate(yaw, stand.pitch()));
    }
}
