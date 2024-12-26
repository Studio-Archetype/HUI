package com.volmit.holoui;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.volmit.holoui.config.HuiSettings;
import com.volmit.holoui.config.MenuDefinitionData;
import com.volmit.holoui.utils.SchedulerUtils;

import java.util.Optional;

@CommandPermission("holoui.command")
@CommandAlias("holoui|holo|hui|holou|hu")
public class HoloCommand extends BaseCommand {

    public static final String PREFIX = "[HoloUI]: ";
    public static final String ROOT_PERM = "holoui.command";

    @Default
    @Subcommand("list")
    @Description("List all menus")
    public void list(CommandSender sender) {
        if(!sender.hasPermission(ROOT_PERM + ".list")) {
            sender.sendMessage(PREFIX + ChatColor.RED + "You do not have permission to run this command.");
            return;
        }
        if(HoloUI.INSTANCE.getConfigManager().keys().isEmpty()) {
            sender.sendMessage(PREFIX + ChatColor.GRAY + "No menus are available.");
            return;
        }

        sender.sendMessage(ChatColor.GRAY + "----------+=== Menus ===+----------");
        HoloUI.INSTANCE.getConfigManager().keys().forEach(s -> sender.sendMessage(ChatColor.GRAY + "  - " + ChatColor.WHITE + s));
        sender.sendMessage(ChatColor.GRAY + "----------------------------------");
    }

    @Default
    @Subcommand("open")
    @Description("Open a menu")
    public void open(CommandSender sender) {
        list(sender);
    }

    @Default
    @Subcommand("open")
    @Description("Open a menu")
    public void open(Player player, String ui) {
        Optional<MenuDefinitionData> data = HoloUI.INSTANCE.getConfigManager().get(ui);
        if(data.isEmpty()) {
            player.sendMessage(PREFIX + ChatColor.RED + "\"" + ui + "\" is not available.");
            return;
        }
        if(!player.hasPermission(ROOT_PERM + ".open")) {
            player.sendMessage(PREFIX + ChatColor.RED + "You do not have permission to run this command.");
            return;
        }
        if(!player.hasPermission("holoui.open." + ui)) {
            player.sendMessage(PREFIX + ChatColor.RED + "You lack permission to open \"" + ui + "\".");
            return;
        }

        try {
            HoloUI.INSTANCE.getSessionManager().createNewSession(player, data.get());
        } catch(NullPointerException e) {
            HoloUI.logExceptionStack(true, e, "Null in session creation?");
        }
    }

    @Default
    @Subcommand("close")
    @Description("Close the current menu")
    public void close(Player player) {
        if(!player.hasPermission(ROOT_PERM + ".close")) {
            player.sendMessage(PREFIX + ChatColor.RED + "You do not have permission to run this command.");
            return;
        }

        if(HoloUI.INSTANCE.getSessionManager().destroySession(player))
            player.sendMessage(PREFIX + ChatColor.GREEN + "Menu closed.");
        else
            player.sendMessage(PREFIX + ChatColor.RED + "No menu is currently open.");
    }

    @Default
    @Subcommand("builder")
    @Description("Builder server status")
    public void serverStatus(CommandSender sender) {
        if(!sender.hasPermission(ROOT_PERM + ".server_status")) {
            sender.sendMessage(PREFIX + ChatColor.RED + "You do not have permission to run this command.");
            return;
        }

        if(HoloUI.INSTANCE.getBuilderServer().isServerRunning()) {
            String host = HuiSettings.BUILDER_IP.value().equalsIgnoreCase("0.0.0.0") ? "localhost" : HuiSettings.BUILDER_IP.value();
            String url = host + ":" + HuiSettings.BUILDER_PORT.value();
            sender.spigot().sendMessage(new ComponentBuilder(PREFIX)
                    .append(new ComponentBuilder("Builder is running at ")
                            .color(net.md_5.bungee.api.ChatColor.GREEN)
                            .create())
                    .append(new ComponentBuilder(url)
                            .underlined(true)
                            .color(net.md_5.bungee.api.ChatColor.WHITE)
                            .event(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://" + url))
                            .create())
                    .append(new ComponentBuilder(".")
                            .color(net.md_5.bungee.api.ChatColor.GREEN)
                            .underlined(false)
                            .create())
                    .create());
        } else {
            sender.spigot().sendMessage(new ComponentBuilder(PREFIX)
                    .append(new ComponentBuilder("Builder is not running. Start it ")
                            .color(net.md_5.bungee.api.ChatColor.RED)
                            .create())
                    .append(new ComponentBuilder("here")
                            .underlined(true)
                            .color(net.md_5.bungee.api.ChatColor.WHITE)
                            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/holoui builder start"))
                            .create())
                    .append(new ComponentBuilder(", or visit ")
                            .color(net.md_5.bungee.api.ChatColor.RED)
                            .underlined(false)
                            .create())
                    .append(new ComponentBuilder("here")
                            .underlined(true)
                            .color(net.md_5.bungee.api.ChatColor.WHITE)
                            .event(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://holoui.studioarchetype.net/"))
                            .create())
                    .append(new ComponentBuilder(" for a online version.")
                            .color(net.md_5.bungee.api.ChatColor.RED)
                            .underlined(false)
                            .create())
                    .create());
        }
    }

    @Default
    @Subcommand("builder start")
    @Description("Start the builder server")
    public void startServer(CommandSender sender) {
        BuilderServer server = HoloUI.INSTANCE.getBuilderServer();
        if(!sender.hasPermission(ROOT_PERM + ".server_start")) {
            sender.sendMessage(PREFIX + ChatColor.RED + "You do not have permission to run this command.");
            return;
        }
        if(server.isServerRunning()) {
            sender.sendMessage(PREFIX + ChatColor.RED + "Builder is already running.");
            return;
        }
        SchedulerUtils.runAsync(HoloUI.INSTANCE, () -> {
            sender.sendMessage(PREFIX + ChatColor.GREEN + "Starting builder...");
            if(!server.prepareServer())
                sender.sendMessage(PREFIX + ChatColor.RED + "An error occurred while setting up the builder! Check the logs for details.");
            server.startServer(HuiSettings.BUILDER_IP.value(), HuiSettings.BUILDER_PORT.value());
            serverStatus(sender);
        });
    }

    @Default
    @Subcommand("builder stop")
    @Description("Stopps the builder server")
    public void stopServer(CommandSender sender) {
        if(!sender.hasPermission(ROOT_PERM + ".server_stop")) {
            sender.sendMessage(PREFIX + ChatColor.RED + "You do not have permission to run this command.");
            return;
        }
        if(HoloUI.INSTANCE.getBuilderServer().stopServer())
            sender.sendMessage(PREFIX + ChatColor.GREEN + "Builder has been stopped.");
        else
            sender.sendMessage(PREFIX + ChatColor.RED + "Builder is not running.");
    }
}
