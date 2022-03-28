package studio.archetype.hologui2.utils;

import net.minecraft.resources.ResourceLocation;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public final class ItemUtils {

    public static Material identifierToMaterial(ResourceLocation loc) {
        return Material.valueOf(loc.getPath().toUpperCase());
    }

    public static ResourceLocation materialToIdentifier(Material m) {
        return new ResourceLocation(m.name().toLowerCase());
    }

    public static final class Builder {

        private final ItemStack stack;
        private final ItemMeta meta;

        public Builder(Material m) {
            this(m, 1);
        }

        public Builder(Material m, int amount) {
            this.stack = new ItemStack(m, amount);
            this.meta = stack.getItemMeta();
        }

        public Builder modelData(int data) {
            meta.setCustomModelData(data);
            return this;
        }

        public Builder damage(int damage) {
            if(meta instanceof Damageable m)
                m.setDamage(damage);
            return this;
        }

        public ItemStack get() {
            stack.setItemMeta(meta);
            return stack;
        }

        public net.minecraft.world.item.ItemStack getNotchian() {
            return CraftItemStack.asNMSCopy(get());
        }
    }
}
