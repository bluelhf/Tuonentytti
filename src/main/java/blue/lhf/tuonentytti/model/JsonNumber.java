package blue.lhf.tuonentytti.model;

public record JsonNumber(double inner) implements JsonValue<Number> {
    @Override
    public Number get() {
        return inner;
    }

    @Override
    public String toString() {
        return String.valueOf(inner);
    }
}
