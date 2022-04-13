package studio.archetype.holoui.menu;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;
import studio.archetype.holoui.HoloUI;
import studio.archetype.holoui.config.HuiSettings;
import studio.archetype.holoui.config.MenuDefinitionData;
import studio.archetype.holoui.menu.components.ClickableComponent;
import studio.archetype.holoui.menu.components.MenuComponent;
import studio.archetype.holoui.utils.Events;
import studio.archetype.holoui.utils.ParticleUtils;
import studio.archetype.holoui.utils.SchedulerUtils;
import studio.archetype.holoui.utils.math.MathHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class MenuSessionManager {

    private static final List<MenuSession> sessions = new ArrayList<>();

    private BukkitTask debugHitbox, debugPos;

    public MenuSessionManager() {
        controlHitboxDebug(HuiSettings.DEBUG_HITBOX.value());
        controlPositionDebug(HuiSettings.DEBUG_SPACING.value());
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
        Events.listen(PlayerQuitEvent.class, e -> destroySession(e.getPlayer()));
    }

    public void createNewSession(Player p, MenuDefinitionData menu) {
        destroySession(p);
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

    public void controlHitboxDebug(boolean hitbox) {
        if(hitbox && (debugHitbox == null || debugHitbox.isCancelled())) {
            debugHitbox = SchedulerUtils.scheduleSyncTask(HoloUI.INSTANCE, 2L, () -> sessions.forEach(s -> s.getComponents().forEach(c -> {
                if(c instanceof ClickableComponent<?> o)
                    o.highlightHitbox(s.getPlayer().getWorld());
            })), false);
        } else if(!hitbox && (debugHitbox != null && !debugHitbox.isCancelled()))
            debugHitbox.cancel();
    }

    public void controlPositionDebug(boolean positionDebug) {
        if(positionDebug && (debugPos == null || debugPos.isCancelled())) {
            debugPos = SchedulerUtils.scheduleSyncTask(HoloUI.INSTANCE, 2L, () -> sessions.forEach(s -> {
                World w = s.getPlayer().getWorld();
                ParticleUtils.playParticle(w, MathHelper.rotateAroundPoint(s.getCenterPoint().clone(), s.getPlayer().getEyeLocation(), 0, s.getInitialY()).toVector(), Color.YELLOW);
                s.getComponents().forEach(c -> ParticleUtils.playParticle(w, c.getLocation().toVector(), Color.ORANGE));
            }), false);
        } else if(!positionDebug && (debugPos != null && !debugPos.isCancelled()))
            debugPos.cancel();
    }
}
