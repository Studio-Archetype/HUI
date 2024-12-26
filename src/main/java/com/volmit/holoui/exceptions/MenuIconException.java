package com.volmit.holoui.exceptions;

public class MenuIconException extends HoloUIException {
    public MenuIconException(String message) {
        super(ComponentType.ICON, message);
    }

    public MenuIconException(String format, Object... objects) {
        super(ComponentType.ICON, format, objects);
    }
}
