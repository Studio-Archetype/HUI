package studio.archetype.hologui2.enums;

import com.mojang.serialization.Codec;
import lombok.AllArgsConstructor;
import studio.archetype.hologui2.config.icon.ItemIconData;
import studio.archetype.hologui2.config.icon.MenuIconData;
import studio.archetype.hologui2.config.icon.TextImageIconData;
import studio.archetype.hologui2.utils.codec.CodecDispatcherEnum;
import studio.archetype.hologui2.utils.codec.EnumCodec;

@AllArgsConstructor
public enum MenuIconType implements EnumCodec.Values, CodecDispatcherEnum<MenuIconData> {
    ITEM_TEXTURE("itemTexture", ItemIconData.CODEC),
    TEXT_IMAGE("textImage", TextImageIconData.CODEC),
    FONT_IMAGE("fontImage", null);

    public static final EnumCodec<MenuIconType> CODEC = new EnumCodec<>(MenuIconType.class);

    private final String value;
    private final Codec<? extends MenuIconData> codec;

    public Codec<? extends MenuIconData> getCodec() { return codec; }
    public String getSerializedName() { return value; }
}
