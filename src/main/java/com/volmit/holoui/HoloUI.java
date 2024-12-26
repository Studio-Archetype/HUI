package com.volmit.holoui;

import co.aikar.commands.PaperCommandManager;
import com.github.retrooper.packetevents.PacketEvents;
import com.volmit.holoui.config.MenuDefinitionData;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import lombok.Getter;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import com.volmit.holoui.config.ConfigManager;
import com.volmit.holoui.menu.MenuSessionManager;
import com.volmit.holoui.utils.TextUtils;

import javax.imageio.ImageIO;
import java.util.logging.Level;

@Getter
public final class HoloUI extends JavaPlugin {
    public static HoloUI INSTANCE;

    private PaperCommandManager commandManager;
    private HoloCommand command;
    private ConfigManager configManager;
    private MenuSessionManager sessionManager;

    private BuilderServer builderServer;
    private Metrics metrics;

    @Override
    public void onLoad() {
        INSTANCE = this;

        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().load();
    }

    @Override
    public void onEnable() {
        ImageIO.scanForPlugins();
        PacketEvents.getAPI().init();
        TextUtils.splash(getLogger());

        this.configManager = new ConfigManager(getDataFolder());
        this.commandManager = new PaperCommandManager(this);
        this.command = new HoloCommand();
        command.registerCompletions(commandManager.getCommandCompletions());
        command.registerContexts(commandManager.getCommandContexts());
        commandManager.registerCommand(command);

        this.sessionManager = new MenuSessionManager();

        this.builderServer = new BuilderServer(getDataFolder());
        this.metrics = new Metrics(this, 24222);
    }

    @Override
    public void onDisable() {
        configManager.shutdown();
        sessionManager.destroyAll();
        commandManager.unregisterCommands();
        PacketEvents.getAPI().terminate();

        builderServer.stopServer();
        metrics.shutdown();
    }

    public static void log(Level logLevel, String s, Object... args) {
        INSTANCE.getLogger().log(logLevel, args.length > 0 ? String.format(s, args) : s);
    }

    public static void logException(boolean isSevere, Throwable e, int indents) {
        StringBuilder format = new StringBuilder("%s%s");
        for(int i = 0; i < indents; i++)
            format.insert(0, "\t");
        log(isSevere ? Level.SEVERE : Level.WARNING,
                format.toString(), e.getClass().getSimpleName(), e.getMessage() != null ? " - " + e.getMessage() : "");
    }

    public static void logExceptionStack(boolean isSevere, Throwable e, String s, Object... args) {
        log(isSevere ? Level.SEVERE : Level.WARNING, s, args);
        int indent = 1;
        Throwable throwable = e;
        while(throwable != null) {
            logException(isSevere, throwable, indent++);
            throwable = throwable.getCause();
        }
    }
}
