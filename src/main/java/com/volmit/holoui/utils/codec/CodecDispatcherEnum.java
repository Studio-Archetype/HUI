package com.volmit.holoui.utils.codec;

import com.mojang.serialization.Codec;

public interface CodecDispatcherEnum<T> {
    Codec<? extends T> getCodec();
}
