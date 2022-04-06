package studio.archetype.holoui.config.icon;

import com.mojang.serialization.Codec;
import studio.archetype.holoui.enums.MenuIconType;

public interface MenuIconData {
    MenuIconType getType();
    Codec<MenuIconData> CODEC = MenuIconType.CODEC.dispatch(MenuIconData::getType, MenuIconType::getCodec);
}
