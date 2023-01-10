package blue.lhf.tuonentytti.model;

public record JsonBoolean(boolean inner) implements JsonValue<Boolean> {
    @Override
    public Boolean get() {
        return inner;
    }

    @Override
    public String toString() {
        return String.valueOf(inner);
    }
}
