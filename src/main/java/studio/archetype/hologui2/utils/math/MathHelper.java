package studio.archetype.hologui2.utils.math;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import studio.archetype.hologui2.utils.math.Matrix4;
import studio.archetype.hologui2.utils.math.Quaternion;

public final class MathHelper {

    public static Location rotateAroundPoint(Location loc, Location center, float pitch, float yaw) {
        loc.subtract(center);
        Vector newPos = loc.toVector().rotateAroundX(pitch).rotateAroundY(yaw);
        loc.setX(newPos.getX());
        loc.setY(newPos.getY());
        loc.setZ(newPos.getZ());
        return loc.add(center);
    }

    public static Vector unit(Vector a, Vector b) {
        return b.clone().subtract(a);
    }

    public static Vector interpolate(Vector start, Vector end, float delta) {
        return start.clone().add(end.clone().subtract(start).multiply(delta));
    }
}
