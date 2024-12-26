package com.volmit.holoui.menu;

import com.google.common.collect.Lists;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import com.volmit.holoui.config.MenuDefinitionData;
import com.volmit.holoui.menu.components.MenuComponent;
import com.volmit.holoui.utils.math.MathHelper;

import java.util.List;

@Getter
public class MenuSession {

    private final String id;
    private final Player player;
    private final boolean freezePlayer, followPlayer;
    private final Vector offset;
    private final List<MenuComponent<?>> components;

    protected Location centerPoint;
    protected float initialY = Float.NaN;

    public MenuSession(MenuDefinitionData data, Player p) {
        this.id = data.getId();
        this.player = p;
        this.freezePlayer = data.isFreeze();
        this.followPlayer = data.isFollow();
        this.offset = data.getOffset().clone().multiply(new Vector(-1, 1, 1));

        this.centerPoint = p.getLocation().clone().add(offset);
        this.components = Lists.newArrayList();
        data.getComponentData().forEach(a -> components.add(MenuComponent.getComponent(this, a)));
    }

    public void move(Location loc, boolean byPlayer) {
        this.centerPoint = loc.add(offset);
        components.forEach(c -> {
            c.move(this.centerPoint.clone());
            c.adjustRotation(byPlayer);
        });
    }

    public void adjustRotation(boolean byPlayer) {
        components.forEach(c -> c.adjustRotation(byPlayer));
    }

    public void rotate(float yaw) {
        components.forEach(c -> c.rotate(yaw));
    }

    public void open() {
        this.initialY = -player.getEyeLocation().getYaw();
        components.forEach(c -> c.open(true));
    }

    public void close() {
        components.forEach(MenuComponent::close);
    }

    public Location getCenterInitialYAdjusted() {
        return MathHelper.rotateAroundPoint(centerPoint.clone(), player.getEyeLocation(), 0, initialY);
    }

    public Location getCenterNoOffset() {
        return this.centerPoint.clone().subtract(offset);
    }

    public void rotateCenter() {
        MathHelper.rotateAroundPoint(this.centerPoint, getCenterNoOffset(), 0, initialY);
        getComponents().forEach(c -> c.move(this.centerPoint.clone()));
    }
}
