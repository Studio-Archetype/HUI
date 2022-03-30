package studio.archetype.hologui2.config.icon;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import studio.archetype.hologui2.enums.MenuIconType;

public record TextImageIconData(String relativePath) implements MenuIconData {

    public MenuIconType getType() { return MenuIconType.TEXT_IMAGE; }

    public static final Codec<TextImageIconData> CODEC = RecordCodecBuilder.create(i -> i.group(
       Codec.STRING.fieldOf("path").forGetter(TextImageIconData::relativePath)
    ).apply(i, TextImageIconData::new));
}
