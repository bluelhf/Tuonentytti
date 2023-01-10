package blue.lhf.tuonentytti.model;

public class JsonNull<T> implements JsonValue<T> {
    @Override
    public T get() {
        return null;
    }

    @Override
    public String toString() {
        return "null";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof JsonNull<?>;
    }

    @Override
    public int hashCode() {
        return getClass().getCanonicalName().hashCode();
    }
}
