package studio.archetype.holoui;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import studio.archetype.holoui.config.ConfigManager;
import studio.archetype.holoui.menu.MenuSessionManager;

import javax.imageio.ImageIO;
import java.util.logging.Level;

@Getter
public final class HoloUI extends JavaPlugin {

    public static final String VERSION = "1.0.0";

    public static HoloUI INSTANCE;

    private HoloCommand command;
    private ConfigManager configManager;
    private MenuSessionManager sessionManager;

    private BuilderServer builderServer;

    @Override
    public void onEnable() {
        INSTANCE = this;

        ImageIO.scanForPlugins();

        this.configManager = new ConfigManager(getDataFolder());
        this.command = new HoloCommand();
        this.sessionManager = new MenuSessionManager();

        this.builderServer = new BuilderServer(getDataFolder());
    }

    @Override
    public void onDisable() {
        configManager.shutdown();
        sessionManager.destroyAll();
        builderServer.stopServer();
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
