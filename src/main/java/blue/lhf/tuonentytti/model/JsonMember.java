package blue.lhf.tuonentytti.model;

public record JsonMember(JsonString key, JsonValue<?> value) {
    @Override
    public String toString() {
        return key + ": " + value;
    }
}
