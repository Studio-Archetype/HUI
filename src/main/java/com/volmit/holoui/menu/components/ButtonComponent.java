package com.volmit.holoui.menu.components;

import com.google.common.collect.Lists;
import com.volmit.holoui.config.MenuComponentData;
import com.volmit.holoui.config.components.ButtonComponentData;
import com.volmit.holoui.menu.MenuSession;
import com.volmit.holoui.menu.action.MenuAction;
import com.volmit.holoui.menu.icon.MenuIcon;

import java.util.List;

public class ButtonComponent extends ClickableComponent<ButtonComponentData> {

    private final List<MenuAction<?>> actions;

    public ButtonComponent(MenuSession session, MenuComponentData data) {
        super(session, data, ((ButtonComponentData)data.data()).highlightMod());
        this.actions = Lists.newArrayList();
        this.data.actions().forEach(a -> actions.add(MenuAction.get(a)));
    }

    @Override
    public MenuIcon<?> createIcon() { return MenuIcon.createIcon(session, location, data.iconData(), this); }

    @Override
    public void onClick() { actions.forEach(a -> a.execute(session)); }
}
