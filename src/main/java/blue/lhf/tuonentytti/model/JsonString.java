package blue.lhf.tuonentytti.model;

import blue.lhf.tuonentytti.parsing.JsonLexer;

public record JsonString(String inner) implements JsonValue<String> {

    @Override
    public String get() {
        return inner;
    }

    @Override
    public String toString() {
        return "\"" + JsonLexer.quote(inner) + "\"";
    }
}
