package com.volmit.holoui.config.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.volmit.holoui.config.action.MenuActionData;
import com.volmit.holoui.config.icon.MenuIconData;
import com.volmit.holoui.enums.MenuComponentType;

import java.util.List;

public record ButtonComponentData(float highlightMod, List<MenuActionData> actions,
                                  MenuIconData iconData) implements ComponentData {

    public static final Codec<ButtonComponentData> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.FLOAT.optionalFieldOf("highlightModifier", 1.0F).forGetter(ButtonComponentData::highlightMod),
            MenuActionData.CODEC.listOf().fieldOf("actions").forGetter(ButtonComponentData::actions),
            MenuIconData.CODEC.fieldOf("icon").forGetter(ButtonComponentData::iconData)
    ).apply(i, ButtonComponentData::new));

    public MenuComponentType getType() {
        return MenuComponentType.BUTTON;
    }
}
