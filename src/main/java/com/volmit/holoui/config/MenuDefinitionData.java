package com.volmit.holoui.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.volmit.holoui.utils.codec.Codecs;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.util.Vector;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class MenuDefinitionData {

    public static final Codec<MenuDefinitionData> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codecs.VECTOR.fieldOf("offset").forGetter(MenuDefinitionData::getOffset),
            Codec.BOOL.optionalFieldOf("lockPosition", true).forGetter(MenuDefinitionData::isFreeze),
            Codec.BOOL.optionalFieldOf("followPlayer", true).forGetter(MenuDefinitionData::isFollow),
            MenuComponentData.CODEC.listOf().fieldOf("components").forGetter(MenuDefinitionData::getComponentData)
    ).apply(i, MenuDefinitionData::new));
    private final Vector offset;
    private final boolean freeze, follow;
    private final List<MenuComponentData> componentData;
    @Setter
    private String id;
}
