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

public class MenuSession {

    @Getter private final MenuDefinitionData data;
    @Getter private final Player player;
    private final Location centerPoint;
    @Getter private final Map<String, MenuOption> options;
    private final List<Pair<MenuIcon<?>, Location>> elements;

    private String selectedOption;

    private Events freezeEvent;

    public MenuSession(MenuDefinitionData data, Player p) {
        this.data = data;
        this.player = p;

        this.centerPoint = p.getLocation().add(data.getOffset());
        this.options = Maps.newHashMap();
        data.getOptions().forEach(o -> options.put(o.id(), new MenuOption(centerPoint.clone(), p, o)));
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

    public void open() {
        options.forEach((k, v) -> v.show(player));
        elements.forEach(p -> p.getFirst().spawn(player, p.getSecond()));
        if(data.isLockPosition())
            registerFreezeEvent();
    }

    public void close() {
        options.forEach((k, v) -> v.remove());
        elements.forEach(p -> p.getFirst().remove());
        if(data.isLockPosition())
            freezeEvent.unregister();
    }

    private void registerFreezeEvent() {
        freezeEvent = Events.listen(PlayerMoveEvent.class, EventPriority.MONITOR, e -> {
            if(e.getPlayer().equals(player)) {
                Location to = e.getFrom();
                to.setDirection(e.getTo().getDirection());
                e.setTo(to);
            }
        });
    }

    private void switchTarget(String option) {
        if(selectedOption != null)
            options.get(selectedOption).setHighlight(false);
        selectedOption = option;
        if(option != null)
            options.get(option).setHighlight(true);
    }
}
