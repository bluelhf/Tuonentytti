package blue.lhf.tuonentytti.util;

@FunctionalInterface
public interface ThrowingFunction<T, R, E extends Exception> {
    R apply(T t) throws Exception;
}
