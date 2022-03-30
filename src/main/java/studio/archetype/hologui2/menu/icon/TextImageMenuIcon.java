package studio.archetype.hologui2.menu.icon;

import com.google.common.collect.Lists;
import net.minecraft.network.chat.*;
import org.bukkit.Location;
import studio.archetype.hologui2.HoloGUI;
import studio.archetype.hologui2.config.icon.TextImageIconData;
import studio.archetype.hologui2.menu.ArmorStandManager;
import studio.archetype.hologui2.utils.TextUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class TextImageMenuIcon extends MenuIcon<TextImageIconData> {

    private final List<Component> components;

    public TextImageMenuIcon(String translationKey, TextImageIconData data) {
        super(translationKey, data);
        components = createComponents();
    }

    @Override
    protected List<UUID> createArmorStands(Location loc) {
        List<UUID> uuids = Lists.newArrayList();
        loc.add(0, components.size() / 2F  * NAMETAG_SIZE, 0);
        uuids.add(ArmorStandManager.add(nametagArmorStand(name, loc.clone().add(0, NAMETAG_SIZE, 0))));
        components.forEach(c -> uuids.add(ArmorStandManager.add(nametagArmorStand(c, loc.subtract(0, NAMETAG_SIZE, 0)))));
        return uuids;
    }

    private List<Component> createComponents() {
        try {
            BufferedImage image = HoloGUI.INSTANCE.getConfigManager().hasImage(data.relativePath());
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
            HoloGUI.log(Level.WARNING, "Failed to load relative image \"%s\":", data.relativePath());
            HoloGUI.log(Level.WARNING, "\t%s" + (e.getMessage() != null ? " - %s" : ""), e.getClass().getSimpleName(), e.getMessage());
            HoloGUI.log(Level.WARNING, "Falling back to missing texture.");
            return MISSING;
        }
    }

    private static final List<Component> MISSING = Lists.newArrayList(
            TextUtils.textColor("████████", "#000000").append(TextUtils.textColor("████████", "#f800f8")),
            TextUtils.textColor("████████", "#000000").append(TextUtils.textColor("████████", "#f800f8")),
            TextUtils.textColor("████████", "#000000").append(TextUtils.textColor("████████", "#f800f8")),
            TextUtils.textColor("████████", "#000000").append(TextUtils.textColor("████████", "#f800f8")),
            TextUtils.textColor("████████", "#000000").append(TextUtils.textColor("████████", "#f800f8")),
            TextUtils.textColor("████████", "#000000").append(TextUtils.textColor("████████", "#f800f8")),
            TextUtils.textColor("████████", "#000000").append(TextUtils.textColor("████████", "#f800f8")),
            TextUtils.textColor("████████", "#000000").append(TextUtils.textColor("████████", "#f800f8")),
            TextUtils.textColor("████████", "#f800f8").append(TextUtils.textColor("████████", "#000000")),
            TextUtils.textColor("████████", "#f800f8").append(TextUtils.textColor("████████", "#000000")),
            TextUtils.textColor("████████", "#f800f8").append(TextUtils.textColor("████████", "#000000")),
            TextUtils.textColor("████████", "#f800f8").append(TextUtils.textColor("████████", "#000000")),
            TextUtils.textColor("████████", "#f800f8").append(TextUtils.textColor("████████", "#000000")),
            TextUtils.textColor("████████", "#f800f8").append(TextUtils.textColor("████████", "#000000")),
            TextUtils.textColor("████████", "#f800f8").append(TextUtils.textColor("████████", "#000000")),
            TextUtils.textColor("████████", "#f800f8").append(TextUtils.textColor("████████", "#000000")));
}
