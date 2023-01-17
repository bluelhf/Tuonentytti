package blue.lhf.tuonentytti.parsing;

public class JsonParseException extends Exception {
    public JsonParseException(final String message) {
        super(message);
    }

    public JsonParseException copy() {
        return new JsonParseException(this.getMessage());
    }
}
