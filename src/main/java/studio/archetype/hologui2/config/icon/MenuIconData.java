package studio.archetype.hologui2.config.icon;

import com.mojang.serialization.Codec;
import studio.archetype.hologui2.enums.MenuIconType;

public interface MenuIconData {
    MenuIconType getType();
    Codec<MenuIconData> CODEC = MenuIconType.CODEC.dispatch(MenuIconData::getType, MenuIconType::getCodec);
}
