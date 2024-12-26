package com.volmit.holoui.utils;

import com.google.common.collect.Lists;
import org.bukkit.command.Command;

import java.util.List;
import java.util.Optional;

public abstract class SimpleCommand extends Command {

    private static final List<SimpleCommand> REGISTRY = Lists.newArrayList();

    public static boolean isRegistered(String name) {
        return getCommand(name).isPresent();
    }

    public static Optional<SimpleCommand> getCommand(String name) {
        return REGISTRY.stream().filter(cmd -> cmd.getName().equalsIgnoreCase(name)).findFirst();
    }

    public static boolean register(SimpleCommand cmd) {
        if(isRegistered(cmd.getName()))
            return false;
        if (ServerUtils.getCommandMap().register("holoui", cmd)) {
            REGISTRY.add(cmd);
            ServerUtils.syncCommands();
            return true;
        }
        return false;
    }

    public static boolean unregister(String name) {
        Optional<SimpleCommand> cmd = getCommand(name);
        if(cmd.isEmpty()) {
            return true;
        } else if(cmd.get().unregister(ServerUtils.getCommandMap())) {
            REGISTRY.remove(cmd.get());
            ServerUtils.syncCommands();
            return true;
        } else {
            return false;
        }
    }

    protected SimpleCommand(String name, String description, String usageMessage, List<String> aliases) {
        super(name, description, usageMessage, aliases);
    }
}
