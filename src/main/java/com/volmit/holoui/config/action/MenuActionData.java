package com.volmit.holoui.config.action;

import com.mojang.serialization.Codec;
import com.volmit.holoui.enums.MenuActionType;

public interface MenuActionData {
    MenuActionType getType();
    Codec<MenuActionData> CODEC = MenuActionType.CODEC.dispatch(MenuActionData::getType, MenuActionType::getCodec);
}
