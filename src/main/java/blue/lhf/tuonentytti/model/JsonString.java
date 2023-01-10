package blue.lhf.tuonentytti.model;

public record JsonString(String inner) implements JsonValue<String> {

    @Override
    public String get() {
        return inner;
    }

    @Override
    public String toString() {
        return "\"" + inner + "\"";
    }
}
