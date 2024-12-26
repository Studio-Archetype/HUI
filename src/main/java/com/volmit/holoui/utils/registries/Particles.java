package com.volmit.holoui.utils.registries;

import org.bukkit.Particle;

public class Particles {

    public static Particle REDSTONE = RegistryUtil.find(Particle.class, RegistryUtil::findByField, "redstone", "dust");
}
