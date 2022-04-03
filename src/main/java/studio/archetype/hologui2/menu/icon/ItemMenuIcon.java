package studio.archetype.hologui2.menu.icon;

import com.google.common.collect.Lists;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;
import studio.archetype.hologui2.config.icon.ItemIconData;
import studio.archetype.hologui2.menu.ArmorStandManager;
import studio.archetype.hologui2.utils.ArmorStandBuilder;
import studio.archetype.hologui2.utils.ItemUtils;
import studio.archetype.hologui2.utils.math.CollisionPlane;

import java.util.List;
import java.util.UUID;

public class ItemMenuIcon extends MenuIcon<ItemIconData> {

    public static final float NAMETAG_OFFSET = 1.975F * 0.85F / 5;
    public static final float ITEM_OFFSET = 1.975F * 0.85F / 3;

    private final ItemStack item;

    public ItemMenuIcon(ItemIconData data) {
        super(data);
        this.item = new ItemUtils.Builder(data.materialType())
                .modelData(data.customModelValue())
                .get();
    }

    @Override
    protected CollisionPlane createBoundingBox(Location loc) {
        return null;
    }

    protected List<UUID> createArmorStands(Location loc) {
        ArmorStandBuilder stand = new ArmorStandBuilder(loc.getWorld())
                .basePlate(false).gravity(false)
                .invisible(false)
                .zeroPose()
                .pos(loc.subtract(0, NAMETAG_OFFSET, 0));
        if(data.materialType().isBlock()) {
            stand.helmet(item);
            return Lists.newArrayList(ArmorStandManager.add(stand.build()));
        } else {
            ArmorStandBuilder itemStand = new ArmorStandBuilder(loc.getWorld())
                    .basePlate(false)
                    .gravity(false)
                    .invisible(false)
                    .zeroPose()
                    .helmet(item)
                    .pos(loc.subtract(0, ITEM_OFFSET, 0));
            return Lists.newArrayList(ArmorStandManager.add(stand.build()), ArmorStandManager.add(itemStand.build()));
        }

    }
}
