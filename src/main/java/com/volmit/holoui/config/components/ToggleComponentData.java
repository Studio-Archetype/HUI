package com.volmit.holoui.config.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.volmit.holoui.config.action.MenuActionData;
import com.volmit.holoui.config.icon.MenuIconData;
import com.volmit.holoui.enums.MenuComponentType;

import java.util.List;

public record ToggleComponentData(float highlightMod, String condition, String expectedValue, List<MenuActionData> trueActions, List<MenuActionData> falseActions, MenuIconData trueIcon, MenuIconData falseIcon) implements ComponentData {

    public MenuComponentType getType() { return MenuComponentType.TOGGLE; }

    public static final Codec<ToggleComponentData> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.FLOAT.optionalFieldOf("hightlightModifier", 1.0F).forGetter(ToggleComponentData::highlightMod),
            Codec.STRING.fieldOf("condition").forGetter(ToggleComponentData::condition),
            Codec.STRING.fieldOf("expectedValue").forGetter(ToggleComponentData::expectedValue),
            MenuActionData.CODEC.listOf().fieldOf("trueActions").forGetter(ToggleComponentData::trueActions),
            MenuActionData.CODEC.listOf().fieldOf("falseActions").forGetter(ToggleComponentData::falseActions),
            MenuIconData.CODEC.fieldOf("trueIcon").forGetter(ToggleComponentData::trueIcon),
            MenuIconData.CODEC.fieldOf("falseIcon").forGetter(ToggleComponentData::falseIcon)
    ).apply(i, ToggleComponentData::new));
}
