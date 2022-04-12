package studio.archetype.holoui.config.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import studio.archetype.holoui.config.icon.MenuIconData;
import studio.archetype.holoui.enums.MenuComponentType;

public record DecoComponentData(MenuIconData iconData) implements ComponentData {

    public MenuComponentType getType() { return MenuComponentType.DECO; }

    public static final Codec<DecoComponentData> CODEC = RecordCodecBuilder.create(i -> i.group(
            MenuIconData.CODEC.fieldOf("icon").forGetter(DecoComponentData::iconData)
    ).apply(i, DecoComponentData::new));
}
