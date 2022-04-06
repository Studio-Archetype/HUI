package studio.archetype.holoui.utils.math;

import org.bukkit.util.Vector;

public class Quaternion {

    private final float x, y, z, w;

    public Quaternion(Vector axis, float angle) {
        angle = (float)Math.toRadians(angle);

        float theta = (float)Math.sin(angle / 2);
        this.x = (float)axis.getX() * theta;
        this.y = (float)axis.getY() * theta;
        this.z = (float)axis.getZ() * theta;
        this.w = (float)Math.cos(angle / 2);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public float getW() {
        return w;
    }
}