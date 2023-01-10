package blue.lhf.tuonentytti.model;

public record JsonMember(String key, JsonValue<?> value) {
    @Override
    public String toString() {
        return "\"" + key + "\": " + value;
    }
}
