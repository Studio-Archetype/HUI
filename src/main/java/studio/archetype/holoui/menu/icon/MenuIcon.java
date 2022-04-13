package studio.archetype.holoui.menu.icon;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import studio.archetype.holoui.config.icon.ItemIconData;
import studio.archetype.holoui.config.icon.MenuIconData;
import studio.archetype.holoui.config.icon.TextIconData;
import studio.archetype.holoui.config.icon.TextImageIconData;
import studio.archetype.holoui.menu.ArmorStandManager;
import studio.archetype.holoui.utils.math.CollisionPlane;

import java.util.List;
import java.util.UUID;

public abstract class MenuIcon<D extends MenuIconData> {

    protected static final float NAMETAG_SIZE = 1 / 16F * 3.5F;

    protected final Player player;
    protected final D data;

    protected List<UUID> armorStands;
    protected Location position;

    public MenuIcon(Player p, Location loc, D data) {
        this.player = p;
        this.position = loc.clone();
        this.data = data;
    }

    protected abstract List<UUID> createArmorStands(Location loc);
    public abstract CollisionPlane createBoundingBox();

    public void spawn() {
        armorStands = createArmorStands(position.clone().subtract(0, NAMETAG_SIZE, 0));
        armorStands.forEach(a -> ArmorStandManager.spawn(a, player));
    }

    public void remove() {
        armorStands.forEach(ArmorStandManager::delete);
        armorStands.clear();
    }

    public void move(Vector offset) {
        if(armorStands != null && !armorStands.isEmpty())
            armorStands.forEach(a -> ArmorStandManager.move(a, offset));
        this.position.add(offset);
    }

    public void teleport(Location loc) {
        Vector offset = loc.toVector().subtract(position.toVector());
        move(offset);
    }

    public static MenuIcon<?> createIcon(Player p, Location loc, MenuIconData data) {
        if(data instanceof ItemIconData d)
            return new ItemMenuIcon(p, loc, d);
        else if(data instanceof TextImageIconData d)
            return new TextImageMenuIcon(p, loc, d);
        else if(data instanceof TextIconData d)
            return new TextMenuIcon(p, loc, d);
        return null;
    }
}
