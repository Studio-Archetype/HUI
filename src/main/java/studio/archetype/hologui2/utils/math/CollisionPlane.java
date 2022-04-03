package studio.archetype.hologui2.utils.math;

import lombok.Getter;
import org.bukkit.util.Vector;

@Getter
public class CollisionPlane {

    private static final Vector UP = new Vector(0, 1, 0);
    private static final Vector RIGHT = new Vector(1, 0, 0);

    private Vector up, right, center, normal;
    private float width, height, pitch, yaw;

    public CollisionPlane(Vector center, float width, float height) {
        this.center = center;
        this.width = width;
        this.height = height;
        this.up = UP.clone();
        this.right = RIGHT.clone();
    }

    public boolean isLookingAt(Vector origin, Vector direction) {
        Vector offset = center.clone().subtract(origin);
        double proj = normal.dot(direction);
        if(proj == 0)
            return false;
        double distance = normal.dot(offset) / proj;
        if(distance < 0.0F)
            return false;
        Vector intersect = origin.clone().add(direction.clone().multiply(distance)).subtract(center);
        float distX = (float)Math.abs(right.dot(intersect));
        float distY = (float)Math.abs(up.dot(intersect));
        return distX < width / 2 && distY < height / 2;
    }

    public void rotate(float pitch, float yaw) {
        if(pitch != this.pitch || yaw != this.yaw) {
            this.pitch = pitch;
            this.yaw = yaw;
            this.up = UP.clone().rotateAroundX(pitch).rotateAroundY(yaw);
            this.right = RIGHT.clone().rotateAroundX(pitch).rotateAroundY(yaw);
            calcNormal();
        }
    }

    public void translate(double x, double y, double z) {
        translate(new Vector(x, y, z));
    }

    public void translate(Vector vec) {
        this.center.add(vec);
    }

    public void resize(float width, float height) {
        this.width = width;
        this.height = height;
    }

    private void calcNormal() {
        this.normal = up.clone().crossProduct(right).normalize();
    }
}
