package studio.archetype.holoui.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;

public final class TextUtils {

    public static Component textColor(String text, String hexColor) {
        return Component.text(text).color(TextColor.fromHexString(hexColor));
    }

    public static Component textColor(String text, int hexColor) {
        return Component.text(text).color(TextColor.color(hexColor));
    }

    public static String content(Component component) {
        StringBuilder builder = new StringBuilder();
        if (component instanceof TextComponent text) {
            builder.append(text.content());
        }

        for (Component child : component.children()) {
            builder.append(content(child));
        }
        return builder.toString();
    }
}
