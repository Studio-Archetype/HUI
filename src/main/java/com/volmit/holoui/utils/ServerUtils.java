package com.volmit.holoui.utils;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandMap;

import java.lang.reflect.Method;

public final class ServerUtils {

    public static CommandMap getCommandMap() {
        try {
            Server server = Bukkit.getServer();
            Method method = server.getClass().getDeclaredMethod("getCommandMap");
            method.setAccessible(true);
            return (CommandMap) method.invoke(server);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void syncCommands() {
        try {
            Server server = Bukkit.getServer();
            Method method = server.getClass().getDeclaredMethod("syncCommands");
            method.setAccessible(true);
            method.invoke(server);
        } catch (Throwable ignored) {
        }
    }
}
