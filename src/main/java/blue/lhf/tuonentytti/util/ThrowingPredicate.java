package blue.lhf.tuonentytti.util;

public interface ThrowingPredicate<T, E extends Exception> {
    boolean test(T t) throws E;
}
