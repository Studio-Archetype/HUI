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

    private final Vector offset;
    private final boolean freeze, follow;
    private final List<MenuComponentData> componentData;

    @Setter private String id;

    public static final Codec<MenuDefinitionData> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codecs.VECTOR.fieldOf("offset").forGetter(MenuDefinitionData::getOffset),
            Codec.BOOL.optionalFieldOf("lockPosition", true).forGetter(MenuDefinitionData::isFreeze),
            Codec.BOOL.optionalFieldOf("followPlayer", true).forGetter(MenuDefinitionData::isFreeze),
            MenuComponentData.CODEC.listOf().fieldOf("components").forGetter(MenuDefinitionData::getComponentData)
    ).apply(i, MenuDefinitionData::new));
}
