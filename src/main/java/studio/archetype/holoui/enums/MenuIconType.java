package studio.archetype.holoui.enums;

import com.mojang.serialization.Codec;
import lombok.AllArgsConstructor;
import studio.archetype.holoui.config.icon.*;
import studio.archetype.holoui.utils.codec.CodecDispatcherEnum;
import studio.archetype.holoui.utils.codec.EnumCodec;

@AllArgsConstructor
public enum MenuIconType implements EnumCodec.Values, CodecDispatcherEnum<MenuIconData> {
    ITEM_TEXTURE("itemTexture", ItemIconData.CODEC),
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
