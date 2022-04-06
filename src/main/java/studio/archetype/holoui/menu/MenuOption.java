package studio.archetype.holoui.menu;

import com.google.common.collect.Lists;
import org.bukkit.*;
import org.bukkit.util.Vector;
import studio.archetype.holoui.config.MenuOptionData;
import studio.archetype.holoui.menu.action.MenuAction;
import studio.archetype.holoui.menu.icon.MenuIcon;
import studio.archetype.holoui.utils.math.CollisionPlane;
import studio.archetype.holoui.utils.math.MathHelper;

import java.util.List;

public class MenuOption {

    private final MenuSession session;

    private final Vector offset;
    private final MenuIcon<?> icon;
    private final List<MenuAction<?>> actions;

    private Location position;
    private CollisionPlane plane;
    private boolean highlighted;

    public MenuOption(MenuSession session, MenuOptionData data) {
        this.session = session;
        this.offset = data.offset();
        this.position = MathHelper.rotateAroundPoint(
                session.getCenterPoint().clone().add(offset),
                session.getPlayer().getEyeLocation(),
                0, session.getPlayer().getLocation().getYaw());
        this.icon = MenuIcon.createIcon(data.icon());
        this.actions = Lists.newArrayList();
        data.actions().forEach(d -> actions.add(MenuAction.get(d)));
    }

    public void show() {
        plane = icon.spawn(session.getPlayer(), position.clone());
        rotateToFace(session.getPlayer().getEyeLocation());
    }

    public void remove() {
        icon.remove();
        plane = null;
    }

    public void move(Location loc) {
        this.position = loc.add(offset);
        plane.move(position);
        icon.teleport(position);
    }

    public boolean checkRaycast(Location position) {
        return plane.isLookingAt(position.toVector(), position.getDirection());
    }

    public void rotateToFace(Location loc) {
        Vector rotation = MathHelper.getRotationFromDirection(MathHelper.unit(plane.getCenter(), loc.toVector()));
        plane.rotate((float)rotation.getX() + 180, (float)-rotation.getY());
        if(highlighted)
            icon.teleport(position.clone().add(plane.getNormal()));
    }

    public void execute() {
        actions.forEach(a -> a.execute(session));
    }

    public void setHighlight(boolean highlight) {
        this.highlighted = highlight;
        if(highlight)
            icon.move(plane.getNormal().clone().multiply(1));
        else
            icon.teleport(position);
    }

    public void highlightHitbox(World w) {
        if(plane == null)
            return;
        Vector downRight = plane.getCenter().clone().subtract(plane.getUp().clone().multiply(plane.getHeight() / 2)).add(plane.getRight().clone().multiply(plane.getWidth() / 2));
        Vector downLeft = plane.getCenter().clone().subtract(plane.getUp().clone().multiply(plane.getHeight() / 2)).subtract(plane.getRight().clone().multiply(plane.getWidth() / 2));
        Vector upRight = plane.getCenter().clone().add(plane.getUp().clone().multiply(plane.getHeight() / 2)).add(plane.getRight().clone().multiply(plane.getWidth() / 2));
        Vector upLeft = plane.getCenter().clone().add(plane.getUp().clone().multiply(plane.getHeight() / 2)).subtract(plane.getRight().clone().multiply(plane.getWidth() / 2));
        for(float d = .1F; d <= 1; d += .1F) {
            playParticle(w, MathHelper.interpolate(downRight, upRight, d), Color.BLUE);
            playParticle(w, MathHelper.interpolate(downLeft, upLeft, d), Color.BLUE);
            playParticle(w, MathHelper.interpolate(downLeft, downRight, d), Color.BLUE);
            playParticle(w, MathHelper.interpolate(upLeft, upRight, d), Color.BLUE);
            playParticle(w, MathHelper.interpolate(plane.getCenter(), plane.getCenter().clone().add(plane.getNormal().clone().multiply(2)), d), Color.RED);
        }
        playParticle(w, downRight, Color.BLUE);
        playParticle(w, downLeft, Color.BLUE);
        playParticle(w, upRight, Color.BLUE);
        playParticle(w, upLeft, Color.BLUE);
    }

    private void playParticle(World w, Vector v, Color c) {
        w.spawnParticle(Particle.REDSTONE, v.getX(), v.getY(), v.getZ(), 5, new Particle.DustOptions(c, 1));
    }

    private void updateHighlightPosition() {}
}
