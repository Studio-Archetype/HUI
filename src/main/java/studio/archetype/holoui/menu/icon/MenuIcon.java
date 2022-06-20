package studio.archetype.holoui.menu.icon;

import io.undertow.server.session.Session;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import studio.archetype.holoui.HoloUI;
import studio.archetype.holoui.config.icon.*;
import studio.archetype.holoui.exceptions.MenuIconException;
import studio.archetype.holoui.menu.ArmorStandManager;
import studio.archetype.holoui.menu.MenuSession;
import studio.archetype.holoui.menu.components.MenuComponent;
import studio.archetype.holoui.utils.math.CollisionPlane;
import studio.archetype.holoui.utils.math.MathHelper;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public abstract class MenuIcon<D extends MenuIconData> {

    protected static final float NAMETAG_SIZE = 1 / 16F * 3.5F;

    protected final MenuSession session;
    protected final D data;

    protected List<UUID> armorStands;
    protected Location position;

    public MenuIcon(MenuSession session, Location loc, D data) throws MenuIconException {
        this.session = session;
        this.position = loc.clone();
        this.data = data;
    }

    protected abstract List<UUID> createArmorStands(Location loc);
    public abstract CollisionPlane createBoundingBox();

    public void tick() { }

    public void spawn() {
        armorStands = createArmorStands(position.clone().subtract(0, NAMETAG_SIZE, 0));
        armorStands.forEach(a -> ArmorStandManager.spawn(a, session.getPlayer()));
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

    public void rotate(float yaw) {
        if(armorStands != null && !armorStands.isEmpty())
            armorStands.forEach(a -> ArmorStandManager.rotate(a, yaw));
    }

    public void teleport(Location loc) {
        Vector offset = loc.toVector().subtract(position.toVector());
        move(offset);
    }

    @NonNull
    public static MenuIcon<?> createIcon(MenuSession session, Location loc, MenuIconData data, MenuComponent<?> component) {
        try {
            if(data instanceof ItemIconData d)
                return new ItemMenuIcon(session, loc, d);
            else if(data instanceof TextImageIconData d)
                return new TextImageMenuIcon(session, loc, d);
            else if(data instanceof TextIconData d)
                return new TextMenuIcon(session, loc, d);
            else if(data instanceof AnimatedImageData d)
                return new AnimatedTextImageMenuIcon(session, loc, d);
            return new TextImageMenuIcon(session, loc);
        } catch(MenuIconException e) {
            HoloUI.logExceptionStack(false, e, "An error occurred while creating a Menu Icon for the component \"%s\":", component.getId());
            HoloUI.log(Level.WARNING, "Falling back to missing icon.");
            try {
                return new TextImageMenuIcon(session, loc);
            } catch(MenuIconException ignored) {
                //noinspection ConstantConditions
                return null;
            }
        }
    }
}
