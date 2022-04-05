package studio.archetype.hologui2.menu;

import com.google.common.collect.Lists;
import org.bukkit.*;
import org.bukkit.util.Vector;
import studio.archetype.hologui2.config.MenuOptionData;
import studio.archetype.hologui2.menu.action.MenuAction;
import studio.archetype.hologui2.menu.icon.MenuIcon;
import studio.archetype.hologui2.utils.math.CollisionPlane;
import studio.archetype.hologui2.utils.math.MathHelper;

import java.util.List;

public class MenuOption {

    private final MenuSession session;

    private final Location position;
    private final MenuIcon<?> icon;
    private final List<MenuAction<?>> actions;

    private CollisionPlane plane;

    public MenuOption(MenuSession session, MenuOptionData data) {
        this.session = session;
        this.position = MathHelper.rotateAroundPoint(
                session.getCenterPoint().clone().add(data.offset()),
                session.getPlayer().getEyeLocation(),
                0, session.getPlayer().getLocation().getYaw());
        this.icon = MenuIcon.createIcon(data.icon());
        this.actions = Lists.newArrayList();
        data.actions().forEach(d -> actions.add(MenuAction.get(d)));
    }

    public void show() {
        plane = icon.spawn(session.getPlayer(), position);
        rotateToFace(session.getPlayer().getEyeLocation());
    }

    public void remove() {
        icon.remove();
        plane = null;
    }

    public boolean checkRaycast(Location position) {
        return plane.isLookingAt(position.toVector(), position.getDirection());
    }

    public void rotateToFace(Location loc) {
        Vector rotation = MathHelper.getRotationFromDirection(MathHelper.unit(plane.getCenter(), loc.toVector()));
        plane.rotate((float)rotation.getX() + 180, (float)-rotation.getY());
    }

    public void execute() {
        actions.forEach(a -> {
            a.execute(session);
        });
    }

    public void setHighlight(boolean highlight) {
        if(highlight)
            icon.offset(plane.getNormal().clone().multiply(1));
        else
            icon.offset(plane.getNormal().clone().multiply(-1));
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
}
