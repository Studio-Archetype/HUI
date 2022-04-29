package studio.archetype.holoui.config.components;

import com.mojang.serialization.Codec;
import studio.archetype.holoui.enums.MenuComponentType;

public interface ComponentData {
    MenuComponentType getType();
    Codec<ComponentData> CODEC = MenuComponentType.CODEC.dispatch(ComponentData::getType, MenuComponentType::getCodec);
}
