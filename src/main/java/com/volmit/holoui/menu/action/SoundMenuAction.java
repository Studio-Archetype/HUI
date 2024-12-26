package com.volmit.holoui.menu.action;

import com.volmit.holoui.config.action.SoundActionData;
import com.volmit.holoui.menu.MenuSession;

public class SoundMenuAction extends MenuAction<SoundActionData> {

    public SoundMenuAction(SoundActionData data) { super(data); }

    @Override
    public void execute(MenuSession session) {
        session.getPlayer().playSound(session.getPlayer().getLocation(), data.sound(), data.source().getCategory(), data.volume(), data.pitch());
    }
}
