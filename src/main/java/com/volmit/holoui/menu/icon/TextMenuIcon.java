package com.volmit.holoui.menu.icon;

import com.google.common.collect.Lists;
import com.volmit.holoui.config.icon.TextIconData;
import com.volmit.holoui.exceptions.MenuIconException;
import com.volmit.holoui.menu.ArmorStandManager;
import com.volmit.holoui.menu.MenuSession;
import com.volmit.holoui.utils.ArmorStand;
import com.volmit.holoui.utils.TextUtils;
import com.volmit.holoui.utils.math.CollisionPlane;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TextMenuIcon extends MenuIcon<TextIconData> {

    private final List<Component> components;

    public TextMenuIcon(MenuSession session, Location loc, TextIconData data) throws MenuIconException {
        super(session, loc, data);
        components = Arrays.stream(data.text().split("\n"))
                .map(s -> TextUtils.parse(PlaceholderAPI.setPlaceholders(session.getPlayer(), s)))
                .collect(Collectors.toList());
    }

    @Override
    protected List<UUID> createArmorStands(Location loc) {
        List<UUID> uuids = Lists.newArrayList();
        loc.add(0, ((components.size() - 1) / 2F * NAMETAG_SIZE) - NAMETAG_SIZE, 0);
        components.forEach(c -> {
            uuids.add(ArmorStandManager.add(ArmorStand.Builder.nametagArmorStand(c, loc)));
            loc.subtract(0, NAMETAG_SIZE, 0);
        });
        return uuids;
    }

    @Override
    public CollisionPlane createBoundingBox() {
        float width = 0;
        for (Component component : components)
            width = Math.max(width, TextUtils.content(component).length() * NAMETAG_SIZE / 2);
        return new CollisionPlane(position.toVector(), width, components.size() * NAMETAG_SIZE);
    }

    public void updateName(int index, Component c) {
        if (index >= components.size())
            return;
        components.set(index, c);
        ArmorStandManager.changeName(this.armorStands.get(index), c);
    }
}
