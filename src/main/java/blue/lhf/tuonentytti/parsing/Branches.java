package blue.lhf.tuonentytti.parsing;

import blue.lhf.tuonentytti.reader.Source;

import java.io.IOException;
import java.util.List;

public class Branches {
    public static final JsonParseException JSON_NO_MATCHES = new JsonParseException(
        "Did not match any possible value");

    static <T> T match(final Source source, final List<Branch<T>> branches) throws IOException, JsonParseException {
        JsonParseException bubble = JSON_NO_MATCHES;
        for (final Branch<T> option : branches) {
            try {
                final Source branched = source.branch();
                final T result = option.apply(branched);
                source.skip((int) (branched.position() - source.position()));
                return result;
            } catch (JsonParseException ex) {
                if (bubble == JSON_NO_MATCHES) bubble = bubble.copy();
                bubble.addSuppressed(ex);
            }
        }

        throw bubble;
    }

    public static boolean match(final Source source, final char... chars) throws IOException {
        final int peeked = source.peek();
        for (final char ch : chars) {
            if (peeked == ch) {
                source.read();
                return true;
            }
        }

        return false;
    }
}
