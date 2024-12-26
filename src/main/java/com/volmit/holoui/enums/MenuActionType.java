package com.volmit.holoui.enums;

import com.mojang.serialization.Codec;
import lombok.AllArgsConstructor;
import com.volmit.holoui.config.action.CommandActionData;
import com.volmit.holoui.config.action.MenuActionData;
import com.volmit.holoui.config.action.SoundActionData;
import com.volmit.holoui.utils.codec.CodecDispatcherEnum;
import com.volmit.holoui.utils.codec.EnumCodec;

@AllArgsConstructor
public enum MenuActionType implements EnumCodec.Values, CodecDispatcherEnum<MenuActionData> {

    COMMAND("command", CommandActionData.CODEC),
    SOUND("sound", SoundActionData.CODEC);

    public static final Codec<MenuActionType> CODEC = new EnumCodec<>(MenuActionType.class);

    private final String value;
    private final Codec<? extends MenuActionData> codec;

    public Codec<? extends MenuActionData> getCodec() { return codec; }
    public String getSerializedName() { return value; }
}
