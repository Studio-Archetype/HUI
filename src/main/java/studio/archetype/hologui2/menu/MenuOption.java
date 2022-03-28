package studio.archetype.hologui2.menu;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import studio.archetype.hologui2.config.MenuOptionData;
import studio.archetype.hologui2.menu.icon.MenuIcon;

public class MenuOption {

    private final MenuOptionData data;
    private final MenuIcon<?> icon;
    private final Location position;

    public MenuOption(Location centerPosition, MenuOptionData data) {
        this.data = data;
        this.position = centerPosition.add(data.getXOffset(), data.getYOffset(), 0);
        this.icon = MenuIcon.createIcon(data.getTranslationKey(), data.getIcon());
    }

    public void show(Player p) {
        icon.spawn(p, position);
    }

    public void remove() {
        icon.remove();
    }
}
