package studio.archetype.holoui.menu.components;

import com.google.common.collect.Lists;
import studio.archetype.holoui.config.MenuComponentData;
import studio.archetype.holoui.config.components.ButtonComponentData;
import studio.archetype.holoui.menu.MenuSession;
import studio.archetype.holoui.menu.action.MenuAction;
import studio.archetype.holoui.menu.icon.MenuIcon;

import java.util.List;

public class ButtonComponent extends ClickableComponent<ButtonComponentData> {

    private final List<MenuAction<?>> actions;

    public ButtonComponent(MenuSession session, MenuComponentData data) {
        super(session, data, ((ButtonComponentData)data.data()).highlightMod());
        this.actions = Lists.newArrayList();
        this.data.actions().forEach(a -> actions.add(MenuAction.get(a)));
    }

    @Override
    public MenuIcon<?> createIcon() { return MenuIcon.createIcon(session.getPlayer(), location, data.iconData()); }

    @Override
    public void onClick() { actions.forEach(a -> a.execute(session)); }
}
