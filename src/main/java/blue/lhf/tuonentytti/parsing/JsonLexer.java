package blue.lhf.tuonentytti.parsing;

import blue.lhf.tuonentytti.model.JsonString;
import blue.lhf.tuonentytti.reader.Source;

import java.io.IOException;
import java.util.List;

import static blue.lhf.tuonentytti.parsing.Branches.match;

public class JsonLexer {
    private static final JsonParseException JSON_STRING_END_EXCEPTION = new JsonParseException(
        "Got end of string '\"' while reading characters");
    public static final JsonParseException JSON_DATA_END_EXCEPTION = new JsonParseException(
        "Expected more of string but reached end of JSON data");
    public static final JsonParseException JSON_NOT_DIGIT_BECAUSE_END_OF_DATA = new JsonParseException(
        "End of data, so not a digit from 1 to 9");

    static String exponent(final Source source) throws IOException {
        if (Branches.match(source, 'E', 'e')) {
            return "E" + sign(source) + digits(source);
        }

        return "";
    }

    static String sign(final Source source) throws IOException {
        if (Branches.match(source, '+')) return "+";
        if (Branches.match(source, '-')) return "-";
        return "";
    }

    static String fraction(final Source source) throws IOException {
        if (Branches.match(source, '.')) {
            return "." + digits(source);
        }

        return "";
    }

    static String integer(final Source source) throws IOException, JsonParseException {
        return match(source, List.of(Branch.concatenate(JsonLexer::oneNine, JsonLexer::digits),
                                     JsonLexer::digit,
                                     Branch.concatenate(Branch.literal("-"),
                                                             JsonLexer::oneNine,
                                                             JsonLexer::digits),
                                     Branch.concatenate(
                                              Branch.literal("-"),
                                              JsonLexer::digit)));
    }

    static String digits(final Source source) throws IOException {
        final StringBuilder builder = new StringBuilder();
        while (true) {
            try {
                builder.append(digit(source));
            } catch (JsonParseException ex) {
                break;
            }
        }

        return builder.toString();
    }

    static String characters(final Source source) throws IOException {
        final StringBuilder builder = new StringBuilder();
        while (true) {
            try {
                final Source branched = source.branch();
                builder.append(character(branched));
                source.skip((int) (branched.position() - source.position()));
            } catch (JsonParseException ex) {
                break;
            }
        }

        return builder.toString();
    }

    static String character(final Source source) throws IOException, JsonParseException {
        final int read = source.read();
        if (read >= 0x0020 && read <= 0x10FFFF && read != '"' && read != '\\') {
            return Character.toString(read);
        } else if (read == '\\') {
            return escape(source);
        } else if (read == -1) {
            throw JSON_DATA_END_EXCEPTION;
        } else if (read == '\"') {
            throw JSON_STRING_END_EXCEPTION;
        } else
            throw new JsonParseException("%s (%04X) is not a valid character for a string.".formatted(
                Character.toString(read),
                read));
    }

    static String escape(final Source source) throws IOException, JsonParseException {
        final int read = source.read();
        return switch (read) {
            case '"', '\\', '/' -> Character.toString(read);
            case 'b' -> "\b";
            case 'f' -> "\f";
            case 'n' -> "\n";
            case 'r' -> "\r";
            case 't' -> "\t";
            case 'u' -> {
                final StringBuilder hexBuilder = new StringBuilder();
                for (int i = 0; i < 4; ++i) {
                    hexBuilder.append(hex(source));
                }

                yield Character.toString(Integer.parseInt(hexBuilder.toString(), 16));
            }
            default ->
                throw new JsonParseException("Unknown escape sequence: \\" + Character.toString(read));
        };
    }

    public static String quote(final String input) {
        return input
            .replace("\\", "\\\\") // has to be first
            .replace("\"", "\\\"")
            .replace("/", "\\/")
            .replace("\b", "\\b")
            .replace("\f", "\\f")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t");
    }

    static String hex(final Source source) throws IOException, JsonParseException {
        final int read = source.read();
        if ((read >= 'A' && read <= 'F') || (read >= 'a' && read <= 'f') || (read >= '0' && read <= '9'))
            return Character.toString(read);

        throw new JsonParseException("'" + Character.toString(read) + "' is not a valid hexadecimal digit");
    }

    static String digit(final Source source) throws IOException, JsonParseException {
        return match(source, List.of(JsonLexer::oneNine, Branch.literal("0")));
    }

    static String oneNine(final Source source) throws IOException, JsonParseException {
        final int read = source.read();
        if (read == -1) throw JSON_NOT_DIGIT_BECAUSE_END_OF_DATA;

        final String asText = Character.toString(read);
        if (read >= '1' && read <= '9') return asText;
        throw new JsonParseException(asText + " is not a digit from 1 to 9");
    }
}
