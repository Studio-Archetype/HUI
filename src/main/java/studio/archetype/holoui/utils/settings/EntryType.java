package studio.archetype.holoui.utils.settings;

import com.google.gson.JsonObject;

@FunctionalInterface
public interface EntryType<E> {
    E parse(String key, JsonObject element);

    EntryType<Boolean> BOOLEAN = (key, element) -> element.get(key).getAsBoolean();

    EntryType<Byte> BYTE = (key, element) -> element.get(key).getAsByte();
    EntryType<Short> SHORT = (key, element) -> element.get(key).getAsShort();
    EntryType<Integer> INT = (key, element) -> element.get(key).getAsInt();
    EntryType<Long> LONG = (key, element) -> element.get(key).getAsLong();

    EntryType<Float> FLOAT = (key, element) -> element.get(key).getAsFloat();
    EntryType<Double> DOUBLE = (key, element) -> element.get(key).getAsDouble();
}
