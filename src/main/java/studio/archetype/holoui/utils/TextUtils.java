package studio.archetype.holoui.utils;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

public final class TextUtils {

    public static MutableComponent textColor(String text, String hexColor) {
        return Component.literal(text).setStyle(Style.EMPTY.withColor(TextColor.parseColor(hexColor)));
    }

    public static MutableComponent textColor(String text, int hexColor) {
        return Component.literal(text).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(hexColor)));
    }
}
