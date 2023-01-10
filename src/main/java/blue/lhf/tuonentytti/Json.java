package blue.lhf.tuonentytti;

import blue.lhf.tuonentytti.model.*;
import blue.lhf.tuonentytti.source.*;
import blue.lhf.tuonentytti.util.*;

import java.nio.channels.*;
import java.nio.file.*;
import java.util.*;
import java.util.function.*;

public final class Json {
    private Json() {

    }

    public static <T extends JsonValue<?>> T parse(final Source source) throws Exception {
        return (T) readValue(source);
    }

    public static <T extends JsonValue<?>> T parse(final SeekableByteChannel channel) throws Exception {
        return (T) readValue(new ByteChannelSource(channel));
    }

    public static <T extends JsonValue<?>> T parse(final Path path, final OpenOption... options) throws Exception {
        return (T) parse(FileChannel.open(path, options));
    }

    public static <T extends JsonValue<?>> T parse(final String input) throws Exception {
        return (T) readValue(new StringSource(input));
    }

    static <T> void ifNonNull(T t, Consumer<T> function) {
        if (t != null) function.accept(t);
    }

    @SafeVarargs
    static <T> T oneOf(final ThrowingSupplier<T, ?>... suppliers) throws Exception {
        for (final ThrowingSupplier<T, ?> supplier : suppliers) {
            final T candidate = supplier.get();
            if (candidate != null) return candidate;
        }

        return null;
    }

    static <T> List<T> readDelimited(final Source reader, final String delimeter,
                                     final ThrowingFunction<Source, T, ?> readSingle) throws Exception {
        return oneOf(() -> reader.attempt(copy -> {
            T first = readSingle.apply(copy);
            if (first == null) return null;
            if (!copy.match(delimeter)) return null;
            List<T> rest = readDelimited(copy, delimeter, readSingle);
            if (rest == null) return null;

            rest.add(0, first);
            return rest;
        }), () -> {
            final T member = reader.attempt(readSingle);
            if (member != null) return new ArrayList<>(List.of(member));
            return null;
        });
    }

    static JsonValue<?> readValue(final Source reader) throws Exception {
        return oneOf(
            () -> reader.attempt(Json::readObject),
            () -> reader.attempt(Json::readArray),
            () -> reader.attempt(Json::readString),
            () -> reader.attempt(Json::readNumber),
            () -> reader.attempt(Json::readBoolean),
            () -> reader.attempt(Json::readNull)
        );
    }

    static JsonArray readArray(final Source reader) throws Exception {
        return oneOf(() -> reader.attempt(copy -> {
            if (copy.read() != '[') return null;
            readWhitespace(copy);
            if (copy.read() != ']') return null;
            return new JsonArray();
        }), () -> reader.attempt(copy -> {
            if (copy.read() != '[') return null;
            final List<JsonValue<?>> elements = readElements(copy);
            if (copy.read() != ']') return null;
            return new JsonArray(elements);
        }));
    }

    static JsonObject readObject(final Source reader) throws Exception {
        return oneOf(() -> reader.attempt(copy -> {
            if (copy.read() != '{') return null;
            readWhitespace(copy);
            if (copy.read() != '}') return null;
            return new JsonObject();
        }), () -> reader.attempt(copy -> {
            if (copy.read() != '{') return null;
            final List<JsonMember> members = readMembers(copy);
            if (copy.read() != '}') return null;
            return new JsonObject(members);
        }));
    }

    static List<JsonMember> readMembers(final Source reader) throws Exception {
        return readDelimited(reader, ",", Json::readMember);
    }

    static List<JsonValue<?>> readElements(final Source reader) throws Exception {
        return readDelimited(reader, ",", Json::readElement);
    }

    static JsonMember readMember(final Source reader) throws Exception {
        readWhitespace(reader);
        final JsonValue<String> key = readString(reader);
        if (key == null) return null;
        readWhitespace(reader);
        if (reader.read() != ':') return null;
        final JsonValue<?> value = readElement(reader);
        return new JsonMember(key.get(), value);
    }

    static JsonValue<?> readElement(final Source source) throws Exception {
        readWhitespace(source);
        final JsonValue<?> value = readValue(source);
        readWhitespace(source);
        return value;
    }

    static Object readWhitespace(final Source reader) throws Exception {
        if (reader.match(" ") || reader.match("\n") || reader.match("\r") || reader.match("\t")) {
            return readWhitespace(reader);
        }

        return null;
    }

    static JsonBoolean readBoolean(final Source reader) throws Exception {
        if (reader.startsWith("true")) return new JsonBoolean(true);
        if (reader.startsWith("false")) return new JsonBoolean(false);
        return null;
    }

    static JsonNull<?> readNull(final Source reader) throws Exception {
        if (reader.startsWith("null")) return new JsonNull<>();
        return null;
    }

    static JsonValue<String> readString(final Source reader) throws Exception {
        if (reader.read() != '"') return null;
        final String chars = reader.attempt(Json::charactersToken);
        if (reader.read() != '"') return null;
        return new JsonString(chars);
    }

    static JsonNumber readNumber(final Source reader) throws Exception {
        final String integer = integerToken(reader);
        if (integer == null) return null;

        final StringBuilder sb = new StringBuilder(integer);

        final String fraction = reader.attempt(Json::fractionToken);
        final String exponent = reader.attempt(Json::exponentToken);

        ifNonNull(fraction, sb::append);
        ifNonNull(exponent, sb::append);

        return new JsonNumber(Double.parseDouble(sb.toString()));
    }

    static String exponentToken(Source reader) throws Exception {
        if (!reader.match("e") && !reader.match("E")) return null;
        final String sign = reader.attempt(Json::signToken);

        final String digits = digitsToken(reader);
        if (digits == null) return null;

        final StringBuilder sb = new StringBuilder("E");
        ifNonNull(sign, sb::append);
        sb.append(digits);
        return sb.toString();
    }

    static String signToken(final Source reader) throws Exception {
        if (reader.match("+")) return "+";
        if (reader.match("-")) return "-";
        return null;
    }

    static String fractionToken(final Source reader) throws Exception {
        if (reader.read() != '.') return null;

        final String digits = Json.digitsToken(reader);
        if (digits == null) return null;

        return "." + digits;
    }

    static String integerToken(final Source reader) throws Exception {
        final boolean negated = reader.match(copy -> copy.read() == '-');
        String number;

        number = reader.attempt(copy -> {
            String oneNine = Json.oneNineToken(copy);
            if (oneNine == null) return null;

            final String digits = Json.digitsToken(copy);
            if (digits == null) return null;

            return oneNine + digits;
        });

        if (number == null) number = reader.attempt(Json::digitToken);
        if (number != null) number = (negated ? "-" : "") + number;
        return number;
    }

    static String digitsToken(final Source reader) throws Exception {
        final String digit = reader.attempt(Json::digitToken);
        if (digit != null) {
            final String next = reader.attempt(Json::digitsToken);
            if (next == null) return digit;
            return digit + next;
        }

        return null;
    }

    static String oneNineToken(final Source reader) throws Exception {
        final int i = reader.read();
        final String asString = Character.toString(i);
        if ("123456789".contains(asString)) return asString;
        return null;
    }

    static String charactersToken(final Source reader) throws Exception {
        String c;
        if ((c = reader.attempt(Json::characterToken)) != null) {
            return c + charactersToken(reader);
        }

        return "";
    }

    static String characterToken(final Source reader) throws Exception {
        final int i = reader.read();
        if (i >= 0x0020 && i != '"' && i != '\\') {
            return Character.toString(i);
        } else if (i == '\\') {
            return escapeToken(reader);
        } else {
            return null;
        }
    }

    static String escapeToken(final Source reader) throws Exception {
        final int i = reader.read();
        if (i == -1) return null;
        return switch ((char) i) {
            case '"' -> "\"";
            case '\\' -> "\\";
            case '/' -> "/";
            case 'b' -> "\b";
            case 'f' -> "\f";
            case 'n' -> "\n";
            case 'r' -> "\r";
            case 't' -> "\t";
            case 'u' -> {
                final StringBuilder hexBuilder = new StringBuilder();
                for (int j = 0; j < 4; ++j) {
                    final Object hex = hexToken(reader);
                    if (hex == null) yield null;
                    hexBuilder.append(hex);
                }
                yield Character.toString(Integer.parseInt(hexBuilder.toString(), 16));
            }
            default -> null;
        };
    }

    static String hexToken(final Source reader) throws Exception {
        return oneOf(() -> reader.attempt((copy) -> {
            final int i = copy.read();
            if ((i >= 'a' && i <= 'f') || (i >= 'A' && i <= 'F'))
                return Character.toString(i);
            return null;
        }), () -> reader.attempt(Json::digitToken));
    }

    static String digitToken(final Source reader) throws Exception {
        final int i = reader.read();
        if (i >= '0' && i <= '9') return Character.toString(i);
        return null;
    }
}
