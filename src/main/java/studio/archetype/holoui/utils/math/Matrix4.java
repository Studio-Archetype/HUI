package studio.archetype.holoui.utils.math;

import org.bukkit.util.Vector;

public class Matrix4 {

    private float a0, a1, a2, a3;
    private float b0, b1, b2, b3;
    private float c0, c1, c2, c3;
    private float d0, d1, d2, d3;

    public Matrix4() {
        reset();
    }

    public Matrix4(Matrix4 source) {
        this.a0 = source.a0; this.a1 = source.a1; this.a2 = source.a2; this.a3 = source.a3;
        this.b0 = source.b0; this.b1 = source.b1; this.b2 = source.b2; this.b3 = source.b3;
        this.c0 = source.c0; this.c1 = source.c1; this.c2 = source.c2; this.c3 = source.c3;
        this.d0 = source.d0; this.d1 = source.d1; this.d2 = source.d2; this.d3 = source.d3;
    }

    public Matrix4(Quaternion rot) {
        float x = rot.getX();
        float y = rot.getY();
        float z = rot.getZ();
        float w = rot.getW();
        float j = 2.0F * x * x;
        float k = 2.0F * y * y;
        float l = 2.0F * z * z;
        this.a0 = 1.0F - k - l;
        this.b1 = 1.0F - l - j;
        this.c2 = 1.0F - j - k;
        this.d3 = 1.0F;
        float m = x * y;
        float n = y * z;
        float o = z * x;
        float p = x * w;
        float q = y * w;
        float r = z * w;
        this.b0 = 2.0F * (m + r);
        this.a1 = 2.0F * (m - r);
        this.c0 = 2.0F * (o - q);
        this.a2 = 2.0F * (o + q);
        this.c1 = 2.0F * (n + p);
        this.b2 = 2.0F * (n - p);
    }

    public static Matrix4 translate(Vector vec) {
        return translate((float)vec.getX(), (float)vec.getY(), (float)vec.getZ());
    }

    public static Matrix4 translate(float x, float y, float z) {
        Matrix4 mat = new Matrix4();
        mat.a0 = mat.b1 = mat.c2 = mat.d3 = 1;
        mat.a3 = x; mat.b3 = y; mat.c3 = z;
        return mat;
    }

    public static Matrix4 scale(float value) {
        return scale(value, value, value);
    }

    public static Matrix4 scale(float x, float y, float z) {
        Matrix4 mat = new Matrix4();
        mat.a0 = x; mat.b1 = y; mat.c2 = z; mat.d3 = 1.0F;
        return mat;
    }

    public void multiply(Quaternion rat) {
        multiply(new Matrix4(rat));
    }

    public void multiply(Matrix4 mat) {
        float na0 = this.a0 * mat.a0 + this.a1 * mat.b0 + this.a2 * mat.c0 + this.a3 * mat.d0;
        float na1 = this.a0 * mat.a1 + this.a1 * mat.b1 + this.a2 * mat.c1 + this.a3 * mat.d1;
        float na2 = this.a0 * mat.a2 + this.a1 * mat.b2 + this.a2 * mat.c2 + this.a3 * mat.d2;
        float na3 = this.a0 * mat.a3 + this.a1 * mat.b3 + this.a2 * mat.c3 + this.a3 * mat.d3;

        float nb0 = this.b0 * mat.a0 + this.b1 * mat.b0 + this.b2 * mat.c0 + this.b3 * mat.d0;
        float nb1 = this.b0 * mat.a1 + this.b1 * mat.b1 + this.b2 * mat.c1 + this.b3 * mat.d1;
        float nb2 = this.b0 * mat.a2 + this.b1 * mat.b2 + this.b2 * mat.c2 + this.b3 * mat.d2;
        float nb3 = this.b0 * mat.a3 + this.b1 * mat.b3 + this.b2 * mat.c3 + this.b3 * mat.d3;

        float nc0 = this.c0 * mat.a0 + this.c1 * mat.b0 + this.c2 * mat.c0 + this.c3 * mat.d0;
        float nc1 = this.c0 * mat.a1 + this.c1 * mat.b1 + this.c2 * mat.c1 + this.c3 * mat.d1;
        float nc2 = this.c0 * mat.a2 + this.c1 * mat.b2 + this.c2 * mat.c2 + this.c3 * mat.d2;
        float nc3 = this.c0 * mat.a3 + this.c1 * mat.b3 + this.c2 * mat.c3 + this.c3 * mat.d3;

        float nd0 = this.d0 * mat.a0 + this.d1 * mat.b0 + this.d2 * mat.c0 + this.d3 * mat.d0;
        float nd1 = this.d0 * mat.a1 + this.d1 * mat.b1 + this.d2 * mat.c1 + this.d3 * mat.d1;
        float nd2 = this.d0 * mat.a2 + this.d1 * mat.b2 + this.d2 * mat.c2 + this.d3 * mat.d2;
        float nd3 = this.d0 * mat.a3 + this.d1 * mat.b3 + this.d2 * mat.c3 + this.d3 * mat.d3;

        this.a0 = na0; this.a1 = na1; this.a2 = na2; this.a3 = na3;
        this.b0 = nb0; this.b1 = nb1; this.b2 = nb2; this.b3 = nb3;
        this.c0 = nc0; this.c1 = nc1; this.c2 = nc2; this.c3 = nc3;
        this.d0 = nd0; this.d1 = nd1; this.d2 = nd2; this.d3 = nd3;
    }

    public void reset() {
        this.a0 = 1.0F; this.a1 = 0; this.a2 = 0; this.a3 = 0;
        this.b0 = 0; this.b1 = 1.0F; this.b2 = 0; this.b3 = 0;
        this.c0 = 0; this.c1 = 0; this.c2 = 1.0F; this.c3 = 0;
        this.d0 = 0; this.d1 = 0; this.d2 = 0; this.d3 = 1.0F;
    }

    public Vector transform(Vector vector) {
        double x = a0 * vector.getX() + a1 * vector.getY() + a2 * vector.getZ() + a3 * 1;
        double y = b0 * vector.getX() + b1 * vector.getY() + b2 * vector.getZ() + b3 * 1;
        double z = c0 * vector.getX() + c1 * vector.getY() + c2 * vector.getZ() + c3 * 1;

        return new Vector(x, y, z);
    }

    @Override
    public String toString() {
        return "Matrix4f:\n" +
                String.format("%f %f %f %f\n", a0, a1, a2, a3) +
                String.format("%f %f %f %f\n", b0, b1, b2, b3) +
                String.format("%f %f %f %f\n", c0, c1, c2, c3) +
                String.format("%f %f %f %f", d0, d1, d2, d3);
    }
}