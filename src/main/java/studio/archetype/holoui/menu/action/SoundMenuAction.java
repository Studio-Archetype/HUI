package studio.archetype.holoui.menu.action;

import studio.archetype.holoui.config.action.SoundActionData;
import studio.archetype.holoui.menu.MenuSession;

public class SoundMenuAction extends MenuAction<SoundActionData> {

    public SoundMenuAction(SoundActionData data) { super(data); }

    @Override
    public void execute(MenuSession session) {
        session.getPlayer().playSound(session.getPlayer(), data.sound(), data.source().getCategory(), data.volume(), data.pitch());
    }
}
