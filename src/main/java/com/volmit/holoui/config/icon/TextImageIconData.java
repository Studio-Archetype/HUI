package com.volmit.holoui.config.icon;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.volmit.holoui.enums.MenuIconType;

public record TextImageIconData(String relativePath) implements MenuIconData {

    public static final Codec<TextImageIconData> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.STRING.fieldOf("path").forGetter(TextImageIconData::relativePath)
    ).apply(i, TextImageIconData::new));

    public MenuIconType getType() {
        return MenuIconType.TEXT_IMAGE;
    }
}
