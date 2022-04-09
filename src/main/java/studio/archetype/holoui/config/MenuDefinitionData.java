package studio.archetype.holoui.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.util.Vector;
import studio.archetype.holoui.utils.codec.Codecs;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class MenuDefinitionData {

    private final String translationKey;
    private final Vector offset;
    private final boolean lockPosition;
    private final List<MenuComponentData> componentData;

    @Setter private String id;

    public static final Codec<MenuDefinitionData> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.STRING.fieldOf("translationKey").forGetter(MenuDefinitionData::getTranslationKey),
            Codecs.VECTOR.fieldOf("offset").forGetter(MenuDefinitionData::getOffset),
            Codec.BOOL.optionalFieldOf("lockPosition", true).forGetter(MenuDefinitionData::isLockPosition),
            MenuComponentData.CODEC.listOf().fieldOf("components").forGetter(MenuDefinitionData::getComponentData)
    ).apply(i, MenuDefinitionData::new));
}
