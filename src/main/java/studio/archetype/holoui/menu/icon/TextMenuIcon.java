package studio.archetype.holoui.menu.icon;

import com.google.common.collect.Lists;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import org.bukkit.Location;
import studio.archetype.holoui.config.icon.TextIconData;
import studio.archetype.holoui.menu.ArmorStandManager;
import studio.archetype.holoui.utils.ArmorStandBuilder;
import studio.archetype.holoui.utils.math.CollisionPlane;

import java.util.List;
import java.util.UUID;

public class TextMenuIcon extends MenuIcon<TextIconData> {

    private final List<Component> components;

    public TextMenuIcon(TextIconData data) {
        super(data);
        components = Lists.newArrayList();
        for(String s : data.text().split("\n"))
            components.add(new TextComponent(s));
    }

    @Override
    protected List<UUID> createArmorStands(Location loc) {
        List<UUID> uuids = Lists.newArrayList();
        loc.add(0, components.size() / 2F  * NAMETAG_SIZE, 0);
        components.forEach(c -> {
            uuids.add(ArmorStandManager.add(ArmorStandBuilder.nametagArmorStand(c, loc)));
            loc.subtract(0, NAMETAG_SIZE, 0);
        });
        return uuids;
    }

    @Override
    protected CollisionPlane createBoundingBox(Location loc) {
        float width = 0;
        for(Component component : components)
            width = Math.max(width, component.getContents().length() * NAMETAG_SIZE * 0.8F);
        return new CollisionPlane(loc.toVector(), width / 2, components.size() * NAMETAG_SIZE / 2);
    }
}
