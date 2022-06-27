package studio.archetype.holoui.menu.components;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import studio.archetype.holoui.config.HuiSettings;
import studio.archetype.holoui.config.MenuComponentData;
import studio.archetype.holoui.config.components.ButtonComponentData;
import studio.archetype.holoui.config.components.ComponentData;
import studio.archetype.holoui.config.components.DecoComponentData;
import studio.archetype.holoui.config.components.ToggleComponentData;
import studio.archetype.holoui.menu.MenuSession;
import studio.archetype.holoui.menu.icon.MenuIcon;
import studio.archetype.holoui.menu.special.BlockMenuSession;
import studio.archetype.holoui.menu.special.inventories.InventoryProgressComponent;
import studio.archetype.holoui.menu.special.inventories.InventorySlotComponent;
import studio.archetype.holoui.utils.math.MathHelper;

public abstract class MenuComponent<T extends ComponentData> {

    protected final MenuSession session;
    protected final Vector offset;
    protected final T data;

    @Getter
    protected final String id;

    @Getter
    protected Location location;
    protected MenuIcon<?> currentIcon;

    @SuppressWarnings("unchecked")
    public MenuComponent(MenuSession session, MenuComponentData data) {
        this.session = session;
        this.id = data.id();
        this.offset = data.offset().clone().multiply(new Vector(-1, 1, 1));
        this.data = (T)data.data();

        this.location = session.getCenterPoint().clone().add(offset);
    }

    public void tick() {
        onTick();
        if(currentIcon != null)
            currentIcon.tick();
    }

    public abstract void onTick();

    protected abstract MenuIcon<?> createIcon();
    protected abstract void onOpen();
    protected abstract void onClose();

    public void open(boolean rotateByPlayer) {
        adjustRotation(rotateByPlayer);
        this.currentIcon = createIcon();
        this.currentIcon.spawn();
        onOpen();
    }

    public void close() {
        if(this.currentIcon != null)
            this.currentIcon.remove();
        onClose();
    }

    public void adjustRotation(boolean byPlayer) {
        if(byPlayer)
            rotateByPlayer();
        else
            rotateByCenter();
        if(this.currentIcon != null)
            this.currentIcon.teleport(location);
    }

    public void move(Location loc) {
        this.location = loc.add(offset);
    }

    public void rotate(float yaw) {
        if(this.currentIcon != null)
            this.currentIcon.rotate(yaw);
    }

    public static MenuComponent<?> getComponent(MenuSession session, MenuComponentData data) {
        if(data.data() instanceof ButtonComponentData)
            return new ButtonComponent(session, data);
        else if(data.data() instanceof DecoComponentData)
            return new DecoComponent(session, data);
        else if(data.data() instanceof ToggleComponentData)
            return new ToggleComponent(session, data);
        else if(data.data() instanceof InventorySlotComponent.Data)
            return new InventorySlotComponent(session, data);
        else if(data.data() instanceof InventoryProgressComponent.Data)
            return new InventoryProgressComponent(session, data);
        else
            return null;
    }

    protected void rotateByPlayer() {
        MathHelper.rotateAroundPoint(this.location, session.getPlayer().getEyeLocation(), 0, session.getInitialY());
    }

    protected void rotateByCenter() {
        if(session instanceof BlockMenuSession)
            MathHelper.rotateAroundPoint(this.location, session.getCenterPoint(), 0, session.getInitialY());
    }
}
