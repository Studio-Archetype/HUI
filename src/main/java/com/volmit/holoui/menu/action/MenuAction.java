package com.volmit.holoui.menu.action;

import com.volmit.holoui.config.action.CommandActionData;
import com.volmit.holoui.config.action.MenuActionData;
import com.volmit.holoui.config.action.SoundActionData;
import com.volmit.holoui.menu.MenuSession;

public abstract class MenuAction<E extends MenuActionData> {

    protected final E data;

    public MenuAction(E data) {
        this.data = data;
    }

    public static MenuAction<?> get(MenuActionData data) {
        if (data instanceof CommandActionData d)
            return new CommandMenuAction(d);
        else if (data instanceof SoundActionData d)
            return new SoundMenuAction(d);
        else
            return null;
    }

    public abstract void execute(MenuSession session);
}
