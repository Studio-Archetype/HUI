package studio.archetype.holoui.menu.action;

import org.bukkit.Bukkit;
import studio.archetype.holoui.config.action.CommandActionData;
import studio.archetype.holoui.enums.MenuActionCommandSource;
import studio.archetype.holoui.menu.MenuSession;

public class CommandMenuAction extends MenuAction<CommandActionData> {

    public CommandMenuAction(CommandActionData data) {
        super(data);
    }

    @Override
    public void execute(MenuSession session) {
        String command = data.command().startsWith("/") ? data.command().substring(1) : data.command();
        if(data.source() == MenuActionCommandSource.PLAYER)
            session.getPlayer().performCommand(command);
        else
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
    }
}
