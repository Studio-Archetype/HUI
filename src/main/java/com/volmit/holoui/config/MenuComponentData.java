package com.volmit.holoui.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.volmit.holoui.config.components.ComponentData;
import com.volmit.holoui.utils.codec.Codecs;
import org.bukkit.util.Vector;

public record MenuComponentData(String id, Vector offset, ComponentData data) {
    public static final Codec<MenuComponentData> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.STRING.fieldOf("id").forGetter(MenuComponentData::id),
            Codecs.VECTOR.fieldOf("offset").forGetter(MenuComponentData::offset),
            ComponentData.CODEC.fieldOf("data").forGetter(MenuComponentData::data)
    ).apply(i, MenuComponentData::new));
}
