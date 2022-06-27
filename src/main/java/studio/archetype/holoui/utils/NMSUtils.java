package studio.archetype.holoui.utils;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack;
import org.bukkit.util.Vector;

public final class NMSUtils {

    public static ServerLevel level(World world) {
        return ((CraftWorld)world).getHandle();
    }

    public static ItemStack notchianItemStack(org.bukkit.inventory.ItemStack stack) {
        return CraftItemStack.asNMSCopy(stack);
    }

    public static MutableComponent notchianComponent(BaseComponent component) {
        String json = ComponentSerializer.toString(component);
        return Component.Serializer.fromJson(json);
    }

    public static Vector vector(Vec3 vec) {
        return new Vector(vec.x(), vec.y(), vec.z());
    }

    public static Vec3 vec3(Location loc) {
        return vec3(loc.toVector());
    }

    public static Vec3 vec3(Vector vec) {
        return new Vec3(vec.getX(), vec.getY(), vec.getZ());
    }
}
