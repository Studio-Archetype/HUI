package studio.archetype.holoui.menu.icon;

import com.google.common.collect.Lists;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import studio.archetype.holoui.HoloUI;
import studio.archetype.holoui.config.icon.TextImageIconData;
import studio.archetype.holoui.menu.ArmorStandManager;
import studio.archetype.holoui.utils.ArmorStandBuilder;
import studio.archetype.holoui.utils.TextUtils;
import studio.archetype.holoui.utils.math.CollisionPlane;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class TextImageMenuIcon extends MenuIcon<TextImageIconData> {

    private final List<Component> components;

    public TextImageMenuIcon(Player p, Location loc, TextImageIconData data) {
        super(p, loc, data);
        components = createComponents();
    }

    @Override
    protected List<UUID> createArmorStands(Location loc) {
        List<UUID> uuids = Lists.newArrayList();
        loc.add(0, ((components.size() - 1) / 2F  * NAMETAG_SIZE) - NAMETAG_SIZE, 0);
        components.forEach(c -> {
            uuids.add(ArmorStandManager.add(ArmorStandBuilder.nametagArmorStand(c, loc)));
            loc.subtract(0, NAMETAG_SIZE, 0);
        });
        System.out.println(loc);
        return uuids;
    }

    @Override
    public CollisionPlane createBoundingBox() {
        float width = 0;
        for(Component component : components)
            width = Math.max(width, component.getString().length() * NAMETAG_SIZE / 2);
        return new CollisionPlane(position.toVector(), width, (components.size() - 1) * NAMETAG_SIZE);
    }

    private List<Component> createComponents() {
        try {
            BufferedImage image = HoloUI.INSTANCE.getConfigManager().hasImage(data.relativePath());
            List<Component> lines = Lists.newArrayList();
            for(int y = 0; y < image.getHeight(); y++) {
                MutableComponent component = new TextComponent("");
                for(int x = 0; x < image.getWidth(); x++) {
                    int colour = image.getRGB(x, y);
                    if(((colour >> 24) & 0x0000FF) < 255)
                        component.append(new TextComponent(" ").setStyle(Style.EMPTY.withBold(true))).append(new TextComponent(" "));
                    else
                        component.append(TextUtils.textColor("█", colour & 0x00FFFFFF));
                }

                lines.add(component);
            }
            return lines;
        } catch(IOException e) {
            HoloUI.log(Level.WARNING, "Failed to load relative image \"%s\":", data.relativePath());
            HoloUI.log(Level.WARNING, "\t%s" + (e.getMessage() != null ? " - %s" : ""), e.getClass().getSimpleName(), e.getMessage());
            HoloUI.log(Level.WARNING, "Falling back to missing texture.");
            return MISSING;
        }
    }

    private static final List<Component> MISSING = Lists.newArrayList(
            TextUtils.textColor("████", "#000000").append(TextUtils.textColor("████", "#f800f8")),
            TextUtils.textColor("████", "#000000").append(TextUtils.textColor("████", "#f800f8")),
            TextUtils.textColor("████", "#000000").append(TextUtils.textColor("████", "#f800f8")),
            TextUtils.textColor("████", "#000000").append(TextUtils.textColor("████", "#f800f8")),
            TextUtils.textColor("████", "#f800f8").append(TextUtils.textColor("████", "#000000")),
            TextUtils.textColor("████", "#f800f8").append(TextUtils.textColor("████", "#000000")),
            TextUtils.textColor("████", "#f800f8").append(TextUtils.textColor("████", "#000000")),
            TextUtils.textColor("████", "#f800f8").append(TextUtils.textColor("████", "#000000")));
}
