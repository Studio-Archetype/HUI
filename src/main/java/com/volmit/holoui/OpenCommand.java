package com.volmit.holoui;

import com.google.common.collect.Lists;
import com.volmit.holoui.config.MenuDefinitionData;
import com.volmit.holoui.utils.SimpleCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class OpenCommand extends SimpleCommand {

    public OpenCommand(String name) {
        super(name, "Opens the " + name + " menu.", "/" + name, Lists.newArrayList());
        setPermission("hui.open." + name);
        setPermissionMessage(HoloCommand.PREFIX + ChatColor.RED + "You lack permission to open \"" + getName() + "\".");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage(HoloCommand.PREFIX + ChatColor.RED + "Direct menus can only be executed by players.");
            return true;
        }
        Optional<MenuDefinitionData> data = HoloUI.INSTANCE.getConfigManager().get(getName());
        if (data.isEmpty()) {
            p.sendMessage(HoloCommand.PREFIX + ChatColor.RED + "\"" + getName() + "\" is not available.");
            return true;
        }
        HoloUI.INSTANCE.getSessionManager().createNewSession(p, data.get());
        return true;
    }

}
