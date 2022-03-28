package studio.archetype.hologui2.enums;

import com.mojang.serialization.Codec;
import lombok.AllArgsConstructor;
import studio.archetype.hologui2.config.action.CommandActionData;
import studio.archetype.hologui2.config.action.MenuActionData;
import studio.archetype.hologui2.utils.codec.CodecDispatcherEnum;
import studio.archetype.hologui2.utils.codec.EnumCodec;

@AllArgsConstructor
public enum MenuActionType implements EnumCodec.Values, CodecDispatcherEnum<MenuActionData> {

    COMMAND("command", CommandActionData.CODEC);

    public static final Codec<MenuActionType> CODEC = new EnumCodec<>(MenuActionType.class);

    private final String value;
    private final Codec<? extends MenuActionData> codec;

    public Codec<? extends MenuActionData> getCodec() { return codec; }
    public String getSerializedName() { return value; }
}
