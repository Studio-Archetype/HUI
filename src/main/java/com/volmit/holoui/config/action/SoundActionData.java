package com.volmit.holoui.config.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.bukkit.Sound;
import com.volmit.holoui.enums.MenuActionType;
import com.volmit.holoui.enums.SoundSource;
import com.volmit.holoui.utils.codec.Codecs;

public record SoundActionData(Sound sound, SoundSource source, float volume, float pitch) implements MenuActionData {

    public MenuActionType getType() { return MenuActionType.SOUND; }

    public static final Codec<SoundActionData> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codecs.SOUND.fieldOf("sound").forGetter(SoundActionData::sound),
            SoundSource.CODEC.optionalFieldOf("source", SoundSource.MASTER).forGetter(SoundActionData::source),
            Codec.FLOAT.optionalFieldOf("volume", 1.0F).forGetter(SoundActionData::volume),
            Codec.FLOAT.optionalFieldOf("pitch", 1.0F).forGetter(SoundActionData::pitch)
    ).apply(i, SoundActionData::new));
}
