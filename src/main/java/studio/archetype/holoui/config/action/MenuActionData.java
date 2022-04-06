package studio.archetype.holoui.config.action;

import com.mojang.serialization.Codec;
import studio.archetype.holoui.enums.MenuActionType;

public interface MenuActionData {
    MenuActionType getType();
    Codec<MenuActionData> CODEC = MenuActionType.CODEC.dispatch(MenuActionData::getType, MenuActionType::getCodec);
}
