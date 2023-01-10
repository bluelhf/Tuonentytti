package blue.lhf.tuonentytti.model;

import java.lang.reflect.*;
import java.util.*;

public interface JsonValue<T> {
    T get();

    static JsonValue<?> coerce(final Object object) {
        if (object instanceof JsonValue<?> value) return value;
        if (object == null) return new JsonNull<>();
        if (object instanceof Number num) {
            return new JsonNumber(num.doubleValue());
        } else if (object instanceof Boolean bool) {
            return new JsonBoolean(bool);
        } else if (object instanceof String text) {
            return new JsonString(text);
        } else if (object.getClass().isArray()) {
            final JsonArray array = new JsonArray();
            for (int i = 0, len = Array.getLength(object); i < len; ++i) {
                array.add(coerce(Array.get(object, i)));
            }
            return array;
        } else if (object instanceof Map<?, ?> map) {
            final JsonObject output = new JsonObject();
            try {
                final Map<String, Object> smap = (Map<String, Object>) map;
                for (final var entry : smap.entrySet()) {
                    output.add(new JsonMember(entry.getKey(), coerce(entry.getValue())));
                }

            } catch (ClassCastException ignored) {
            }
            return output;
        } else {
            final JsonObject output = new JsonObject();
            final Field[] fields = object.getClass().getFields();
            for (final Field field : fields) {
                try {
                    output.add(new JsonMember(field.getName(), coerce(field.get(object))));
                } catch (IllegalAccessException e) {
                    output.add(new JsonMember(field.getName(), new JsonNull<>()));
                }
            }

            return output;
        }
    }
}
