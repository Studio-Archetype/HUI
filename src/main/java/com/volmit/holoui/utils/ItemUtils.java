package com.volmit.holoui.utils;

import com.volmit.holoui.utils.registries.RegistryUtil;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public final class ItemUtils {

    public static Material identifierToMaterial(NamespacedKey loc) {
        return RegistryUtil.find(Material.class, loc);
    }

    public static NamespacedKey materialToIdentifier(Material m) {
        return m.getKey();
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
            if (meta == null)
                return this;
            meta.setCustomModelData(data);
            return this;
        }

        public Builder damage(int damage) {
            if (meta == null)
                return this;
            if (meta instanceof Damageable m)
                m.setDamage(damage);
            return this;
        }

        public ItemStack get() {
            if (meta != null)
                stack.setItemMeta(meta);
            return stack;
        }
    }
}
