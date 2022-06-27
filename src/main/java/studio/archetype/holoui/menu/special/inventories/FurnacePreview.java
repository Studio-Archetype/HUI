package studio.archetype.holoui.menu.special.inventories;

import net.minecraft.ChatFormatting;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.bukkit.Material;
import org.bukkit.block.BlastFurnace;
import org.bukkit.block.Container;
import org.bukkit.block.Smoker;
import org.bukkit.craftbukkit.v1_19_R1.util.CraftMagicNumbers;
import org.bukkit.inventory.FurnaceInventory;
import studio.archetype.holoui.config.MenuComponentData;
import studio.archetype.holoui.config.components.DecoComponentData;
import studio.archetype.holoui.config.icon.TextIconData;

import java.awt.*;
import java.util.List;

public class FurnacePreview implements InventoryPreviewMenu<FurnaceInventory> {

    @Override
    public void supply(Container container, List<MenuComponentData> components) {
        FurnaceInventory inv = getInventory(container);
        components.add(component("cookProgress", 0, 0.65F, 0, new InventoryProgressComponent.Data(inv, i -> {
                    FurnaceInventory furnace = (FurnaceInventory)i;
                    return (double)furnace.getHolder().getCookTime() / (float)furnace.getHolder().getCookTimeTotal();
                }, 40, ChatFormatting.WHITE)));
        components.add(component("input", -.8F, 0.25F, 0, new InventorySlotComponent.Data(inv, 0)));
        components.add(component("fuel", -.3F, 0.25F, 0, new InventorySlotComponent.Data(inv, 1)));
        components.add(component("progressArrow", .25F, 0.25F, 0, new DecoComponentData(new TextIconData("--->"))));
        components.add(component("output", .9F, 0.25F, 0, new InventorySlotComponent.Data(inv, 2)));
        components.add(component("fuelProgress", 0, -.15F, 0, new InventoryProgressComponent.Data(inv, i -> {
            FurnaceInventory furnace = (FurnaceInventory)i;
            if(furnace.getFuel() == null)
                return 0D;
            double burnTime = (double)furnace.getHolder().getBurnTime() * (i.getHolder() instanceof BlastFurnace || i.getHolder() instanceof Smoker ? 2 : 1);
            return burnTime / (float)getBurnTime(furnace.getFuel().getType());
        }, 40, ChatFormatting.GOLD)));
    }

    @Override
    public boolean isValid(Container b) {
        return b.getInventory() instanceof FurnaceInventory;
    }

    private int getBurnTime(Material m) {
        return AbstractFurnaceBlockEntity.getFuel().getOrDefault(CraftMagicNumbers.getItem(m), 20);
    }
}
