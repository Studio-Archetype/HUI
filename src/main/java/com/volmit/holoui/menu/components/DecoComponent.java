package com.volmit.holoui.menu.components;

import com.volmit.holoui.config.MenuComponentData;
import com.volmit.holoui.config.components.DecoComponentData;
import com.volmit.holoui.menu.MenuSession;
import com.volmit.holoui.menu.icon.MenuIcon;

public class DecoComponent extends MenuComponent<DecoComponentData> {

    public DecoComponent(MenuSession session, MenuComponentData data) {
        super(session, data);
    }

    protected MenuIcon<?> createIcon() { return MenuIcon.createIcon(session, location, data.iconData(), this); }

    protected void onOpen() { }
    protected void onTick() { }
    protected void onClose() { }
}
