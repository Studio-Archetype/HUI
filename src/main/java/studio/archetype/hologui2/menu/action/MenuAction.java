package studio.archetype.hologui2.menu.action;

import studio.archetype.hologui2.config.action.CommandActionData;
import studio.archetype.hologui2.config.action.MenuActionData;
import studio.archetype.hologui2.menu.MenuSession;

public abstract class MenuAction<E extends MenuActionData> {

    protected final E data;

    public MenuAction(E data) {
        this.data = data;
    }

    public abstract void execute(MenuSession session);

    public static MenuAction<?> get(MenuActionData data) {
        if(data instanceof CommandActionData d)
            return new CommandMenuAction(d);
        else
            return null;
    }
}
