package studio.archetype.holoui.menu.special.inventories;

import com.google.common.collect.Lists;
import org.bukkit.block.*;
import org.bukkit.inventory.Inventory;
import studio.archetype.holoui.config.MenuComponentData;

import java.util.List;

public class HopperPreview implements InventoryPreviewMenu<Inventory> {

    private static final float X_START = -1F;

    @Override
    public void supply(Container b, List<MenuComponentData> components) {
        Inventory inv = getInventory(b);
        for(int i = 0; i < 5; i++)
            components.add(component("slot" + (i), X_START + (i * .5F), .25F, 0, new InventorySlotComponent.Data(inv, i)));
    }

    @Override
    public boolean isValid(Container b) {
        return b instanceof Hopper;
    }
}
