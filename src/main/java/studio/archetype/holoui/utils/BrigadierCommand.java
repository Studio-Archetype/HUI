package studio.archetype.holoui.utils;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_19_R2.CraftServer;

public abstract class BrigadierCommand {

    public BrigadierCommand(String commandName, String rootPermission, String... alias) {
        LiteralArgumentBuilder<CommandSourceStack> rootNode = literal(commandName);
        rootNode.requires(src -> src.getBukkitSender().hasPermission(rootPermission));
        register(rootNode);
        LiteralCommandNode<CommandSourceStack> node = getDispatcher().register(rootNode);
        for(String a : alias)
            getDispatcher().register(literal(a).redirect(node));
    }

    protected abstract void register(LiteralArgumentBuilder<CommandSourceStack> rootNode);

    protected LiteralArgumentBuilder<CommandSourceStack> literal(String s) {
        return LiteralArgumentBuilder.literal(s);
    }

    protected <T> RequiredArgumentBuilder<CommandSourceStack, T> argument(String s, ArgumentType<T> argumentType) {
        return RequiredArgumentBuilder.argument(s, argumentType);
    }

    private CommandDispatcher<CommandSourceStack> getDispatcher() {
        return ((CraftServer) Bukkit.getServer()).getHandle().getServer().vanillaCommandDispatcher.getDispatcher();
    }
}
