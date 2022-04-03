package studio.archetype.hologui2.menu;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import studio.archetype.hologui2.HoloGUI;
import studio.archetype.hologui2.config.MenuDefinitionData;
import studio.archetype.hologui2.menu.icon.MenuIcon;
import studio.archetype.hologui2.utils.Events;
import studio.archetype.hologui2.utils.SchedulerUtils;
import studio.archetype.hologui2.utils.math.MathHelper;

import java.util.List;
import java.util.logging.Level;

public class MenuSession {

    @Getter private final MenuDefinitionData data;
    @Getter private final Player player;
    private final Location centerPoint;
    @Getter private final List<MenuOption> options;
    private final List<Pair<MenuIcon<?>, Location>> elements;

    private Events event;

    public MenuSession(MenuDefinitionData data, Player p) {
        this.data = data;
        this.player = p;

        this.centerPoint = p.getLocation().add(data.getOffset());
        this.options = Lists.newArrayList();
        data.getOptions().forEach(o -> options.add(new MenuOption(centerPoint.clone(), p, o)));
        elements = Lists.newArrayList();
        data.getElements().forEach(e -> {
            Location loc = MathHelper.rotateAroundPoint(centerPoint.clone().add(e.offset()), p.getEyeLocation(), 0, p.getLocation().getYaw());
            elements.add(new Pair<>(MenuIcon.createIcon(e.icon()), loc));
        });
    }

    public void open() {
        options.forEach(o -> o.show(player));
        elements.forEach(p -> p.getFirst().spawn(player, p.getSecond()));
        registerEvents();
    }

    public void close() {
        options.forEach(MenuOption::remove);
        elements.forEach(p -> p.getFirst().remove());
        event.unregister();
    }

    private void registerEvents() {
        event = Events.listen(PlayerMoveEvent.class, EventPriority.MONITOR, e -> {
            Location to = e.getFrom();
            to.setDirection(e.getTo().getDirection());

            if(data.isLockPosition())
                e.setTo(to);

            if(e.getFrom().getYaw() != e.getTo().getYaw() || e.getFrom().getPitch() != e.getTo().getPitch()) {
                options.forEach(o -> {
                    if(o.checkRaycast(to))
                        HoloGUI.log(Level.WARNING, "Hit menu option \"%s\"", o.getId());
                });
            }
        });
    }
}
