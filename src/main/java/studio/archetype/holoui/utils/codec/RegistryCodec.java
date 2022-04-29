package studio.archetype.holoui.utils.codec;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;

import java.util.Map;

public class RegistryCodec<K, V extends Serializable<K>> {

    private final Codec<V> valueCodec;
    private final Map<K, Codec<? extends V>> registry = Maps.newHashMap();

    public RegistryCodec(Codec<K> keyCodec) {
        this.valueCodec = keyCodec.dispatch(Serializable::serialize, registry::get);
    }

    public void register(K key, Codec<? extends V> codec) {
        registry.putIfAbsent(key, codec);
    }

    public Codec<V> getCodec() {
        return this.valueCodec;
    }
}
