package com.volmit.holoui.config.components;

import com.mojang.serialization.Codec;
import com.volmit.holoui.enums.MenuComponentType;

public interface ComponentData {
    Codec<ComponentData> CODEC = MenuComponentType.CODEC.dispatch(ComponentData::getType, MenuComponentType::getCodec);

    MenuComponentType getType();
}
