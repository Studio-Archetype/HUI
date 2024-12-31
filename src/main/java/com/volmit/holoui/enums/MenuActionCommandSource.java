package com.volmit.holoui.enums;

import com.mojang.serialization.Codec;
import com.volmit.holoui.utils.codec.EnumCodec;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MenuActionCommandSource implements EnumCodec.Values {
    PLAYER("player"),
    GLOBAL("server");

    public static final Codec<MenuActionCommandSource> CODEC = new EnumCodec<>(MenuActionCommandSource.class);

    private final String serialized;

    @Override
    public String getSerializedName() {
        return serialized;
    }
}
