package studio.archetype.holoui.menu.icon;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import studio.archetype.holoui.HoloUI;
import studio.archetype.holoui.config.icon.TextImageIconData;
import studio.archetype.holoui.enums.ImageFormat;
import studio.archetype.holoui.exceptions.MenuIconException;
import studio.archetype.holoui.menu.ArmorStandManager;
import studio.archetype.holoui.menu.MenuSession;
import studio.archetype.holoui.utils.ArmorStand;
import studio.archetype.holoui.utils.TextUtils;
import studio.archetype.holoui.utils.math.CollisionPlane;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class TextImageMenuIcon extends MenuIcon<TextImageIconData> {

    private final List<Component> components;

    public TextImageMenuIcon(MenuSession session, Location loc, TextImageIconData data) throws MenuIconException {
        super(session, loc, data);
        components = createComponents();
    }

    public TextImageMenuIcon(MenuSession session, Location loc) throws MenuIconException {
        super(session, loc, null);
        components = MISSING;
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
        for(Component component : components)
            width = Math.max(width, TextUtils.content(component).length() * NAMETAG_SIZE / 2);
        return new CollisionPlane(position.toVector(), width, (components.size() - 1) * NAMETAG_SIZE);
    }

    private List<Component> createComponents() throws MenuIconException {
        try {
            Pair<ImageFormat, BufferedImage> imageData = HoloUI.INSTANCE.getConfigManager().getImage(data.relativePath());
            BufferedImage image = imageData.getSecond();
            ImageFormat format = imageData.getFirst();
            List<Component> lines = Lists.newArrayList();
            for(int y = 0; y < image.getHeight(); y++) {
                var component = Component.text();
                for(int x = 0; x < image.getWidth(); x++) {
                    int colour = image.getRGB(x, y);
                    if(format != ImageFormat.JPEG && ((colour >> 24) & 0x0000FF) < 255)
                        component.append(Component.text(" ").decorate(TextDecoration.BOLD)).append(Component.text(" "));
                    else
                        component.append(TextUtils.textColor("█", colour & 0x00FFFFFF));
                }

                lines.add(component.build());
            }
            return lines;
        } catch(IOException e) {
            MenuIconException ex = new MenuIconException("Failed to load relative image \"%s\"!", data.relativePath());
            ex.initCause(e);
            throw ex;
        }
    }

    public static final List<Component> MISSING = Lists.newArrayList(
            TextUtils.textColor("████", "#000000").append(TextUtils.textColor("████", "#f800f8")),
            TextUtils.textColor("████", "#000000").append(TextUtils.textColor("████", "#f800f8")),
            TextUtils.textColor("████", "#000000").append(TextUtils.textColor("████", "#f800f8")),
            TextUtils.textColor("████", "#000000").append(TextUtils.textColor("████", "#f800f8")),
            TextUtils.textColor("████", "#f800f8").append(TextUtils.textColor("████", "#000000")),
            TextUtils.textColor("████", "#f800f8").append(TextUtils.textColor("████", "#000000")),
            TextUtils.textColor("████", "#f800f8").append(TextUtils.textColor("████", "#000000")),
            TextUtils.textColor("████", "#f800f8").append(TextUtils.textColor("████", "#000000")));
}
