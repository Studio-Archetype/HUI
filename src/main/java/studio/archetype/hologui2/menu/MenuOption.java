package studio.archetype.hologui2.menu;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import studio.archetype.hologui2.config.MenuOptionData;
import studio.archetype.hologui2.menu.icon.MenuIcon;
import studio.archetype.hologui2.utils.math.CollisionPlane;
import studio.archetype.hologui2.utils.math.MathHelper;

import java.util.List;

public class MenuOption {

    private final MenuOptionData data;
    private final MenuIcon<?> icon;
    private final Player player;
    private final Location position;

    private CollisionPlane plane;

    public MenuOption(Location centerPosition, Player p, MenuOptionData data) {
        this.data = data;
        this.player = p;
        this.position = MathHelper.rotateAroundPoint(centerPosition.add(data.offset()), p.getEyeLocation(), 0, p.getLocation().getYaw());
        this.icon = MenuIcon.createIcon(data.icon());
    }

    public String getId() {
        return data.id();
    }

    public void show(Player p) {
        plane = icon.spawn(p, position);
    }

    public void remove() {
        icon.remove();
        plane = null;
    }

    public boolean checkRaycast(Location position) {
        return plane.isLookingAt(position.toVector(), position.getDirection());
    }

    public void rotateToFace(Location loc) {
        double yaw = Math.atan2(loc.getY(), loc.getX()) - Math.atan2(plane.getCenter().getY(), plane.getCenter().getZ());
        plane.rotate(0, (float)Math.toDegrees(yaw));
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
