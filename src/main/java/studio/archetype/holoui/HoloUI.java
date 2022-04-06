package studio.archetype.holoui;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import studio.archetype.holoui.config.ConfigManager;
import studio.archetype.holoui.menu.MenuSessionManager;

import java.util.logging.Level;

@Getter
public final class HoloUI extends JavaPlugin {

    public static final String VERSION = "1.0.0";

    public static HoloUI INSTANCE;

    private HoloCommand command;
    private ConfigManager configManager;
    private MenuSessionManager sessionManager;

    @Override
    public void onEnable() {
        INSTANCE = this;
        this.configManager = new ConfigManager(getDataFolder());
        this.command = new HoloCommand();
        this.sessionManager = new MenuSessionManager();
    }

    @Override
    public void onDisable() {
        sessionManager.destroyAll();
    }

    public static void log(Level logLevel, String s, Object... args) {
        INSTANCE.getLogger().log(logLevel, args.length > 0 ? String.format(s, args) : s);
    }
}
