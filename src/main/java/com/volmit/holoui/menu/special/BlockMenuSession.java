package com.volmit.holoui.menu.special;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import com.volmit.holoui.config.HuiSettings;
import com.volmit.holoui.config.MenuDefinitionData;
import com.volmit.holoui.menu.MenuSession;
import com.volmit.holoui.utils.math.MathHelper;

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
        double distance = getPlayer().getLocation().distance(centerPoint);
        /*if(distance <= MIN_DISTANCE || distance >= MAX_DISTANCE)
            return false;*/
        return lookingAt.equals(this.block);
    }

    @Override
    public void rotate(float yaw) {
        super.rotate(yaw);
        this.initialY = yaw;
    }

    @Override
    public void move(Location loc, boolean byPlayer) {
        if(!HuiSettings.PREVIEW_FOLLOW_PLAYER.value())
            this.centerPoint = block.getLocation().add(getOffset()).clone().subtract(-.5, -.5, -.5);
        else
            this.centerPoint = loc.add(getOffset());
        rotateCenter();
        adjustRotation(byPlayer);
    }

    public void open() {
        this.initialY = -(float)MathHelper.getRotationFromDirection(getPlayer().getEyeLocation().getDirection()).getY();
        getComponents().forEach(c -> c.open(false));
    }
}
