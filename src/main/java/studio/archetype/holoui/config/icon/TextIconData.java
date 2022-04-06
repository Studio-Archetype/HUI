package studio.archetype.holoui.config.icon;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import studio.archetype.holoui.enums.MenuIconType;

public record TextIconData(String text) implements  MenuIconData {

    public MenuIconType getType() { return MenuIconType.TEXT; }

    public static final Codec<TextIconData> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.STRING.fieldOf("text").forGetter(TextIconData::text)
    ).apply(i, TextIconData::new));
}
