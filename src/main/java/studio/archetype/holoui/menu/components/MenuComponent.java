package studio.archetype.holoui.menu.components;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import studio.archetype.holoui.config.MenuComponentData;
import studio.archetype.holoui.config.components.ButtonComponentData;
import studio.archetype.holoui.config.components.ComponentData;
import studio.archetype.holoui.config.components.DecoComponentData;
import studio.archetype.holoui.config.components.ToggleComponentData;
import studio.archetype.holoui.menu.MenuSession;
import studio.archetype.holoui.menu.icon.MenuIcon;
import studio.archetype.holoui.utils.math.MathHelper;

public abstract class MenuComponent<T extends ComponentData> {

    protected final MenuSession session;
    protected final String id;
    protected final Vector offset;
    protected final T data;

    protected Location location;
    protected MenuIcon<?> currentIcon;

    @SuppressWarnings("unchecked-cast")
    public MenuComponent(MenuSession session, MenuComponentData data) {
        this.session = session;
        this.id = data.id();
        this.offset = data.offset();
        this.data = (T)data.data();

        this.location = MathHelper.rotateAroundPoint(
                session.getCenterPoint().clone().add(offset),
                session.getPlayer().getEyeLocation(),
                0, session.getPlayer().getLocation().getYaw());
    }

    public abstract void tick();

    protected abstract MenuIcon<?> createIcon();
    protected abstract void onOpen();
    protected abstract void onClose();

    public void open() {
        this.currentIcon = createIcon();
        this.currentIcon.spawn();
        onOpen();
    }

    public void close() {
        this.currentIcon.remove();
        onClose();
    }

    public void move(Location loc) {
        this.location = loc.add(offset);
        this.currentIcon.teleport(location);
    }

    public static MenuComponent<?> getComponent(MenuSession session, MenuComponentData data) {
        if(data.data() instanceof ButtonComponentData)
            return new ButtonComponent(session, data);
        else if(data.data() instanceof DecoComponentData)
            return new DecoComponent(session, data);
        else if(data.data() instanceof ToggleComponentData)
            return new ToggleComponent(session, data);
        else
            return null;
    }
}
