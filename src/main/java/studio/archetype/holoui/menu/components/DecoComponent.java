package studio.archetype.holoui.menu.components;

import studio.archetype.holoui.config.MenuComponentData;
import studio.archetype.holoui.config.components.DecoComponentData;
import studio.archetype.holoui.menu.MenuSession;
import studio.archetype.holoui.menu.icon.MenuIcon;

public class DecoComponent extends MenuComponent<DecoComponentData> {

    public DecoComponent(MenuSession session, MenuComponentData data) {
        super(session, data);
    }

    protected MenuIcon<?> createIcon() { return MenuIcon.createIcon(session.getPlayer(), location, data.iconData(), this); }

    protected void onOpen() { }
    public void tick() { }
    protected void onClose() { }
}
