package studio.archetype.holoui.menu.icon;

import com.google.common.collect.Lists;
import me.clip.placeholderapi.PlaceholderAPI;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import studio.archetype.holoui.config.icon.TextIconData;
import studio.archetype.holoui.exceptions.MenuIconException;
import studio.archetype.holoui.menu.ArmorStandManager;
import studio.archetype.holoui.menu.MenuSession;
import studio.archetype.holoui.utils.ArmorStandBuilder;
import studio.archetype.holoui.utils.math.CollisionPlane;

import java.util.List;
import java.util.UUID;

public class TextMenuIcon extends MenuIcon<TextIconData> {

    private final List<Component> components;

    public TextMenuIcon(MenuSession session, Location loc, TextIconData data) throws MenuIconException {
        super(session, loc, data);
        components = Lists.newArrayList();
        for(String s : data.text().split("\n"))
            components.add(new TextComponent(PlaceholderAPI.setPlaceholders(session.getPlayer(), s)));
    }

    @Override
    protected List<UUID> createArmorStands(Location loc) {
        List<UUID> uuids = Lists.newArrayList();
        loc.add(0, ((components.size() - 1) / 2F * NAMETAG_SIZE) - NAMETAG_SIZE, 0);
        components.forEach(c -> {
            uuids.add(ArmorStandManager.add(ArmorStandBuilder.nametagArmorStand(c, loc)));
            loc.subtract(0, NAMETAG_SIZE, 0);
        });
        return uuids;
    }

    @Override
    public CollisionPlane createBoundingBox() {
        float width = 0;
        for(Component component : components)
            width = Math.max(width, component.getString().length() * NAMETAG_SIZE / 2);
        return new CollisionPlane(position.toVector(), width, components.size() * NAMETAG_SIZE);
    }

    public void updateName(int index, Component c) {
        if(index >= components.size())
            return;
        components.set(index, c);
        ArmorStandManager.changeName(this.armorStands.get(index), c);
    }
}
