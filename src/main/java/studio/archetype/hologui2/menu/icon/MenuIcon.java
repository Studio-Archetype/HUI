package studio.archetype.hologui2.menu.icon;

import com.google.common.collect.Lists;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import studio.archetype.hologui2.config.icon.ItemIconData;
import studio.archetype.hologui2.config.icon.MenuIconData;
import studio.archetype.hologui2.config.icon.TextImageIconData;
import studio.archetype.hologui2.menu.ArmorStandManager;
import studio.archetype.hologui2.utils.ArmorStandBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class MenuIcon<D extends MenuIconData> {

    protected static final float NAMETAG_SIZE = 1 / 16F * 3.5F;

    protected final Component name;
    protected final D data;

    protected List<UUID> armorStands;

    public MenuIcon(String translationKey, D data) {
        this.name = new TranslatableComponent(translationKey);
        this.data = data;
    }

    protected abstract List<UUID> createArmorStands(Location loc);

    public void spawn(Player p, Location loc) {
        armorStands = createArmorStands(loc.clone().subtract(0, NAMETAG_SIZE / 2, 0));
        armorStands.forEach(a -> ArmorStandManager.spawn(a, p));
    }

    public void remove() {
        armorStands.forEach(ArmorStandManager::delete);
        armorStands.clear();
    }

    public static ArmorStand nametagArmorStand(Component component, Location loc) {
        return new ArmorStandBuilder(loc.getWorld())
                .marker(true)
                .invisible(true)
                .basePlate(false)
                .zeroPose()
                .gravity(false)
                .name(component, true)
                .pos(loc)
                .build();
    }

    public static MenuIcon<?> createIcon(String translationKey, MenuIconData data) {
        if(data instanceof ItemIconData d)
            return new ItemMenuIcon(translationKey, d);
        else if(data instanceof TextImageIconData d)
            return new TextImageMenuIcon(translationKey, d);
            return null;
    }
}
