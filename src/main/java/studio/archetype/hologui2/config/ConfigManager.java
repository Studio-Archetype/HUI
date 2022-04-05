package studio.archetype.hologui2.config;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import studio.archetype.hologui2.HoloGUI;
import studio.archetype.hologui2.utils.SchedulerUtils;
import studio.archetype.hologui2.utils.file.FileWatcher;
import studio.archetype.hologui2.utils.file.FolderWatcher;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;

public final class ConfigManager {

    private final Map<String, MenuDefinitionData> menuRegistry = Maps.newHashMap();

    private final File imageDir;
    private final FolderWatcher menuDefinitionFolder;
    private final FileWatcher settings;

    public ConfigManager(File configDir) {
        this.imageDir = new File(configDir, "images");
        if(!imageDir.exists())
            imageDir.mkdirs();

        menuDefinitionFolder = new FolderWatcher(new File(configDir, "menus"));
        settings = new FileWatcher(new File(configDir, "settings.json"));

        loadConfigs();

        SchedulerUtils.scheduleSyncTask(HoloGUI.INSTANCE, 5L, () -> {
            if(menuDefinitionFolder.checkModifiedFast()) {
                menuDefinitionFolder.getChanged().forEach(f -> {
                    String name = FilenameUtils.getBaseName(f.getName());
                    Optional<MenuDefinitionData> data = loadConfig(name, f);
                    data.ifPresent(d -> {
                        //HoloGUI.INSTANCE.getSessionManager().destroyAllType(name);
                        menuRegistry.put(name, d);
                        HoloGUI.log(Level.INFO, "Menu config \"%s\" has been changed and re-registered.", name);
                    });
                });
            }
            if(settings.checkModified()) {
                //TODO Reload Settings
            }
        }, true);
        SchedulerUtils.scheduleSyncTask(HoloGUI.INSTANCE, 20L, () -> {
            if(menuDefinitionFolder.checkModified()) {
                menuDefinitionFolder.getCreated().forEach(f -> {
                    String name = FilenameUtils.getBaseName(f.getName());
                    Optional<MenuDefinitionData> data = loadConfig(name, f);
                    data.ifPresent(d -> {
                        menuRegistry.put(name, d);
                        HoloGUI.log(Level.INFO, "New menu config \"%s\" detected and registered.", name);
                    });
                });
                menuDefinitionFolder.getDeleted().forEach(f -> {
                    String name = FilenameUtils.getBaseName(f.getName());
                    if(menuRegistry.containsKey(name)) {
                        HoloGUI.INSTANCE.getSessionManager().destroyAllType(name);
                        menuRegistry.remove(name);
                        HoloGUI.log(Level.INFO, "Menu config \"%s\" has been deleted and unregistered.", name);
                    }
                });
            }
        }, true);
    }

    public Set<String> keys() {
        return menuRegistry.keySet();
    }

    public Optional<MenuDefinitionData> get(String key) {
        return exists(key) ? Optional.of(menuRegistry.get(key)) : Optional.empty();
    }

    public boolean exists(String key) {
        return menuRegistry.containsKey(key);
    }

    public BufferedImage hasImage(String relative) throws IOException {
        File f = new File(imageDir, relative.endsWith(".png") ? relative : relative + ".png");
        if(!f.exists() || f.isDirectory())
            throw new FileNotFoundException();
        return ImageIO.read(f);
    }

    private void loadConfigs() {
        menuDefinitionFolder.getWatchers().keySet().forEach(f -> {
            if(f.getPath().contains("menus")) {
                String name = FilenameUtils.getBaseName(f.getName());
                Optional<MenuDefinitionData> data = loadConfig(name, f);
                data.ifPresent(d -> {
                    menuRegistry.put(name, d);
                    HoloGUI.log(Level.INFO, "Registered menu config \"%s\".", name);
                });
            }
        });
    }

    private Optional<MenuDefinitionData> loadConfig(String menuName, File f) {
        try(FileReader reader = new FileReader(f)) {
            if(FileUtils.sizeOf(f) == 0) {
                HoloGUI.log(Level.WARNING, "Menu config \"%s.json\" is empty, ignoring.", menuName);
                return Optional.empty();
            }

            DataResult<Pair<MenuDefinitionData, JsonElement>> result = JsonOps.INSTANCE.withDecoder(MenuDefinitionData.CODEC).apply(JsonParser.parseReader(reader));
            if(result.error().isPresent())
                HoloGUI.log(Level.WARNING, "Failed to parse menu config \"%s.json\":\n\t%s", menuName, result.error().get().message());
            else {
                if(result.result().isEmpty())
                    HoloGUI.log(Level.WARNING, "An unknown error occurred while parsing menu config \"%s.json\"! Skipping.", menuName);
                else {
                    MenuDefinitionData data = result.result().get().getFirst();
                    data.setId(menuName);
                    return Optional.of(data);
                }
            }
        } catch(IOException | JsonParseException ex) {
            HoloGUI.log(Level.WARNING, "A %s occurred while parsing menu config \"%s.json\":\n\t", ex.getClass().getSimpleName(), menuName, ex.getMessage());
        }
        return Optional.empty();
    }
}
