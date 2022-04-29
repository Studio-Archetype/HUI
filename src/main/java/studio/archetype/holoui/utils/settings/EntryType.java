package studio.archetype.holoui.utils.settings;

import com.google.gson.JsonObject;

public interface EntryType<E> {
    E parse(String key, JsonObject element);
    void serialize(String key, E object, JsonObject json);

    EntryType<Boolean> BOOLEAN = new EntryType<>() {
        public Boolean parse(String key, JsonObject element) { return element.get(key).getAsBoolean(); }
        public void serialize(String key, Boolean object, JsonObject json) { json.addProperty(key, object); }
    };

    EntryType<Integer> INTEGER = new EntryType<>() {
        public Integer parse(String key, JsonObject element) { return element.get(key).getAsInt(); }
        public void serialize(String key, Integer object, JsonObject json) { json.addProperty(key, object); }
    };

    /*EntryType<Byte> BYTE = (key, element) -> element.get(key).getAsByte();
    EntryType<Short> SHORT = (key, element) -> element.get(key).getAsShort();
    EntryType<Long> LONG = (key, element) -> element.get(key).getAsLong();

    EntryType<Float> FLOAT = (key, element) -> element.get(key).getAsFloat();
    EntryType<Double> DOUBLE = (key, element) -> element.get(key).getAsDouble();*/
}
