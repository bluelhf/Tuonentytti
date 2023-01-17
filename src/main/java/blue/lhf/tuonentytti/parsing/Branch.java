package blue.lhf.tuonentytti.parsing;

import blue.lhf.tuonentytti.reader.Source;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@FunctionalInterface
interface Branch<T> {
    static <T> Branch<T> constant(final Branch<?> sideEffect, T realValue) {
        return branched -> {
            sideEffect.apply(branched);
            return realValue;
        };
    }

    static Branch<String> literal(final String text) {
        return branched -> {
            final byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
            final StringBuilder inputBuilder = new StringBuilder();
            for (int i = 0, len = text.length(); i < len; ++i) {
                final int read = branched.read();
                if (read == -1) {
                    throw new JsonParseException("Not '" + text + "', because hit end of data at '" + inputBuilder + "'");
                }

                inputBuilder.append(Character.toString(read));

                if (read != bytes[i]) {
                    throw new JsonParseException("Text '" + inputBuilder + "' doesn't match '" + text + "'");
                }
            }

            return text;
        };
    }

    @SafeVarargs
    static Branch<String> concatenate(final Branch<String>... branches) {
        return branched -> {
            final StringBuilder builder = new StringBuilder();
            for (final Branch<String> branch : branches) {
                builder.append(branch.apply(branched));
            }

            return builder.toString();
        };
    }

    T apply(final Source source) throws IOException, JsonParseException;
}
