package com.volmit.holoui.config;

import com.volmit.holoui.HoloUI;
import com.volmit.holoui.utils.settings.EntryType;
import com.volmit.holoui.utils.settings.Settings;

import java.io.File;

public class HuiSettings extends Settings {

    public static final Entry<Boolean> DEBUG_HITBOX = new Entry<>(EntryType.BOOLEAN, false, b -> HoloUI.INSTANCE.getSessionManager().controlHitboxDebug(b));
    public static final Entry<Boolean> DEBUG_SPACING = new Entry<>(EntryType.BOOLEAN, false, b -> HoloUI.INSTANCE.getSessionManager().controlPositionDebug(b));
    public static final Entry<String> BUILDER_IP = new Entry<>(EntryType.STRING, "0.0.0.0", b -> {
    });
    public static final Entry<Integer> BUILDER_PORT = new Entry<>(EntryType.INTEGER, 8080, i -> {
    });
    public static final Entry<Boolean> PREVIEW_FOLLOW_PLAYER = new Entry<>(EntryType.BOOLEAN, false, i -> {
    });

    public HuiSettings(File configDir) {
        super(new File(configDir, "settings.json"));
    }

    @Override
    protected void registerFields() {
        registerField("debugHitbox", DEBUG_HITBOX);
        registerField("debugPosition", DEBUG_SPACING);
        registerField("builderIp", BUILDER_IP);
        registerField("builderPort", BUILDER_PORT);

        registerField("previewFollowPlayer", PREVIEW_FOLLOW_PLAYER);
    }
}
