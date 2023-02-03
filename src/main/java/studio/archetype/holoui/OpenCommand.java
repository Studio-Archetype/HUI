package studio.archetype.holoui;

import com.google.common.collect.Lists;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import studio.archetype.holoui.config.MenuDefinitionData;
import studio.archetype.holoui.utils.SimpleCommand;

import java.util.Collections;
import java.util.Optional;

public class OpenCommand extends SimpleCommand {

    public OpenCommand(String name) {
        super(name, "Opens the " + name + " menu.", "/" + name, Lists.newArrayList());
        setPermission("hui.open." + name);
        setPermissionMessage(HoloCommand.PREFIX + ChatColor.RED + "You lack permission to open \"" + getName() + "\".");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(HoloCommand.PREFIX + ChatColor.RED + "Direct menus can only be executed by players.");
            return true;
        }
        Player p = (Player)sender;
        Optional<MenuDefinitionData> data = HoloUI.INSTANCE.getConfigManager().get(getName());
        if(data.isEmpty()) {
            p.sendMessage(HoloCommand.PREFIX + ChatColor.RED + "\"" + getName() + "\" is not available.");
            return true;
        }
        HoloUI.INSTANCE.getSessionManager().createNewSession(p, data.get());
        return true;
    }

}
