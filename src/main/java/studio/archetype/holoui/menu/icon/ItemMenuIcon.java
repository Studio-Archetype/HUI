package studio.archetype.holoui.menu.icon;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.network.chat.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import studio.archetype.holoui.config.icon.ItemIconData;
import studio.archetype.holoui.exceptions.MenuIconException;
import studio.archetype.holoui.menu.ArmorStandManager;
import studio.archetype.holoui.menu.MenuSession;
import studio.archetype.holoui.utils.ArmorStandBuilder;
import studio.archetype.holoui.utils.ItemUtils;
import studio.archetype.holoui.utils.math.CollisionPlane;
import studio.archetype.holoui.utils.math.MathHelper;

import java.util.List;
import java.util.UUID;

public class ItemMenuIcon extends MenuIcon<ItemIconData> {

    private static final float ITEM_OFFSET = 1F;
    private static final float BLOCK_OFFSET = -.95F;

    private final ItemStack item;

    public ItemMenuIcon(MenuSession session, Location loc, ItemIconData data) throws MenuIconException {
        super(session, loc, data);
        this.item = new ItemUtils.Builder(data.materialType(), data.count() > 0 ? data.count() : 1)
                .modelData(data.customModelValue())
                .get();
    }

    @Override // TODO Bounding Box Item
    public CollisionPlane createBoundingBox() {
        return null;
    }

    protected List<UUID> createArmorStands(Location loc) {
        List<UUID> uuids = Lists.newArrayList();
        Location location = loc.clone();
        if(isBlock())
            location.add(0, BLOCK_OFFSET, 0);
        else
            location.subtract(0, ITEM_OFFSET + (item.getAmount() > 1 ? 0 : .09F), 0);
        ArmorStandBuilder builder = ArmorStandBuilder.itemArmorStand(item, location).small(true);
        uuids.add(ArmorStandManager.add(builder.build()));
        if(item.getAmount() > 1) {
            loc.add(0F, -NAMETAG_SIZE - .15F, 0);
            Component count = Component.literal(String.valueOf(item.getAmount()));
            uuids.add(ArmorStandManager.add(ArmorStandBuilder.nametagArmorStand(count, loc)));
        }
        return uuids;
    }

    public void updateCount(int count) {
        if(armorStands.size() == 1 && count > 1) {
            ArmorStandManager.move(armorStands.get(0), new Vector(0, .09F, 0));
            UUID armorStand = ArmorStandManager.add(ArmorStandBuilder.nametagArmorStand(Component.literal(String.valueOf(count)), position.clone().add(0F, -NAMETAG_SIZE - .37F, 0)));
            armorStands.add(armorStand);
            ArmorStandManager.spawn(armorStand, session.getPlayer());
        } else if(armorStands.size() == 2 && count < 2){
            ArmorStandManager.move(armorStands.get(0), new Vector(0, -.09F, 0));
            ArmorStandManager.delete(armorStands.get(1));
            armorStands.remove(1);
        } else {
            ArmorStandManager.changeName(armorStands.get(1), Component.literal(String.valueOf(count)));
        }
    }

    @Override
    public void spawn() {
        super.spawn();
        rotate((float)MathHelper.getRotationFromDirection(session.getPlayer().getEyeLocation().getDirection().multiply(-1F)).getY());
    }

    @Override
    public void rotate(float yaw) {
        if(isBlock()) {
            Location offset = MathHelper.rotateAroundPoint(this.position.clone().add(0, BLOCK_OFFSET, .3F), this.position, 0, yaw);
            ArmorStandManager.goTo(armorStands.get(0), offset);
            super.rotate(-yaw + 180);
        } else
            super.rotate(-yaw + 180);
    }

    private boolean isBlock() {
        return item.getType().isBlock() && !BLOCK_BLACKLIST.contains(item.getType());
    }


    private static final List<Material> BLOCK_BLACKLIST = ImmutableList.of(
            Material.BARRIER, Material.LIGHT, Material.HOPPER, Material.TURTLE_EGG, Material.GRASS, Material.TALL_GRASS,
            Material.WHITE_STAINED_GLASS_PANE, Material.ORANGE_STAINED_GLASS_PANE, Material.MAGENTA_STAINED_GLASS_PANE,
            Material.LIGHT_BLUE_STAINED_GLASS_PANE, Material.YELLOW_STAINED_GLASS_PANE, Material.LIME_STAINED_GLASS_PANE,
            Material.PINK_STAINED_GLASS_PANE, Material.GRAY_STAINED_GLASS_PANE, Material.LIGHT_GRAY_STAINED_GLASS_PANE,
            Material.CYAN_STAINED_GLASS_PANE, Material.PURPLE_STAINED_GLASS_PANE, Material.BLUE_STAINED_GLASS_PANE,
            Material.BROWN_STAINED_GLASS_PANE, Material.GREEN_STAINED_GLASS_PANE, Material.RED_STAINED_GLASS_PANE,
            Material.BLACK_STAINED_GLASS_PANE, Material.GLASS_PANE, Material.POPPY, Material.DANDELION);
}
