package studio.archetype.holoui.config.components;

import studio.archetype.holoui.enums.MenuComponentType;

public record ToggleComponentData() implements ComponentData {

    public MenuComponentType getType() { return MenuComponentType.TOGGLE; }
}
