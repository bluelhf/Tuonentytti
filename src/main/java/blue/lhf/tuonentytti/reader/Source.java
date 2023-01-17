package blue.lhf.tuonentytti.reader;

import java.io.IOException;

public interface Source {
    Source branch();

    int read() throws IOException;

    long position();

    int peek() throws IOException;

    void skip(final int amount) throws IOException;
}
