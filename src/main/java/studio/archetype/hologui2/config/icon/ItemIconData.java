package studio.archetype.hologui2.config.icon;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.bukkit.Material;
import studio.archetype.hologui2.enums.MenuIconType;
import studio.archetype.hologui2.utils.codec.Codecs;

public record ItemIconData(Material materialType, short customModelValue, boolean disableRotation) implements MenuIconData {

    public MenuIconType getType() { return MenuIconType.ITEM_TEXTURE; }

    public static final Codec<ItemIconData> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codecs.MATERIAL.fieldOf("item").forGetter(ItemIconData::materialType),
            Codec.SHORT.optionalFieldOf("customModelData", (short) 0).forGetter(ItemIconData::customModelValue),
            Codec.BOOL.optionalFieldOf("disableRotation", false).forGetter(ItemIconData::disableRotation)
    ).apply(i, ItemIconData::new));
}