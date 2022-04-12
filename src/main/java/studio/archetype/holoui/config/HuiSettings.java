package studio.archetype.holoui.config;

import studio.archetype.holoui.HoloUI;
import studio.archetype.holoui.utils.settings.EntryType;
import studio.archetype.holoui.utils.settings.Settings;

import java.io.File;

public class HuiSettings extends Settings {

    public static final Entry<Boolean> DEBUG = new Entry<>(EntryType.BOOLEAN, false, (b) -> HoloUI.INSTANCE.getSessionManager().controlDebugTask(b));

    public HuiSettings(File configDir) {
        super(new File(configDir, "settings.json"));
    }

    @Override
    protected void registerFields() {
        registerField("debug", DEBUG);
    }
}
