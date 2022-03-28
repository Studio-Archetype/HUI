package studio.archetype.hologui2.utils.codec;

import com.mojang.serialization.Codec;

public interface CodecDispatcherEnum<T> {
    Codec<? extends T> getCodec();
}
