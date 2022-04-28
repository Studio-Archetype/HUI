package studio.archetype.holoui.utils.codec;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.trunkplacers.BendingTrunkPlacer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.util.Vector;
import studio.archetype.holoui.utils.ItemUtils;

public final class Codecs {

    public static final Codec<Material> MATERIAL = ResourceLocation.CODEC.xmap(ItemUtils::identifierToMaterial, ItemUtils::materialToIdentifier);
    public static final Codec<Vector> VECTOR = Codec.DOUBLE.listOf().xmap(l -> new Vector(l.get(0), l.get(1), l.get(2)), v -> Lists.newArrayList(v.getX(), v.getY(), v.getZ()));

    public static final Codec<Sound> SOUND = ResourceLocation.CODEC.xmap(s -> {
       NamespacedKey key = NamespacedKey.fromString(s.toString());
        for(Sound value : Sound.values())
            if(value.getKey().equals(key))
                return value;
        return Sound.BLOCK_GLASS_BREAK;
    }, s -> new ResourceLocation(s.getKey().getNamespace(), s.getKey().getKey()));
}
