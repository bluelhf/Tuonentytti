package blue.lhf.tuonentytti.model;

import java.util.*;

import static java.util.stream.Collectors.joining;

public class JsonArray extends ArrayList<JsonValue<?>> implements JsonValue<List<?>> {
    public JsonArray() {

    }

    public JsonArray(Collection<JsonValue<?>> elements) {
        addAll(elements);
    }

    public JsonArray(JsonValue<?>... values) {
        this(List.of(values));
    }

    public static JsonArray from(final Object... values) {
        final JsonArray array = new JsonArray();
        for (final Object object : values) {
            array.add(JsonValue.coerce(object));
        }

        return array;
    }

    @Override
    public List<?> get() {
        return stream().map(JsonValue::get).toList();
    }

    @Override
    public String toString() {
        return "[" + stream().map(JsonValue::toString).collect(joining(", ")) + "]";
    }
}
