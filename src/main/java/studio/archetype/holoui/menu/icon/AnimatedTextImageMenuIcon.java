package studio.archetype.holoui.menu.icon;

import com.google.common.collect.Lists;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import studio.archetype.holoui.HoloUI;
import studio.archetype.holoui.config.icon.AnimatedImageData;
import studio.archetype.holoui.exceptions.MenuIconException;
import studio.archetype.holoui.menu.ArmorStandManager;
import studio.archetype.holoui.menu.MenuSession;
import studio.archetype.holoui.utils.ArmorStand;
import studio.archetype.holoui.utils.TextUtils;
import studio.archetype.holoui.utils.math.CollisionPlane;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class AnimatedTextImageMenuIcon extends MenuIcon<AnimatedImageData> {

    private final LinkedList<List<Component>> frameComponents = Lists.newLinkedList();

    private int currentFrame;
    private int passedTicks;

    public AnimatedTextImageMenuIcon(MenuSession session, Location loc, AnimatedImageData data) throws MenuIconException {
        super(session, loc, data);
        createComponents();
        currentFrame = passedTicks = 0;
    }

    @Override
    public void tick() {
        passedTicks++;
        if(passedTicks >= data.speed()) {
            passedTicks = 0;
            currentFrame++;
            if(currentFrame >= frameComponents.size())
                currentFrame = 0;
            updateFrame();
        }
    }

    @Override
    protected List<UUID> createArmorStands(Location location) {
        List<UUID> uuids = Lists.newArrayList();
        location.add(0, ((frameComponents.getFirst().size() - 1) / 2F  * NAMETAG_SIZE) - NAMETAG_SIZE, 0);
        frameComponents.getFirst().forEach(c -> {
            uuids.add(ArmorStandManager.add(ArmorStand.Builder.nametagArmorStand(c, location)));
            location.subtract(0, NAMETAG_SIZE, 0);
        });
        return uuids;
    }

    @Override
    public CollisionPlane createBoundingBox() {
        float width = 0;
        for(Component component : frameComponents.getFirst())
            width = Math.max(width, TextUtils.content(component).length() * NAMETAG_SIZE / 2);
        return new CollisionPlane(position.toVector(), width, (frameComponents.getFirst().size() - 1) * NAMETAG_SIZE);
    }

    private List<BufferedImage> getImages() throws IOException {
        if(data.source().left().isPresent())
            return HoloUI.INSTANCE.getConfigManager().getGifFrames(data.source().left().get());
        else if(data.source().right().isPresent()) {
            List<BufferedImage> images = Lists.newArrayList();
            for(String s : data.source().right().get())
                images.add(HoloUI.INSTANCE.getConfigManager().getImage(s).getSecond());
            return images;
        } else
            throw new IOException("Unknown critical error occurred while gathering animation frames!");
    }

    private void createComponents() throws MenuIconException {
        try {
            getImages().forEach(i -> {
                List<Component> lines = Lists.newArrayList();
                for(int y = 0; y < i.getHeight(); y++) {
                    var component = Component.text();
                    for(int x = 0; x < i.getWidth(); x++) {
                        int colour = i.getRGB(x, y);
                        if(((colour >> 24) & 0x0000FF) < 255)
                            component.append(Component.text(" ").decorate(TextDecoration.BOLD))
                                    .append(Component.text(" "));
                        else
                            component.append(TextUtils.textColor("█", colour & 0x00FFFFFF));
                    }
                    lines.add(component.build());
                }
                frameComponents.add(lines);
            });
        } catch(IOException e) {
            MenuIconException ex = new MenuIconException("Failed to construct animated icon!");
            ex.initCause(e);
            throw ex;
        }
    }

    private void updateFrame() {
        List<Component> components = frameComponents.get(currentFrame);
        for(int i = 0; i < armorStands.size(); i++)
            ArmorStandManager.changeName(armorStands.get(i), components.get(i));
    }
}
