package studio.archetype.hologui2.menu.action;

import org.bukkit.Bukkit;
import studio.archetype.hologui2.config.action.CommandActionData;
import studio.archetype.hologui2.enums.MenuActionCommandSource;
import studio.archetype.hologui2.menu.MenuSession;

public class CommandMenuAction extends MenuAction<CommandActionData> {

    public CommandMenuAction(CommandActionData data) {
        super(data);
    }

    @Override
    public void execute(MenuSession session) {
        System.out.println("Mer");
        if(data.source() == MenuActionCommandSource.PLAYER)
            session.getPlayer().performCommand(data.command().startsWith("/") ? data.command() : "/" + data.command());
        else
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), data.command().startsWith("/") ? "/" + data.command() : data.command());
    }
}
