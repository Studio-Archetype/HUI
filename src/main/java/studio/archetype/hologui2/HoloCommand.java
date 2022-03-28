package studio.archetype.hologui2;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.sun.jdi.connect.Connector;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import net.minecraft.commands.CommandSourceStack;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import studio.archetype.hologui2.config.MenuDefinitionData;
import studio.archetype.hologui2.menu.MenuSession;
import studio.archetype.hologui2.utils.BrigadierCommand;

import java.util.Optional;

public final class HoloCommand extends BrigadierCommand {

    private static final String CMD = "hui";
    private static final String[] ALIASES = { "holo", "holoui", "holou", "hu" };
    private static final String PREFIX = "[HUI]: ";

    public static final String ROOT_PERM = "test.permission.hologui";

    public HoloCommand() { super(CMD, ROOT_PERM, ALIASES); }

    @Override
    protected void register(LiteralArgumentBuilder<CommandSourceStack> rootNode) {
        rootNode.executes(HoloCommand::info)
                .then(literal("list")
                        .executes(HoloCommand::list))
                .then(literal("open")
                        .then(argument("ui", StringArgumentType.string())
                                .executes(ctx -> open(ctx, StringArgumentType.getString(ctx, "ui")))))
                .then(literal("close")
                        .executes(HoloCommand::close));
    }

    private static int info(CommandContext<CommandSourceStack> ctx) {
        return 1;
    }

    private static int open(CommandContext<CommandSourceStack> ctx, String ui) throws CommandSyntaxException {
        Player p = ctx.getSource().getPlayerOrException().getBukkitEntity();
        Optional<MenuDefinitionData> data = HoloGUI.INSTANCE.getConfigManager().get(ui);
        if(data.isEmpty()) {
            p.sendMessage(PREFIX + ChatColor.RED + "\"" + ui + "\" is not a registered menu.");
            return 1;
        }
        HoloGUI.INSTANCE.getSessionManager().createNewSession(p, data.get());
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TranslatableComponent(data.get().getTranslationKey()));
        return 1;
    }

    private static int list(CommandContext<CommandSourceStack> ctx) {
        CommandSender sender = ctx.getSource().getBukkitSender();
        sender.sendMessage(
                PREFIX + ChatColor.GRAY + "Listing registered menus...",
                ChatColor.GRAY + "------------------------------");
        HoloGUI.INSTANCE.getConfigManager().keys().forEach(s -> sender.sendMessage(ChatColor.GRAY + "\t- " + ChatColor.WHITE + s));
        sender.sendMessage(ChatColor.GRAY + "------------------------------");
        return 1;
    }

    private static int close(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Player p = ctx.getSource().getPlayerOrException().getBukkitEntity();
        if(HoloGUI.INSTANCE.getSessionManager().destroySession(p))
            p.sendMessage(PREFIX + ChatColor.GREEN + "Menu closed.");
        else
            p.sendMessage(PREFIX + ChatColor.RED + "No menu is currently open.");
        return 1;
    }
}
