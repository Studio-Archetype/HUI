package com.volmit.holoui.utils.registries;

import org.bukkit.Material;

public class Materials {
    public static final Material GRASS = RegistryUtil.find(Material.class, RegistryUtil::findByField, "grass", "short_grass");
}
