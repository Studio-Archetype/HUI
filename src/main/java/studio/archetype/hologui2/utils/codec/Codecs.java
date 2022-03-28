package studio.archetype.hologui2.utils.codec;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import org.bukkit.Material;
import studio.archetype.hologui2.utils.ItemUtils;

public final class Codecs {

    public static final Codec<Material> MATERIAL = ResourceLocation.CODEC.xmap(ItemUtils::identifierToMaterial, ItemUtils::materialToIdentifier);
}
