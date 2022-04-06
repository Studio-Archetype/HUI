package studio.archetype.holoui.utils.settings;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import studio.archetype.holoui.HoloUI;
import studio.archetype.holoui.utils.file.FileWatcher;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Level;

public abstract class Settings {

    private final Map<String, Entry<?>> fields;

    private final File file;
    private final FileWatcher fileWatcher;

    public Settings(File file) {
        this.file = file;
        this.fileWatcher = new FileWatcher(file);
        this.fields = Maps.newHashMap();

        registerFields();
        doReload(false);
    }

    public void update() {
        if(fileWatcher.checkModified())
            doReload(true);
    }

    protected abstract void registerFields();

    protected void registerField(String field, Entry<?> entry) {
        this.fields.putIfAbsent(field, entry);
    }

    private void doReload(boolean triggerListeners) {
        try(FileReader reader = new FileReader(file)) {
            JsonElement element = JsonParser.parseReader(reader);
            JsonObject obj = element.getAsJsonObject();
            fields.forEach((f, e) -> e.update(f, obj, triggerListeners));
        } catch(IOException | JsonParseException e) {
            HoloUI.log(Level.WARNING, "An error occurred while reloading the settings file:");
            if(e.getMessage() != null)
                HoloUI.log(Level.WARNING, "\t%s: %s", e.getClass().getSimpleName(), e.getMessage());
            else
                HoloUI.log(Level.WARNING, "\t%s", e.getClass().getSimpleName());
        }

    }

    @RequiredArgsConstructor
    public static class Entry<V> {

        private final EntryType<V> type;
        private final V defaultValue;
        private final Consumer<V> onChange;

        @Getter
        private V value;

        private void update(String key, JsonObject obj, boolean triggerListener) {
            if(!obj.has(key))
                this.value = defaultValue;
            else
                this.value = type.parse(key, obj);
            if(triggerListener)
                onChange.accept(this.value);
        }

        private void setValue(V value) {
            if(!value.equals(this.value))
                return;
            this.value = value;
            onChange.accept(this.value);
        }

        private void reset() {
            setValue(this.defaultValue);
        }
    }
}
