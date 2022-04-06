package studio.archetype.holoui.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.bukkit.util.Vector;
import studio.archetype.holoui.config.action.MenuActionData;
import studio.archetype.holoui.config.icon.MenuIconData;
import studio.archetype.holoui.utils.codec.Codecs;

import java.util.List;

public record MenuOptionData(String id, Vector offset, List<MenuActionData> actions, MenuIconData icon) {
    public static final Codec<MenuOptionData> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.STRING.fieldOf("id").forGetter(MenuOptionData::id),
            Codecs.VECTOR.fieldOf("offset").forGetter(MenuOptionData::offset),
            MenuActionData.CODEC.listOf().fieldOf("actions").forGetter(MenuOptionData::actions),
            MenuIconData.CODEC.fieldOf("icon").forGetter(MenuOptionData::icon)
    ).apply(i, MenuOptionData::new));
}
