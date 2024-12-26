package com.volmit.holoui.enums;

import com.mojang.serialization.Codec;
import com.volmit.holoui.config.icon.*;
import lombok.AllArgsConstructor;
import studio.archetype.holoui.config.icon.*;
import com.volmit.holoui.utils.codec.CodecDispatcherEnum;
import com.volmit.holoui.utils.codec.EnumCodec;

@AllArgsConstructor
public enum MenuIconType implements EnumCodec.Values, CodecDispatcherEnum<MenuIconData> {
    ITEM("item", ItemIconData.CODEC),
    ANIMATED_TEXT_IMAGE("animatedTextImage", AnimatedImageData.CODEC),
    TEXT_IMAGE("textImage", TextImageIconData.CODEC),
    TEXT("text", TextIconData.CODEC),
    FONT_IMAGE("fontImage", null);

    public static final EnumCodec<MenuIconType> CODEC = new EnumCodec<>(MenuIconType.class);

    private final String value;
    private final Codec<? extends MenuIconData> codec;

    public Codec<? extends MenuIconData> getCodec() { return codec; }
    public String getSerializedName() { return value; }
}
