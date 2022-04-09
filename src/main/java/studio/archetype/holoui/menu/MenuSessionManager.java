package studio.archetype.holoui.menu;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import studio.archetype.holoui.HoloUI;
import studio.archetype.holoui.config.HuiSettings;
import studio.archetype.holoui.config.MenuDefinitionData;
import studio.archetype.holoui.menu.components.ClickableComponent;
import studio.archetype.holoui.menu.components.MenuComponent;
import studio.archetype.holoui.utils.Events;
import studio.archetype.holoui.utils.SchedulerUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class MenuSessionManager {

    private static final List<MenuSession> sessions = new ArrayList<>();

    private BukkitTask debug;

    public MenuSessionManager() {
        if(HuiSettings.DEBUG.getValue())
            controlDebugTask(true);
        SchedulerUtils.scheduleSyncTask(HoloUI.INSTANCE, 1L, () -> sessions.forEach(s -> s.getComponents().forEach(MenuComponent::tick)), false);
        Events.listen(PlayerMoveEvent.class, EventPriority.MONITOR, e -> sessions.forEach(s -> {
            if(e.getPlayer().equals(s.getPlayer())) {
                if(s.isFreezePlayer()) {
                    Location to = e.getFrom();
                    to.setDirection(e.getTo().getDirection());
                    e.setTo(to);
                    e.setCancelled(true);
                } else {
                    s.move(e.getTo().clone());
                }
            }
        }));
    }

    public void createNewSession(Player p, MenuDefinitionData menu) {
        MenuSession session = new MenuSession(menu, p);
        sessions.add(session);
        session.open();
    }

    public boolean destroySession(Player p) {
        Optional<MenuSession> session = byPlayer(p);
        if(session.isEmpty())
            return false;

        session.get().close();
        sessions.remove(session.get());
        return true;
    }

    public void destroyAll() {
        sessions.forEach(MenuSession::close);
        sessions.clear();
    }

    public void destroyAllType(String id) {
        sessions.removeIf(s -> {
            if(s.getId().equalsIgnoreCase(id)) {
                s.close();
                return true;
            }
            return false;
        });
    }

    public List<MenuSession> byId(String id) {
        return sessions.stream().filter(s -> s.getId().equalsIgnoreCase(id)).toList();
    }

    public Optional<MenuSession> byPlayer(Player p) {
        return sessions.stream().filter(s -> s.getPlayer().equals(p)).findFirst();
    }

    public void controlDebugTask(boolean enable) {
        if(enable && (debug == null || debug.isCancelled())) {
            debug = SchedulerUtils.scheduleSyncTask(HoloUI.INSTANCE, 2L, () -> {
                sessions.forEach(s -> s.getComponents().forEach(c -> {
                    if(c instanceof ClickableComponent<?> o)
                        o.highlightHitbox(s.getPlayer().getWorld());
                }));
            }, false);
        } else if(!enable && (debug != null && !debug.isCancelled()))
            debug.cancel();
    }

    private void playParticle(World w, Vector v, Color c) {
        w.spawnParticle(Particle.REDSTONE, v.getX(), v.getY(), v.getZ(), 5, new Particle.DustOptions(c, 1));
    }
}
