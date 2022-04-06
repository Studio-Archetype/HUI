package studio.archetype.holoui.utils;

import net.minecraft.core.Rotations;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class ArmorStandBuilder {

    private final ArmorStand armorStand;

    public ArmorStandBuilder(World w) { armorStand = EntityType.ARMOR_STAND.create(NMSUtils.level(w)); }
    public ArmorStandBuilder(Player w) { armorStand = EntityType.ARMOR_STAND.create(NMSUtils.level(w.getWorld())); }

    public ArmorStandBuilder pos(Location loc) {
        armorStand.setPos(NMSUtils.vec3(loc));
        return this;
    }

    public ArmorStandBuilder small(boolean small) {
        armorStand.setSmall(small);
        return this;
    }

    public ArmorStandBuilder marker(boolean marker) {
        armorStand.setMarker(marker);
        return this;
    }

    public ArmorStandBuilder invisible(boolean invisible) {
        armorStand.setInvisible(invisible);
        return this;
    }

    public ArmorStandBuilder basePlate(boolean basePlate) {
        armorStand.setNoBasePlate(!basePlate);
        return this;
    }

    public ArmorStandBuilder gravity(boolean gravity) {
        armorStand.setNoGravity(!gravity);
        return this;
    }

    public ArmorStandBuilder zeroPose() {
        armorStand.setYBodyRot(0);
        armorStand.setYHeadRot(0);
        armorStand.setYRot(0);
        return headPose(0, 0, 0)
                .bodyPose(0, 0, 0)
                .leftArm(0, 0, 0)
                .rightArm(0, 0, 0)
                .leftLeg(0, 0, 0)
                .rightLeg(0, 0, 0);
    }

    public ArmorStandBuilder headPose(float x, float y, float z) {
        armorStand.setHeadPose(new Rotations(x, y, z));
        return this;
    }

    public ArmorStandBuilder bodyPose(float x, float y, float z) {
        armorStand.setBodyPose(new Rotations(x, y, z));
        return this;
    }

    public ArmorStandBuilder leftArm(float x, float y, float z) {
        armorStand.setLeftArmPose(new Rotations(x, y, z));
        return this;
    }

    public ArmorStandBuilder rightArm(float x, float y, float z) {
        armorStand.setRightArmPose(new Rotations(x, y, z));
        return this;
    }

    public ArmorStandBuilder leftLeg(float x, float y, float z) {
        armorStand.setLeftLegPose(new Rotations(x, y, z));
        return this;
    }

    public ArmorStandBuilder rightLeg(float x, float y, float z) {
        armorStand.setRightArmPose(new Rotations(x, y, z));
        return this;
    }

    public ArmorStandBuilder name(String name) { return name(name, true); }
    public ArmorStandBuilder name(Component name) { return name(name, true); }
    public ArmorStandBuilder name(String name, boolean visible) { return name(new TextComponent(name), visible); }

    public ArmorStandBuilder name(Component name, boolean visible) {
        armorStand.setCustomName(name);
        armorStand.setCustomNameVisible(visible);
        return this;
    }

    public ArmorStandBuilder helmet(ItemStack stack) { return helmet(stack, false); }
    public ArmorStandBuilder helmet(ItemStack stack, boolean silent) { return equipment(EquipmentSlot.HEAD, stack, silent); }
    public ArmorStandBuilder chestPlate(ItemStack stack) { return chestPlate(stack, false); }
    public ArmorStandBuilder chestPlate(ItemStack stack, boolean silent) { return equipment(EquipmentSlot.CHEST, stack, silent); }
    public ArmorStandBuilder leggings(ItemStack stack) { return leggings(stack, false); }
    public ArmorStandBuilder leggings(ItemStack stack, boolean silent) { return equipment(EquipmentSlot.LEGS, stack, silent); }
    public ArmorStandBuilder boots(ItemStack stack) { return boots(stack, false); }
    public ArmorStandBuilder boots(ItemStack stack, boolean silent) { return equipment(EquipmentSlot.FEET, stack, silent); }
    public ArmorStandBuilder mainHand(ItemStack stack) { return mainHand(stack, false); }
    public ArmorStandBuilder mainHand(ItemStack stack, boolean silent) { return equipment(EquipmentSlot.MAINHAND, stack, silent); }
    public ArmorStandBuilder offHand(ItemStack stack) { return offHand(stack, false); }
    public ArmorStandBuilder offHand(ItemStack stack, boolean silent) { return equipment(EquipmentSlot.OFFHAND, stack, silent); }

    private ArmorStandBuilder equipment(EquipmentSlot slot, ItemStack stack, boolean silent) {
        armorStand.setItemSlot(slot, NMSUtils.notchianItemStack(stack), silent);
        return this;
    }

    public ArmorStand build() {
        return armorStand;
    }

    public static ArmorStand nametagArmorStand(Component component, Location loc) {
        return new ArmorStandBuilder(loc.getWorld())
                .marker(true).gravity(false)
                .invisible(true).basePlate(false).zeroPose()
                .name(component, true).pos(loc)
                .build();
    }
}
