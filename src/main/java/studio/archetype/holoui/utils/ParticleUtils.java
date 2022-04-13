package studio.archetype.holoui.utils;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.util.Vector;

public final class ParticleUtils {

    public static void playParticle(World w, Vector v, Color c) {
        w.spawnParticle(Particle.REDSTONE, v.getX(), v.getY(), v.getZ(), 5, new Particle.DustOptions(c, 1));
    }

}
