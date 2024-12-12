package studio.archetype.holoui.menu.special.inventories;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import org.bukkit.block.Container;
import org.bukkit.inventory.FurnaceInventory;
import studio.archetype.holoui.config.MenuComponentData;
import studio.archetype.holoui.config.components.DecoComponentData;
import studio.archetype.holoui.config.icon.TextIconData;

import java.util.List;

public class FurnacePreview implements InventoryPreviewMenu<FurnaceInventory> {

    @Override
    public void supply(Container container, List<MenuComponentData> components) {
        FurnaceInventory inv = getInventory(container);
        components.add(component("cookProgress", 0, 0.65F, 0, new InventoryProgressComponent.Data(inv, i -> {
                    FurnaceInventory furnace = (FurnaceInventory)i;
                    return (double)furnace.getHolder().getCookTime() / (float)furnace.getHolder().getCookTimeTotal();
                }, 40, Style.style(NamedTextColor.WHITE))));
        components.add(component("input", -.8F, 0.25F, 0, new InventorySlotComponent.Data(inv, 0)));
        components.add(component("fuel", -.3F, 0.25F, 0, new InventorySlotComponent.Data(inv, 1)));
        components.add(component("progressArrow", .25F, 0.25F, 0, new DecoComponentData(new TextIconData("--->"))));
        components.add(component("output", .9F, 0.25F, 0, new InventorySlotComponent.Data(inv, 2)));
    }

    @Override
    public boolean isValid(Container b) {
        return b.getInventory() instanceof FurnaceInventory;
    }
}
