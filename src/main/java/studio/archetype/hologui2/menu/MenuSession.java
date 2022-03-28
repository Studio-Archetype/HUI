package studio.archetype.hologui2.menu;

import com.google.common.collect.Lists;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import studio.archetype.hologui2.config.MenuDefinitionData;

import java.util.List;

public class MenuSession {

    @Getter private final MenuDefinitionData data;
    @Getter private final Player player;
    private final Location centerPoint;
    private final List<MenuOption> options;

    public MenuSession(MenuDefinitionData data, Player p) {
        this.data = data;
        this.player = p;

        this.centerPoint = p.getLocation();
        centerPoint.add(0, data.getYOffset(), data.getDistance());
        this.options = Lists.newArrayList();
        data.getOptions().forEach(o -> options.add(new MenuOption(centerPoint, o)));
    }

    public void open() {
        options.forEach(o -> o.show(player));
    }

    public void close() {
        options.forEach(MenuOption::remove);
    }
}
