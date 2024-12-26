package com.volmit.holoui.enums;

import com.mojang.serialization.Codec;
import lombok.AllArgsConstructor;
import com.volmit.holoui.config.components.ButtonComponentData;
import com.volmit.holoui.config.components.ComponentData;
import com.volmit.holoui.config.components.DecoComponentData;
import com.volmit.holoui.config.components.ToggleComponentData;
import com.volmit.holoui.utils.codec.CodecDispatcherEnum;
import com.volmit.holoui.utils.codec.EnumCodec;

@AllArgsConstructor
public enum MenuComponentType implements EnumCodec.Values, CodecDispatcherEnum<ComponentData> {
    BUTTON("button", ButtonComponentData.CODEC),
    DECO("decoration", DecoComponentData.CODEC),
    TOGGLE("toggle", ToggleComponentData.CODEC);

    public static final Codec<MenuComponentType> CODEC = new EnumCodec<>(MenuComponentType.class);

    private final String serializedName;
    private final Codec<? extends ComponentData> codec;

    public Codec<? extends ComponentData> getCodec() { return codec; }
    public String getSerializedName() { return serializedName; }
}
