package blue.lhf.tuonentytti.parsing;

import blue.lhf.tuonentytti.model.*;
import blue.lhf.tuonentytti.reader.*;

import java.io.IOException;
import java.util.*;

public class JsonParser {
    public static final JsonParseException JSON_OBJECT_PREFIX_MISMATCH = new JsonParseException("Objects must start with '{'");
    public static final JsonParseException JSON_OBJECT_SUFFIX_MISMATCH = new JsonParseException("Objects must end in '}'");
    public static final JsonParseException JSON_MEMBER_COLON_MISMATCH = new JsonParseException("There must be a ':' between a member's name and its value");
    public static final JsonParseException JSON_ARRAY_PREFIX_MISMATCH = new JsonParseException("Arrays must start with '['");
    public static final JsonParseException JSON_ARRAY_SUFFIX_MISMATCH = new JsonParseException("Arrays must end in ']'");
    public static final JsonParseException JSON_STRING_PREFIX_MISMATCH = new JsonParseException("Strings must start with '\"'");
    public static final JsonParseException JSON_STRING_SUFFIX_MISMATCH = new JsonParseException("Strings must end in '\"'");

    private JsonParser() {

    }

    public static <T extends JsonValue<?>> T parseAs(final Class<T> clazz, final Source source)
        throws IOException, ClassCastException, JsonParseException {
        return clazz.cast(parse(source));
    }

    public static JsonValue<?> parse(final Source source) throws IOException, JsonParseException {
        return readElement(source);
    }

    // <editor-fold desc="Objects" defaultstate="collapsed">
    static JsonObject readObject(final Source source) throws IOException, JsonParseException {
        if (source.read() != '{') throw JSON_OBJECT_PREFIX_MISMATCH;
        final List<JsonMember> members = Branches.match(
            source, List.of(JsonParser::readMembers, Branch.constant(JsonParser::readWhitespace, List.of())));
        if (source.read() != '}') throw JSON_OBJECT_SUFFIX_MISMATCH;

        return new JsonObject(members);
    }

    static List<JsonMember> readMembers(final Source source) throws IOException, JsonParseException {
        final List<JsonMember> members = new ArrayList<>();
        do {
            final JsonMember member = readMember(source);
            members.add(member);
        } while (Branches.match(source, ','));
        return members;
    }

    static JsonMember readMember(final Source source) throws IOException, JsonParseException {
        readWhitespace(source);
        final JsonString name = readString(source);
        readWhitespace(source);
        if (source.read() != ':') throw JSON_MEMBER_COLON_MISMATCH;
        final JsonValue<?> element = readElement(source);
        return new JsonMember(name.get(), element);
    }

    // </editor-fold>

    // <editor-fold desc="Arrays" defaultstate="collapsed">
    static JsonArray readArray(final Source source) throws IOException, JsonParseException {
        if (source.read() != '[') throw JSON_ARRAY_PREFIX_MISMATCH;

        final List<JsonValue<?>> values = Branches.match(source,
            List.of(JsonParser::readElements, Branch.constant(JsonParser::readWhitespace, List.of())));

        if (source.read() != ']') throw JSON_ARRAY_SUFFIX_MISMATCH;
        return new JsonArray(values);
    }

    static List<JsonValue<?>> readElements(final Source source) throws IOException, JsonParseException {
        final List<JsonValue<?>> elements = new ArrayList<>();
        do {
            final JsonValue<?> value = readElement(source);
            elements.add(value);
        } while (Branches.match(source, ','));
        return elements;
    }

    static JsonValue<?> readElement(final Source source) throws IOException, JsonParseException {
        readWhitespace(source);
        final JsonValue<?> value = readValue(source);
        readWhitespace(source);
        return value;
    }
    // </editor-fold>

    // <editor-fold desc="Primitives" defaultstate="collapsed">
    static JsonValue<?> readValue(final Source source) throws IOException, JsonParseException {
        return Branches.match(source,
            List.of(JsonParser::readObject, JsonParser::readArray, JsonParser::readString,
                JsonParser::readNumber, JsonParser::readBoolean, JsonParser::readNull));
    }

    static JsonString readString(final Source source) throws IOException, JsonParseException {
        if (source.read() != '"') throw JSON_STRING_PREFIX_MISMATCH;
        final String characters = JsonLexer.characters(source);
        if (source.read() != '"') throw JSON_STRING_SUFFIX_MISMATCH;
        return new JsonString(characters);
    }

    static JsonNumber readNumber(final Source source) throws IOException, JsonParseException {
        final String integer = JsonLexer.integer(source);
        final String fraction = JsonLexer.fraction(source);
        final String exponent = JsonLexer.exponent(source);

        // JSON numbers are a sub-set of doubles
        final double num = Double.parseDouble(integer + fraction + exponent);

        return new JsonNumber(num);
    }

    static JsonBoolean readBoolean(final Source source) throws IOException, JsonParseException {
        return new JsonBoolean(Boolean.parseBoolean(
            Branches.match(source, List.of(Branch.literal("true"), Branch.literal("false")))));
    }

    static <T> JsonNull<T> readNull(final Source source) throws IOException, JsonParseException {
        return Branch.constant(Branch.literal("null"), new JsonNull<T>()).apply(source);
    }
    // </editor-fold>

    static <T> JsonValue<T> readWhitespace(final Source source) throws IOException {
        //noinspection StatementWithEmptyBody — Side effect of reader#match() breaks this
        while (Branches.match(source, ' ', '\n', '\r', '\t')) { }
        return null;
    }
}
