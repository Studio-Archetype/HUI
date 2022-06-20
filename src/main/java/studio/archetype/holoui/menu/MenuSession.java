package studio.archetype.holoui.menu;

import com.google.common.collect.Lists;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import studio.archetype.holoui.config.MenuDefinitionData;
import studio.archetype.holoui.menu.components.MenuComponent;
import studio.archetype.holoui.utils.math.MathHelper;

import java.util.List;

@Getter
public class MenuSession {

    private final String id;
    private final Player player;
    private final boolean freezePlayer, followPlayer;
    private final Vector offset;
    private final List<MenuComponent<?>> components;

    private Location centerPoint;
    protected float initialY;

    public MenuSession(MenuDefinitionData data, Player p) {
        this.id = data.getId();
        this.player = p;
        this.freezePlayer = data.isFreeze();
        this.followPlayer = data.isFollow();
        this.offset = data.getOffset().clone().multiply(new Vector(-1, 1, 1));

        this.centerPoint = p.getLocation().clone().add(offset);
        this.components = Lists.newArrayList();
        data.getComponentData().forEach(a -> components.add(MenuComponent.getComponent(this, a)));
    }

    public void move(Location loc, boolean adjustRotation) {
        this.centerPoint = loc.add(offset);
        components.forEach(c -> c.move(this.centerPoint.clone(), adjustRotation));
    }

    public void rotate(float yaw) {
        components.forEach(c -> c.rotate(yaw));
    }

    public void open() {
        this.initialY = -player.getEyeLocation().getYaw();
        components.forEach(MenuComponent::open);
    }

    public void close() {
        components.forEach(MenuComponent::close);
    }

    public Location getCenterInitialYAdjusted() {
        return MathHelper.rotateAroundPoint(centerPoint.clone(), player.getEyeLocation(), 0, initialY);
    }
}
