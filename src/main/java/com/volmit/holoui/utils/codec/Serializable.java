package com.volmit.holoui.utils.codec;

@FunctionalInterface
public interface Serializable<K> {
    K serialize();
}
