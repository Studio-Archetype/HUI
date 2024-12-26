package com.volmit.holoui.config.icon;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.EitherCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.volmit.holoui.enums.MenuIconType;

import java.util.List;

public record AnimatedImageData(Either<String, List<String>> source, int speed) implements MenuIconData {

    public MenuIconType getType() { return MenuIconType.ANIMATED_TEXT_IMAGE; }

    public static final Codec<AnimatedImageData> CODEC = RecordCodecBuilder.create(i -> i.group(
            new EitherCodec<>(Codec.STRING, Codec.STRING.listOf()).fieldOf("source").forGetter(AnimatedImageData::source),
            Codec.INT.fieldOf("speed").forGetter(AnimatedImageData::speed)
    ).apply(i, AnimatedImageData::new));
}
