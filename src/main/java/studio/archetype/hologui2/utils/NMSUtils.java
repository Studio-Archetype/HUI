package studio.archetype.hologui2.utils;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;

public final class NMSUtils {

    public static ServerLevel level(World world) {
        return ((CraftWorld)world).getHandle();
    }

    public static ItemStack notchianItemStack(org.bukkit.inventory.ItemStack stack) {
        return CraftItemStack.asNMSCopy(stack);
    }

    public static Vec3 vec3(Location loc) {
        return new Vec3(loc.getX(), loc.getY(), loc.getZ());
    }
}
