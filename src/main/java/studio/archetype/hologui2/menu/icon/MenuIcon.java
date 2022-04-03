package studio.archetype.hologui2.menu.icon;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import studio.archetype.hologui2.config.icon.ItemIconData;
import studio.archetype.hologui2.config.icon.MenuIconData;
import studio.archetype.hologui2.config.icon.TextIconData;
import studio.archetype.hologui2.config.icon.TextImageIconData;
import studio.archetype.hologui2.menu.ArmorStandManager;
import studio.archetype.hologui2.utils.math.CollisionPlane;

import java.util.List;
import java.util.UUID;

public abstract class MenuIcon<D extends MenuIconData> {

    protected static final float NAMETAG_SIZE = 1 / 16F * 3.5F;

    protected final D data;

    protected List<UUID> armorStands;

    public MenuIcon(D data) {
        this.data = data;
    }

    protected abstract List<UUID> createArmorStands(Location loc);
    protected abstract CollisionPlane createBoundingBox(Location loc);

    public CollisionPlane spawn(Player p, Location loc) {
        armorStands = createArmorStands(loc.clone().subtract(0, NAMETAG_SIZE, 0));
        armorStands.forEach(a -> ArmorStandManager.spawn(a, p));
        CollisionPlane b = createBoundingBox(loc.clone().add(0, NAMETAG_SIZE, 0));
        Location player = p.getEyeLocation();
        double pitch = Math.atan2(player.getY(), player.getX()) - Math.atan2(b.getCenter().getY(), b.getCenter().getZ());
        b.rotate((float)pitch, 0);
        return b;
    }

    public void remove() {
        armorStands.forEach(ArmorStandManager::delete);
        armorStands.clear();
    }

    public static MenuIcon<?> createIcon(MenuIconData data) {
        if(data instanceof ItemIconData d)
            return new ItemMenuIcon(d);
        else if(data instanceof TextImageIconData d)
            return new TextImageMenuIcon(d);
        else if(data instanceof TextIconData d)
            return new TextMenuIcon(d);
        return null;
    }
}
