package studio.archetype.holoui.menu.special.inventories;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import studio.archetype.holoui.config.MenuComponentData;
import studio.archetype.holoui.config.components.ComponentData;
import studio.archetype.holoui.config.icon.ItemIconData;
import studio.archetype.holoui.enums.MenuComponentType;
import studio.archetype.holoui.menu.MenuSession;
import studio.archetype.holoui.menu.components.MenuComponent;
import studio.archetype.holoui.menu.icon.ItemMenuIcon;
import studio.archetype.holoui.menu.icon.MenuIcon;

public class InventorySlotComponent extends MenuComponent<InventorySlotComponent.Data> {

    private static final ItemStack MISSING = new ItemStack(Material.BARRIER);

    private ItemStack currentStack;

    public InventorySlotComponent(MenuSession session, MenuComponentData data) {
        super(session, data);
        ItemStack item =  this.data.inventory().getItem(this.data.slotId());
        if(item == null)
            currentStack = MISSING;
        else
            currentStack = item.clone();
    }

    @Override
    public void onTick() {
        ItemStack stack = data.inventory().getItem(data.slotId());
        if(stack == null && currentStack != MISSING) {
            this.currentStack = MISSING;
            updateDisplay();
            return;
        }

        if(stack != null && currentStack.isSimilar(stack) && currentStack.getAmount() != stack.getAmount()) {
            this.currentStack.setAmount(stack.getAmount());
            ((ItemMenuIcon)currentIcon).updateCount(stack.getAmount());
            return;
        }

        if(stack != null && !currentStack.equals(stack)) {
            if(stack.getType() == Material.AIR || stack.getAmount() < 1)
                this.currentStack = MISSING;
            else
                this.currentStack = stack.clone();
            updateDisplay();
        }
    }

    @Override
    protected MenuIcon<?> createIcon() {
        return MenuIcon.createIcon(session, getLocation(), ItemIconData.of(currentStack, true), this);
    }

    protected void onOpen() { }
    protected void onClose() { }

    private void updateDisplay() {
        if(currentIcon != null)
            this.currentIcon.remove();
        this.currentIcon = MenuIcon.createIcon(session, getLocation(), ItemIconData.of(currentStack, true), this);
        this.currentIcon.teleport(location.clone());
        this.currentIcon.spawn();
    }

    public record Data(Inventory inventory, int slotId) implements ComponentData {
        public MenuComponentType getType() { return null; }
    }
}
