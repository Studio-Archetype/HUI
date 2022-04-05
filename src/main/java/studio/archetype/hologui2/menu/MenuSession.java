package studio.archetype.hologui2.menu;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import studio.archetype.hologui2.config.MenuDefinitionData;
import studio.archetype.hologui2.menu.icon.MenuIcon;
import studio.archetype.hologui2.utils.Events;
import studio.archetype.hologui2.utils.math.MathHelper;

import java.util.List;
import java.util.Map;

@Getter
public class MenuSession {

    private final String id;
    private final Player player;
    private final Location centerPoint;

    private final boolean freezePlayer;
    private final Map<String, MenuOption> options;
    private final List<Pair<MenuIcon<?>, Location>> elements;

    private String selectedOption;

    public MenuSession(MenuDefinitionData data, Player p) {
        this.id = data.getId();
        this.player = p;
        this.freezePlayer = data.isLockPosition();
        this.centerPoint = p.getLocation().add(data.getOffset());
        this.options = Maps.newHashMap();
        data.getOptions().forEach(o -> options.put(o.id(), new MenuOption(this, o)));
        elements = Lists.newArrayList();
        data.getElements().forEach(e -> {
            Location loc = MathHelper.rotateAroundPoint(centerPoint.clone().add(e.offset()), p.getEyeLocation(), 0, p.getLocation().getYaw());
            elements.add(new Pair<>(MenuIcon.createIcon(e.icon()), loc));
        });
    }

    public void updateSelection() {
        boolean none = true;
        for(Map.Entry<String, MenuOption> entry : options.entrySet()) {
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

    public void open() {
        options.forEach((k, v) -> v.show());
        elements.forEach(p -> p.getFirst().spawn(player, p.getSecond()));
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
}
