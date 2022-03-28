package studio.archetype.hologui2.menu;

import org.bukkit.entity.Player;
import studio.archetype.hologui2.config.MenuDefinitionData;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class MenuSessionManager {

    private final List<MenuSession> sessions = new ArrayList<>();

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
            if(s.getData().getId().equalsIgnoreCase(id)) {
                s.close();
                return true;
            }
            return false;
        });
    }

    public List<MenuSession> byId(String id) {
        return sessions.stream().filter(s -> s.getData().getId().equalsIgnoreCase(id)).toList();
    }

    public Optional<MenuSession> byPlayer(Player p) {
        return sessions.stream().filter(s -> s.getPlayer().equals(p)).findFirst();
    }
}
