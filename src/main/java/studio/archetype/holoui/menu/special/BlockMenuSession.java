package studio.archetype.holoui.menu.special;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import studio.archetype.holoui.config.MenuDefinitionData;
import studio.archetype.holoui.menu.MenuSession;

public class BlockMenuSession extends MenuSession {

    private static final float MIN_DISTANCE = 2.75F;
    private static final float MAX_DISTANCE = 10.75F;

    private final Block block;

    public BlockMenuSession(MenuDefinitionData data, Player p, Block b) {
        super(data, p);
        this.block = b;
    }

    // TODO configurable max distance
    public boolean shouldRender(Block lookingAt) {
        double distance = getPlayer().getLocation().distance(block.getLocation().add(.5, .5, .5));
        if(distance <= MIN_DISTANCE || distance >= MAX_DISTANCE)
            return false;
        return lookingAt.equals(this.block);
    }

    @Override
    public void rotate(float yaw) {
        super.rotate(yaw);
        this.initialY = yaw;
    }
}
