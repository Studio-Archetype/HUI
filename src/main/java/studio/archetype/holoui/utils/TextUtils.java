package studio.archetype.holoui.utils;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TextComponent;

public final class TextUtils {

    public static MutableComponent textColor(String text, String hexColor) {
        return new TextComponent(text).setStyle(Style.EMPTY.withColor(TextColor.parseColor(hexColor)));
    }

    public static MutableComponent textColor(String text, int hexColor) {
        return new TextComponent(text).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(hexColor)));
    }
}
