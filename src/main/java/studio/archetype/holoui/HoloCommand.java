package studio.archetype.holoui;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.minecraft.commands.CommandSourceStack;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import studio.archetype.holoui.config.HuiSettings;
import studio.archetype.holoui.config.MenuDefinitionData;
import studio.archetype.holoui.utils.BrigadierCommand;
import studio.archetype.holoui.utils.SchedulerUtils;

import java.util.Optional;

public final class HoloCommand extends BrigadierCommand {

    public static final String PREFIX = "[HoloUI]: ";
    public static final String ROOT_PERM = "test.permission.hologui";

    private static final String CMD = "holoui";
    private static final String[] ALIASES = { "holo", "hui", "holou", "hu" };

    public HoloCommand() { super(CMD, ROOT_PERM, ALIASES); }

    @Override
    protected void register(LiteralArgumentBuilder<CommandSourceStack> rootNode) {
        rootNode.executes(HoloCommand::info)
                .then(literal("list")
                        .executes(HoloCommand::list))
                .then(literal("open")
                        .executes(HoloCommand::list)
                        .then(argument("ui", StringArgumentType.string())
                                .executes(ctx -> open(ctx, StringArgumentType.getString(ctx, "ui")))))
                .then(literal("close")
                        .executes(HoloCommand::close))
                .then(literal("builder")
                        .executes(HoloCommand::serverStatus)
                        .then(literal("start")
                                .executes(HoloCommand::startServer))
                        .then(literal("stop")
                                .executes(HoloCommand::stopServer)));
    }

    private static int info(CommandContext<CommandSourceStack> ctx) {
        ctx.getSource().getBukkitSender().spigot().sendMessage(new ComponentBuilder(PREFIX)
                .append(new ComponentBuilder("You are running" + ChatColor.WHITE + " HoloUI " + HoloUI.VERSION + ChatColor.GRAY + " by ")
                        .color(net.md_5.bungee.api.ChatColor.GRAY)
                        .create())
                .append(new ComponentBuilder("Studio Archetype")
                        .underlined(true)
                        .color(net.md_5.bungee.api.ChatColor.WHITE)
                        .event(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://studioarchetype.net/"))
                        .create())
                .append(new ComponentBuilder(".")
                        .color(net.md_5.bungee.api.ChatColor.GRAY)
                        .underlined(false)
                        .create())
                .create());
        return 1;
    }

    private static int open(CommandContext<CommandSourceStack> ctx, String ui) throws CommandSyntaxException {
        Player p = ctx.getSource().getPlayerOrException().getBukkitEntity();
        Optional<MenuDefinitionData> data = HoloUI.INSTANCE.getConfigManager().get(ui);
        if(data.isEmpty()) {
            p.sendMessage(PREFIX + ChatColor.RED + "\"" + ui + "\" is not available.");
            return 1;
        }
        HoloUI.INSTANCE.getSessionManager().createNewSession(p, data.get());
        return 1;
    }

    private static int list(CommandContext<CommandSourceStack> ctx) {
        CommandSender sender = ctx.getSource().getBukkitSender();
        if(HoloUI.INSTANCE.getConfigManager().keys().isEmpty()) {
            sender.sendMessage(PREFIX + ChatColor.GRAY + "No menus are available.");
            return 1;
        }
        sender.sendMessage(ChatColor.GRAY + "----------+=== Menus ===+----------");
        HoloUI.INSTANCE.getConfigManager().keys().forEach(s -> sender.sendMessage(ChatColor.GRAY + "  - " + ChatColor.WHITE + s));
        sender.sendMessage(ChatColor.GRAY + "----------------------------------");
        return 1;
    }

    private static int close(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Player p = ctx.getSource().getPlayerOrException().getBukkitEntity();
        if(HoloUI.INSTANCE.getSessionManager().destroySession(p))
            p.sendMessage(PREFIX + ChatColor.GREEN + "Menu closed.");
        else
            p.sendMessage(PREFIX + ChatColor.RED + "No menu is currently open.");
        return 1;
    }

    private static int startServer(CommandContext<CommandSourceStack> ctx) {
        BuilderServer server = HoloUI.INSTANCE.getBuilderServer();
        CommandSender sender = ctx.getSource().getBukkitSender();
        if(server.isServerRunning()) {
            sender.sendMessage(PREFIX + ChatColor.RED + "Builder is already running.");
            return 1;
        }
        SchedulerUtils.runAsync(HoloUI.INSTANCE, () -> {
            sender.sendMessage(PREFIX + ChatColor.GREEN + "Starting builder...");
            if(!server.prepareServer())
                sender.sendMessage(PREFIX + ChatColor.RED + "An error occurred while setting up the builder! Check the logs for details.");
            server.startServer(HuiSettings.BUILDER_IP.value(), HuiSettings.BUILDER_PORT.value());
            serverStatus(ctx);
        });
        return 1;
    }

    private static int serverStatus(CommandContext<CommandSourceStack> ctx) {
        CommandSender sender = ctx.getSource().getBukkitSender();
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
        return 1;
    }

    private static int stopServer(CommandContext<CommandSourceStack> ctx) {
        CommandSender sender = ctx.getSource().getBukkitSender();
        if(HoloUI.INSTANCE.getBuilderServer().stopServer())
            sender.sendMessage(PREFIX + ChatColor.GREEN + "Builder has been stopped.");
        else
            sender.sendMessage(PREFIX + ChatColor.RED + "Builder is not running.");
        return 1;
    }
}
