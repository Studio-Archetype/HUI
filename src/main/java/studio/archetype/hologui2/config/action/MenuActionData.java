package studio.archetype.hologui2.config.action;

import com.mojang.serialization.Codec;
import studio.archetype.hologui2.enums.MenuActionType;

public interface MenuActionData {
    MenuActionType getType();
    Codec<MenuActionData> CODEC = MenuActionType.CODEC.dispatch(MenuActionData::getType, MenuActionType::getCodec);
}
