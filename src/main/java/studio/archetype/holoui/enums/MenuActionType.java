package studio.archetype.holoui.enums;

import com.mojang.serialization.Codec;
import lombok.AllArgsConstructor;
import studio.archetype.holoui.config.action.CommandActionData;
import studio.archetype.holoui.config.action.MenuActionData;
import studio.archetype.holoui.utils.codec.CodecDispatcherEnum;
import studio.archetype.holoui.utils.codec.EnumCodec;

@AllArgsConstructor
public enum MenuActionType implements EnumCodec.Values, CodecDispatcherEnum<MenuActionData> {

    COMMAND("command", CommandActionData.CODEC);

    public static final Codec<MenuActionType> CODEC = new EnumCodec<>(MenuActionType.class);

    private final String value;
    private final Codec<? extends MenuActionData> codec;

    public Codec<? extends MenuActionData> getCodec() { return codec; }
    public String getSerializedName() { return value; }
}
