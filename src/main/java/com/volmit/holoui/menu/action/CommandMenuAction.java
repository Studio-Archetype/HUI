package com.volmit.holoui.menu.action;

import com.volmit.holoui.config.action.CommandActionData;
import com.volmit.holoui.enums.MenuActionCommandSource;
import com.volmit.holoui.menu.MenuSession;
import org.bukkit.Bukkit;

public class CommandMenuAction extends MenuAction<CommandActionData> {

    public CommandMenuAction(CommandActionData data) {
        super(data);
    }

    @Override
    public void execute(MenuSession session) {
        String command = data.command().startsWith("/") ? data.command().substring(1) : data.command();
        if (data.source() == MenuActionCommandSource.PLAYER)
            session.getPlayer().performCommand(command);
        else
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
    }
}
