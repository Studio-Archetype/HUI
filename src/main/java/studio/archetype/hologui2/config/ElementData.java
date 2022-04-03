package studio.archetype.hologui2.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.bukkit.util.Vector;
import studio.archetype.hologui2.config.icon.MenuIconData;
import studio.archetype.hologui2.menu.icon.MenuIcon;
import studio.archetype.hologui2.utils.codec.Codecs;

public record ElementData(Vector offset, MenuIconData icon) {
    public static final Codec<ElementData> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codecs.VECTOR.fieldOf("offset").forGetter(ElementData::offset),
            MenuIconData.CODEC.fieldOf("icon").forGetter(ElementData::icon)
    ).apply(i, ElementData::new));
}
