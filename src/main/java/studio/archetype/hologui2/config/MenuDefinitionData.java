package studio.archetype.hologui2.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class MenuDefinitionData {

    private final String translationKey;
    private final boolean lockPosition;
    private final List<MenuOptionData> options;
    private final float distance, yOffset;

    @Setter private String id;

    public static final Codec<MenuDefinitionData> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.STRING.fieldOf("translationKey").forGetter(MenuDefinitionData::getTranslationKey),
            Codec.BOOL.optionalFieldOf("lockPosition", false).forGetter(MenuDefinitionData::isLockPosition),
            MenuOptionData.CODEC.listOf().fieldOf("options").forGetter(MenuDefinitionData::getOptions),
            Codec.FLOAT.optionalFieldOf("distance", 3F).forGetter(MenuDefinitionData::getDistance),
            Codec.FLOAT.optionalFieldOf("yOffset", .5F).forGetter(MenuDefinitionData::getYOffset)
    ).apply(i, MenuDefinitionData::new));
}
