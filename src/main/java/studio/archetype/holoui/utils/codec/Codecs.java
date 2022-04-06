package studio.archetype.holoui.utils.codec;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import org.bukkit.Material;
import org.bukkit.util.Vector;
import studio.archetype.holoui.utils.ItemUtils;

public final class Codecs {

    public static final Codec<Material> MATERIAL = ResourceLocation.CODEC.xmap(ItemUtils::identifierToMaterial, ItemUtils::materialToIdentifier);
    public static final Codec<Vector> VECTOR = Codec.DOUBLE.listOf().xmap(l -> new Vector(l.get(0), l.get(1), l.get(2)), v -> Lists.newArrayList(v.getX(), v.getY(), v.getZ()));
}
