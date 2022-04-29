package studio.archetype.holoui.utils.codec;

@FunctionalInterface
public interface Serializable<K> {
    K serialize();
}
