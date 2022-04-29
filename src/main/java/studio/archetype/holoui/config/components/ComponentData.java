package studio.archetype.holoui.config.components;

import com.mojang.serialization.Codec;
import studio.archetype.holoui.enums.MenuComponentType;
import studio.archetype.holoui.utils.codec.Serializable;

public interface ComponentData {
    MenuComponentType getType();
    Codec<ComponentData> CODEC = MenuComponentType.CODEC.dispatch(ComponentData::getType, MenuComponentType::getCodec);
}
