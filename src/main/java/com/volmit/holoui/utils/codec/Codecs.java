package com.volmit.holoui.utils.codec;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.volmit.holoui.utils.ItemUtils;
import com.volmit.holoui.utils.registries.RegistryUtil;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.util.Vector;

public final class Codecs {

    public static final Codec<NamespacedKey> NAMESPACED_KEY = Codec.STRING.xmap(NamespacedKey::fromString, NamespacedKey::toString);
    public static final Codec<Material> MATERIAL = NAMESPACED_KEY.xmap(ItemUtils::identifierToMaterial, ItemUtils::materialToIdentifier);
    public static final Codec<Vector> VECTOR = Codec.DOUBLE.listOf().xmap(l -> new Vector(l.get(0), l.get(1), l.get(2)), v -> Lists.newArrayList(v.getX(), v.getY(), v.getZ()));

    public static final Codec<Sound> SOUND = NAMESPACED_KEY.xmap(key -> {
        try {
            return RegistryUtil.find(Sound.class, key);
        } catch (Throwable e) {
            return Sound.BLOCK_GLASS_BREAK;
        }
    }, Sound::getKey);
}
