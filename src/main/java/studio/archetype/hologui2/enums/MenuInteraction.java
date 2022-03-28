package studio.archetype.hologui2.enums;

import com.mojang.serialization.Codec;
import lombok.AllArgsConstructor;
import studio.archetype.hologui2.utils.codec.EnumCodec;

@AllArgsConstructor
public enum MenuInteraction implements EnumCodec.Values {

    MOUSE_RIGHT("mouseRight"),
    MOUSE_LEFT("mouseLeft");

    public static final Codec<MenuInteraction> CODEC = new EnumCodec<>(MenuInteraction.class);

    private final String value;

    @Override
    public String getSerializedName() { return value; }
}
