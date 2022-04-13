package studio.archetype.holoui.enums;

import com.mojang.serialization.Codec;
import lombok.AllArgsConstructor;
import studio.archetype.holoui.config.components.ButtonComponentData;
import studio.archetype.holoui.config.components.ComponentData;
import studio.archetype.holoui.config.components.DecoComponentData;
import studio.archetype.holoui.config.components.ToggleComponentData;
import studio.archetype.holoui.utils.codec.CodecDispatcherEnum;
import studio.archetype.holoui.utils.codec.EnumCodec;

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
