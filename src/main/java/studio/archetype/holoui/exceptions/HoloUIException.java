package studio.archetype.holoui.exceptions;

import lombok.Getter;

public abstract class HoloUIException extends Exception {

    @Getter
    private final ComponentType type;

    public HoloUIException(ComponentType type, String message) {
        super(message);
        this.type = type;
    }

    public HoloUIException(ComponentType type, String format, Object... objects) {
        super(String.format(format, objects));
        this.type = type;
    }

    public enum ComponentType {
        ICON,
        COMPONENT,
        ACTION
    }
}
