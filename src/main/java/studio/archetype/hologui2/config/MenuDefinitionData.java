package studio.archetype.hologui2.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.util.Vector;
import studio.archetype.hologui2.config.icon.MenuIconData;
import studio.archetype.hologui2.menu.icon.MenuIcon;
import studio.archetype.hologui2.utils.codec.Codecs;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class MenuDefinitionData {

    private final String translationKey;
    private final Vector offset;
    private final boolean lockPosition;
    private final List<ElementData> elements;
    private final List<MenuOptionData> options;

    @Setter private String id;

    public static final Codec<MenuDefinitionData> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.STRING.fieldOf("translationKey").forGetter(MenuDefinitionData::getTranslationKey),
            Codecs.VECTOR.fieldOf("offset").forGetter(MenuDefinitionData::getOffset),
            Codec.BOOL.optionalFieldOf("lockPosition", true).forGetter(MenuDefinitionData::isLockPosition),
            ElementData.CODEC.listOf().fieldOf("elements").forGetter(MenuDefinitionData::getElements),
            MenuOptionData.CODEC.listOf().fieldOf("options").forGetter(MenuDefinitionData::getOptions)
    ).apply(i, MenuDefinitionData::new));
}
