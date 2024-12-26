package com.volmit.holoui.config.icon;

import com.mojang.serialization.Codec;
import com.volmit.holoui.enums.MenuIconType;

public interface MenuIconData {
    MenuIconType getType();
    Codec<MenuIconData> CODEC = MenuIconType.CODEC.dispatch(MenuIconData::getType, MenuIconType::getCodec);
}
