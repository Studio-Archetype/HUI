package studio.archetype.holoui.menu.action;

import studio.archetype.holoui.config.action.CommandActionData;
import studio.archetype.holoui.config.action.MenuActionData;
import studio.archetype.holoui.config.action.SoundActionData;
import studio.archetype.holoui.menu.MenuSession;

public abstract class MenuAction<E extends MenuActionData> {

    protected final E data;

    public MenuAction(E data) {
        this.data = data;
    }

    public abstract void execute(MenuSession session);

    public static MenuAction<?> get(MenuActionData data) {
        if(data instanceof CommandActionData d)
            return new CommandMenuAction(d);
        else if(data instanceof SoundActionData d)
            return new SoundMenuAction(d);
        else
            return null;
    }
}
