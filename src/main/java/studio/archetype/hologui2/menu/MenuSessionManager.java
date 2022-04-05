package studio.archetype.hologui2.menu;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitTask;
import studio.archetype.hologui2.HoloGUI;
import studio.archetype.hologui2.config.MenuDefinitionData;
import studio.archetype.hologui2.utils.Events;
import studio.archetype.hologui2.utils.SchedulerUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class MenuSessionManager {

    private final List<MenuSession> sessions = new ArrayList<>();

    private final BukkitTask hitDetectionRunnable, debugRunnable;
    private Events clickHandler, moveHandler;

    public MenuSessionManager() {
        debugRunnable = SchedulerUtils.scheduleSyncTask(HoloGUI.INSTANCE, 2L, () -> {
            sessions.forEach(s -> {
                s.getOptions().forEach((k, o) -> {
                    o.rotateToFace(s.getPlayer().getEyeLocation());
                    o.highlightHitbox(s.getPlayer().getWorld());
                    boolean result = o.checkRaycast(s.getPlayer().getEyeLocation());
                    TextComponent component = new TextComponent(result ? "Hit!" : "Miss...");
                    component.setColor(result ? ChatColor.GREEN : ChatColor.RED);
                    s.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, component);
                });
            });
        }, false);

        hitDetectionRunnable = SchedulerUtils.scheduleSyncTask(HoloGUI.INSTANCE, 1L, () -> sessions.forEach(MenuSession::updateSelection), false);

        registerEvents();
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

    private void registerEvents() {
        moveHandler = Events.listen(PlayerMoveEvent.class, EventPriority.MONITOR, e -> {
            sessions.forEach(s -> {
                if(s.isFreezePlayer() && e.getPlayer().equals(s.getPlayer())) {
                    Location to = e.getFrom();
                    to.setDirection(e.getTo().getDirection());
                    e.setTo(to);
                    e.setCancelled(true);
                }
            });
        });

        clickHandler = Events.listen(PlayerInteractEvent.class, EventPriority.MONITOR, e -> {
           sessions.forEach(s -> {
               if(s.getPlayer().equals(e.getPlayer()) && s.getSelection() != null) {
                   System.out.println(e.getAction());
                   if(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK)
                       s.getSelection().execute();
               }
           });
        });
    }
}
