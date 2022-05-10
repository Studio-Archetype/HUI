package studio.archetype.holoui.utils.settings;

import com.google.gson.JsonObject;

public interface EntryType<E> {
    E parse(String key, JsonObject element);
    void serialize(String key, E object, JsonObject json);

    EntryType<Boolean> BOOLEAN = new EntryType<>() {
        public Boolean parse(String key, JsonObject element) { return element.get(key).getAsBoolean(); }
        public void serialize(String key, Boolean object, JsonObject json) { json.addProperty(key, object); }
    };

    EntryType<String> STRING = new EntryType<>() {
        public String parse(String key, JsonObject element) { return element.get(key).getAsString(); }
        public void serialize(String key, String object, JsonObject json) { json.addProperty(key, object); }
    };

    EntryType<Byte> BYTE = new EntryType<>() {
        public Byte parse(String key, JsonObject element) { return element.get(key).getAsByte(); }
        public void serialize(String key, Byte object, JsonObject json) { json.addProperty(key, object); }
    };

    EntryType<Short> SHORT = new EntryType<>() {
        public Short parse(String key, JsonObject element) { return element.get(key).getAsShort(); }
        public void serialize(String key, Short object, JsonObject json) { json.addProperty(key, object); }
    };

    EntryType<Integer> INTEGER = new EntryType<>() {
        public Integer parse(String key, JsonObject element) { return element.get(key).getAsInt(); }
        public void serialize(String key, Integer object, JsonObject json) { json.addProperty(key, object); }
    };

    EntryType<Long> LONG = new EntryType<>() {
        public Long parse(String key, JsonObject element) { return element.get(key).getAsLong(); }
        public void serialize(String key, Long object, JsonObject json) { json.addProperty(key, object); }
    };

    EntryType<Float> FLOAT = new EntryType<>() {
        public Float parse(String key, JsonObject element) { return element.get(key).getAsFloat(); }
        public void serialize(String key, Float object, JsonObject json) { json.addProperty(key, object); }
    };

    EntryType<Double> DOUBLE = new EntryType<>() {
        public Double parse(String key, JsonObject element) { return element.get(key).getAsDouble(); }
        public void serialize(String key, Double object, JsonObject json) { json.addProperty(key, object); }
    };
}
