package studio.archetype.holoui.menu.special.inventories;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import org.bukkit.inventory.Inventory;
import studio.archetype.holoui.config.MenuComponentData;
import studio.archetype.holoui.config.components.ComponentData;
import studio.archetype.holoui.config.icon.TextIconData;
import studio.archetype.holoui.enums.MenuComponentType;
import studio.archetype.holoui.menu.MenuSession;
import studio.archetype.holoui.menu.components.MenuComponent;
import studio.archetype.holoui.menu.icon.MenuIcon;
import studio.archetype.holoui.menu.icon.TextMenuIcon;

import java.util.function.Function;

public class InventoryProgressComponent extends MenuComponent<InventoryProgressComponent.Data> {

    private final Inventory inventory;
    private final Function<Inventory, Double> progress;
    private final int segmentCount;
    private final Style color;

    private double prevProg;

    public InventoryProgressComponent(MenuSession session, MenuComponentData data) {
        super(session, data);
        this.inventory = this.data.inv();
        this.progress = this.data.progress();
        this.segmentCount = this.data.segments();
        this.color = this.data.color();
    }

    @Override
    public void onTick() {
        double progress = this.progress.apply(inventory);
        if(prevProg == progress)
            return;
        ((TextMenuIcon)currentIcon).updateName(0, getBar(progress));
        this.prevProg = progress;
    }

    @Override
    protected MenuIcon<?> createIcon() {
        return MenuIcon.createIcon(session, location, new TextIconData(""), this);
    }

    protected void onOpen() {
        double progress = this.progress.apply(inventory);
        ((TextMenuIcon)currentIcon).updateName(0, getBar(progress));
        this.prevProg = progress;
    }

    protected void onClose() { }

    private Component getBar(double invProgress) {
        int progress = (int)(invProgress * segmentCount);
        var c = Component.text();
        for(int i = 0; i < segmentCount; i++) {
            Style s = i < progress ? this.color : Style.style(NamedTextColor.DARK_GRAY);
            c.append(Component.text("|").style(s));
        }

        return c.build();
    }

    public record Data(Inventory inv, Function<Inventory, Double> progress, int segments, Style color) implements ComponentData {
        public MenuComponentType getType() { return null; }
    }
}
