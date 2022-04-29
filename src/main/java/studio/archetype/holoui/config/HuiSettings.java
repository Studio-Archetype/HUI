package studio.archetype.holoui.config;

import studio.archetype.holoui.HoloUI;
import studio.archetype.holoui.utils.settings.EntryType;
import studio.archetype.holoui.utils.settings.Settings;

import java.io.File;

public class HuiSettings extends Settings {

    public static final Entry<Boolean> DEBUG_HITBOX = new Entry<>(EntryType.BOOLEAN, false, b -> HoloUI.INSTANCE.getSessionManager().controlHitboxDebug(b));
    public static final Entry<Boolean> DEBUG_SPACING = new Entry<>(EntryType.BOOLEAN, false, b -> HoloUI.INSTANCE.getSessionManager().controlPositionDebug(b));
    public static final Entry<Integer> BUILDER_PORT = new Entry<>(EntryType.INTEGER, 8080, i -> { });

    public HuiSettings(File configDir) {
        super(new File(configDir, "settings.json"));
    }

    @Override
    protected void registerFields() {
        registerField("debugHitbox", DEBUG_HITBOX);
        registerField("debugPosition", DEBUG_SPACING);
        registerField("builderPort", BUILDER_PORT);
    }
}
