package studio.archetype.holoui.menu;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import studio.archetype.holoui.config.MenuDefinitionData;
import studio.archetype.holoui.menu.icon.MenuIcon;
import studio.archetype.holoui.utils.math.MathHelper;

import java.util.List;
import java.util.Map;

@Getter
public class MenuSession {

    private final String id;
    private final Player player;
    private final boolean freezePlayer;
    private final Vector offset;
    private final Map<String, MenuOption> options;
    private final List<Pair<MenuIcon<?>, Vector>> elements;

    private Location centerPoint;
    private String selectedOption;

    public MenuSession(MenuDefinitionData data, Player p) {
        this.id = data.getId();
        this.player = p;
        this.freezePlayer = data.isLockPosition();
        this.offset = data.getOffset();
        this.centerPoint = p.getLocation().add(offset);
        this.options = Maps.newHashMap();
        data.getOptions().forEach(o -> options.put(o.id(), new MenuOption(this, o)));
        elements = Lists.newArrayList();
        data.getElements().forEach(e -> {
            elements.add(new Pair<>(MenuIcon.createIcon(e.icon()), e.offset()));
        });
    }

    public void updateSelection() {
        boolean none = true;
        for(Map.Entry<String, MenuOption> entry : options.entrySet()) {
            entry.getValue().rotateToFace(player.getEyeLocation());
            if(entry.getValue().checkRaycast(player.getEyeLocation())) {
                none = false;
                if(!entry.getKey().equalsIgnoreCase(selectedOption))
                    switchTarget(entry.getKey());
            }
        }
        if(none)
            switchTarget(null);
    }

    public MenuOption getSelection() {
        if(selectedOption == null)
            return null;
        return options.get(selectedOption);
    }

    public void move(Location loc) {
        this.centerPoint = loc.add(offset);
        options.forEach((k, v) -> v.move(this.centerPoint.clone()));
        elements.forEach(p -> p.getFirst().teleport(getElementLocation(p)));
    }

    public void open() {
        options.forEach((k, v) -> v.show());
        elements.forEach(p -> p.getFirst().spawn(player, getElementLocation(p)));
    }

    public void close() {
        options.forEach((k, v) -> v.remove());
        elements.forEach(p -> p.getFirst().remove());
    }

    private void switchTarget(String option) {
        if(selectedOption != null)
            options.get(selectedOption).setHighlight(false);
        selectedOption = option;
        if(option != null)
            options.get(option).setHighlight(true);
    }

    private Location getElementLocation(Pair<MenuIcon<?>, Vector> element) {
        return MathHelper.rotateAroundPoint(centerPoint.clone().add(element.getSecond()), player.getEyeLocation(), 0, player.getLocation().getYaw());
    }
}
