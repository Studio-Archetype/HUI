package com.volmit.holoui.config.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.volmit.holoui.config.icon.MenuIconData;
import com.volmit.holoui.enums.MenuComponentType;

public record DecoComponentData(MenuIconData iconData) implements ComponentData {

    public static final Codec<DecoComponentData> CODEC = RecordCodecBuilder.create(i -> i.group(
            MenuIconData.CODEC.fieldOf("icon").forGetter(DecoComponentData::iconData)
    ).apply(i, DecoComponentData::new));

    public MenuComponentType getType() {
        return MenuComponentType.DECO;
    }
}
