package com.volmit.holoui.menu.components;

import com.google.common.collect.Lists;
import com.volmit.holoui.config.MenuComponentData;
import com.volmit.holoui.config.components.ToggleComponentData;
import com.volmit.holoui.menu.MenuSession;
import com.volmit.holoui.menu.action.MenuAction;
import com.volmit.holoui.menu.icon.MenuIcon;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Location;

import java.util.List;

public class ToggleComponent extends ClickableComponent<ToggleComponentData> {

    private final String condition, expected;
    private final MenuIcon<?> trueIcon, falseIcon;
    private final List<MenuAction<?>> trueActions, falseActions;

    private boolean state;

    public ToggleComponent(MenuSession session, MenuComponentData data) {
        super(session, data, ((ToggleComponentData) data.data()).highlightMod());
        this.condition = this.data.condition();
        this.expected = this.data.expectedValue();
        this.trueIcon = MenuIcon.createIcon(session, location, this.data.trueIcon(), this);
        this.falseIcon = MenuIcon.createIcon(session, location, this.data.falseIcon(), this);
        this.trueActions = Lists.newArrayList();
        this.data.trueActions().forEach(a -> trueActions.add(MenuAction.get(a)));
        this.falseActions = Lists.newArrayList();
        this.data.falseActions().forEach(a -> falseActions.add(MenuAction.get(a)));

        state = isValid();
    }

    @Override
    public void onClick() {
        if (state) {
            falseActions.forEach(a -> a.execute(session));
            changeIcon(falseIcon);
            state = false;
        } else {
            trueActions.forEach(a -> a.execute(session));
            changeIcon(trueIcon);
            state = true;
        }
    }

    @Override
    protected MenuIcon<?> createIcon() {
        falseIcon.teleport(location);
        trueIcon.teleport(location);
        return state ? trueIcon : falseIcon;
    }

    @Override
    public void move(Location loc) {
        super.move(loc);
        falseIcon.teleport(location);
        trueIcon.teleport(location);
    }

    private void changeIcon(MenuIcon<?> icon) {
        this.currentIcon.remove();
        this.currentIcon = icon;
        this.currentIcon.teleport(location.clone());
        this.plane = this.currentIcon.createBoundingBox();
        this.currentIcon.spawn();
    }

    private boolean isValid() {
        return PlaceholderAPI.setPlaceholders(session.getPlayer(), condition).equalsIgnoreCase(expected);
    }
}
