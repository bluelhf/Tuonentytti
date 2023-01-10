package blue.lhf.tuonentytti.source;

import blue.lhf.tuonentytti.util.*;

import java.io.*;

public interface Source {
    Source copy() throws Exception;

    void move(final long pointer) throws Exception;

    long pointer() throws Exception;

    boolean startsWith(final String subtext) throws Exception;

    default <T, E extends Exception> T attempt(final ThrowingFunction<Source, T, E> function) throws Exception {
        final Source copy = copy();
        final T result = function.apply(copy);
        if (result != null) {
            move(copy.pointer());
        }

        return result;
    }

    default boolean match(final String text) throws Exception {
        if (startsWith(text)) {
            move(pointer() + text.length());
            return true;
        }

        return false;
    }

    default <E extends Exception> boolean match(final ThrowingPredicate<Source, E> predicate) throws Exception {
        final Source copy = copy();
        if (predicate.test(copy)) {
            move(copy.pointer());
            return true;
        }

        return false;
    }

    int read() throws Exception;
}
