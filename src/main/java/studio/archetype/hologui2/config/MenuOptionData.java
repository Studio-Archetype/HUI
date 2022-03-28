package studio.archetype.hologui2.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import studio.archetype.hologui2.config.action.MenuActionData;
import studio.archetype.hologui2.config.icon.MenuIconData;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
public class MenuOptionData {

    private final String id;
    private final String translationKey;
    private final List<MenuActionData> actionsPrimary, actionsSecondary;
    private final MenuIconData icon;
    private final float xOffset, yOffset;

    public static final Codec<MenuOptionData> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.STRING.fieldOf("id").forGetter(MenuOptionData::getId),
            Codec.STRING.fieldOf("translationKey").forGetter(MenuOptionData::getTranslationKey),
            MenuActionData.CODEC.listOf().optionalFieldOf("actionsPrimary", new ArrayList<>()).forGetter(MenuOptionData::getActionsPrimary),
            MenuActionData.CODEC.listOf().optionalFieldOf("actionsSecondary", new ArrayList<>()).forGetter(MenuOptionData::getActionsSecondary),
            MenuIconData.CODEC.fieldOf("icon").forGetter(MenuOptionData::getIcon),
            Codec.FLOAT.fieldOf("xOffset").forGetter(MenuOptionData::getXOffset),
            Codec.FLOAT.fieldOf("yOffset").forGetter(MenuOptionData::getYOffset)
            ).apply(i, MenuOptionData::new));
}
