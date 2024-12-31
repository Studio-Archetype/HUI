package com.volmit.holoui.menu.special.inventories;

import com.volmit.holoui.config.MenuComponentData;
import org.bukkit.block.Container;
import org.bukkit.block.Hopper;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class HopperPreview implements InventoryPreviewMenu<Inventory> {

    private static final float X_START = -1F;

    @Override
    public void supply(Container b, List<MenuComponentData> components) {
        Inventory inv = getInventory(b);
        for (int i = 0; i < 5; i++)
            components.add(component("slot" + (i), X_START + (i * .5F), .25F, 0, new InventorySlotComponent.Data(inv, i)));
    }

    @Override
    public boolean isValid(Container b) {
        return b instanceof Hopper;
    }
}
