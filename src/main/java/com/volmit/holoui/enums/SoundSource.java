package com.volmit.holoui.enums;

import com.mojang.serialization.Codec;
import com.volmit.holoui.utils.codec.EnumCodec;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.SoundCategory;

import java.util.Locale;

@AllArgsConstructor
@Getter
public enum SoundSource implements EnumCodec.Values {
    MASTER(SoundCategory.MASTER),
    MUSIC(SoundCategory.MUSIC),
    RECORD(SoundCategory.RECORDS),
    WEATHER(SoundCategory.WEATHER),
    BLOCK(SoundCategory.BLOCKS),
    HOSTILE(SoundCategory.HOSTILE),
    NEUTRAL(SoundCategory.NEUTRAL),
    PLAYER(SoundCategory.PLAYERS),
    AMBIENT(SoundCategory.AMBIENT),
    VOICE(SoundCategory.VOICE);

    public static final Codec<SoundSource> CODEC = new EnumCodec<>(SoundSource.class);

    private final SoundCategory category;

    public String getSerializedName() {
        return this.name().toLowerCase(Locale.ROOT);
    }
}
